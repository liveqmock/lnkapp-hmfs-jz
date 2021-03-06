package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia2001;
import org.fbi.hmfsjz.gateway.domain.txn.Toa2001;
import org.fbi.hmfsjz.helper.ObjectFieldsCopier;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzActMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzActTxnMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzBillMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzAct;
import org.fbi.hmfsjz.repository.model.HmfsJzActExample;
import org.fbi.hmfsjz.repository.model.HmfsJzActTxn;
import org.fbi.hmfsjz.repository.model.HmfsJzBill;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500711 缴款单缴款确认 业务逻辑 每个缴款单号对应的分户有且只有一个，销户时需将原分户删除，重新开户
 * **** 注意缴款交易需参考计息协议 ******
 * 开户时，记录首存日期、计息日、上次存款日为交易日期
 * 续缴款时，更新上次存款日、计息日为交易日期
 */
public class Txn0711Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0711Service.class);
    private BillService billService = new BillService();

    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String billNo, String txnDate) {

        Tia2001 tia = new Tia2001();
        tia.BODY.PAY_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJzBill bill = billService.qryBillByNo(billNo);
        if (bill == null) {
            throw new RuntimeException("缴款单号不存在:" + billNo);
        }
        bill.setCfmTxnCode("2001");
        bill.setActSerialNo(serialNo);
        bill.setOperId(tellerID);
        bill.setDeptId(branchID);
        bill.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzBillMapper mapper = session.getMapper(HmfsJzBillMapper.class);
            mapper.updateByPrimaryKey(bill);
            HmfsJzActMapper actMapper = session.getMapper(HmfsJzActMapper.class);
            HmfsJzActExample actExample = new HmfsJzActExample();
            actExample.createCriteria().andHouseAccountEqualTo(bill.getHouseAccount());
            List<HmfsJzAct> actList = actMapper.selectByExample(actExample);
            // TODO
            if (actList.size() == 0) {                   // 开户
                HmfsJzAct act = new HmfsJzAct();
                ObjectFieldsCopier.copyFields(bill, act);
                act.setBalAmt(bill.getTxnAmt());
                act.setIntAmt(new BigDecimal("0.00"));   // 利息额
                act.setMngAmt(new BigDecimal("0.00"));   // 增值收益
                act.setFirstDate(txnDate);               // 首存日期
                act.setLastDepDate(txnDate);             // 上次存款日期
                act.setIntDate(txnDate);                 // 计息日为首次存款日期
                act.setFirstAmt(bill.getTxnAmt());       // 首次缴存金额
                actMapper.insert(act);
                logger.info("分户开户成功，分户号:" + bill.getHouseAccount() + " 缴款单号：" + bill.getBillno());
                // 记账明细
            } else {   // 已开户，视为续缴.
                HmfsJzAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().add(bill.getTxnAmt()));
                act.setLastDepDate(txnDate);             // 上次存款日期
//                act.setIntDate(txnDate);
                actMapper.updateByPrimaryKey(act);
                logger.info("余额更新成功，分户号:" + bill.getHouseAccount() + " 单号：" + bill.getBillno());
            }
            HmfsJzActTxn txn = new HmfsJzActTxn();
            ObjectFieldsCopier.copyFields(bill, txn);
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("2001");
            txn.setOperDate(txnDate.substring(0, 8));
            txn.setOperTime(txnDate.substring(8));
            txn.setBookType(BillBookType.DEPOSIT.getCode());
            HmfsJzActTxnMapper acttxnMapper = session.getMapper(HmfsJzActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[2001-缴款确认-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.PAY_BILLNO);
            // 交易发起
            Toa2001 toa = (Toa2001) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("网络异常。");

            logger.info("[2001-缴款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                    " 单号：" + toa.BODY.PAY_BILLNO +
                    " 状态码：" + toa.BODY.BILL_STS_CODE +
                    " 状态说明：" + toa.BODY.BILL_STS_TITLE);
            session.commit();
            return toa;
        } catch (Exception e) {
            session.rollback();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}