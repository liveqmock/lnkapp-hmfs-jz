package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia3002;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3002;
import org.fbi.hmfsjz.helper.ObjectFieldsCopier;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzActMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzActTxnMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzRefundMapper;
import org.fbi.hmfsjz.repository.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 1500621 退款确认 业务逻辑
 */
public class Txn0621Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0621Service.class);
    private RefundService refundService = new RefundService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String refundNo, String txnDate) {

        Tia3002 tia = new Tia3002();
        tia.BODY.REFUND_BILLNO = refundNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJzRefund refund = refundService.qryRefundByNo(refundNo);
        if (refund == null) {
            throw new RuntimeException("退款单号不存在:" + refundNo);
        }
        refund.setCfmTxnCode("3002");
        refund.setActSerialNo(serialNo);
        refund.setOperId(tellerID);
        refund.setDeptId(branchID);
        refund.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzRefundMapper refundMapper = session.getMapper(HmfsJzRefundMapper.class);
            refundMapper.updateByPrimaryKey(refund);
            HmfsJzActMapper actMapper = session.getMapper(HmfsJzActMapper.class);
            HmfsJzActExample actExample = new HmfsJzActExample();
            actExample.createCriteria().andHouseAccountEqualTo(refund.getHouseAccount());
            List<HmfsJzAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {
                logger.error("退款分户账号不存在，分户号:" + refund.getHouseAccount() + " 退款单号：" + refund.getBillno());
                throw new RuntimeException("分户账号不存在:" + refund.getHouseAccount() + " 退款单号：" + refund.getBillno());
            } else {   // 退款
                HmfsJzAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().subtract(refund.getTxnAmt()));
                if (new BigDecimal("0.00").compareTo(act.getBalAmt()) > 0) {
                    throw new RuntimeException("分户余额不足");
                }
                actMapper.updateByPrimaryKey(act);
                logger.info("余额更新成功，分户号:" + refund.getHouseAccount() + " 单号：" + refund.getBillno());
            }
            HmfsJzActTxn txn = new HmfsJzActTxn();
            ObjectFieldsCopier.copyFields(refund, txn);
            txn.setPkid(UUID.randomUUID().toString());
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("3002");
            txn.setOperDate(txnDate.substring(0, 8));
            txn.setOperTime(txnDate.substring(8));
            txn.setBookType(BillBookType.REFUND.getCode());
            HmfsJzActTxnMapper acttxnMapper = session.getMapper(HmfsJzActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[3002-退款确认-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.REFUND_BILLNO);
            // 交易发起
            Toa3002 toa = (Toa3002) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("网络异常。");

            logger.info("[3002-退款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                    " 单号：" + toa.BODY.REFUND_BILLNO +
                    " 状态码：" + toa.BODY.BILL_STS_CODE +
                    " 状态说明：" + toa.BODY.BILL_STS_TITLE);
            session.commit();
            return toa;
        } catch (Exception e) {
            session.rollback();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("退款失败");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}