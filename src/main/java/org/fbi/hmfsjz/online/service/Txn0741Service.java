package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.enums.SendFlag;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia3004;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3004;
import org.fbi.hmfsjz.helper.ObjectFieldsCopier;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.*;
import org.fbi.hmfsjz.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 1500741 ֧ȡȷ�� ҵ���߼�
 */
public class Txn0741Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0741Service.class);
    private DrawService drawService = new DrawService();
    private ActirtService actirtService = new ActirtService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String drawNo, String txnDate) {

        Tia3004 tia = new Tia3004();
        tia.BODY.DRAW_BILLNO = drawNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJzDraw draw = drawService.qryDrawByNo(drawNo);
        if (draw == null) {
            throw new RuntimeException("֧ȡ���Ų�����:" + drawNo);
        }
        draw.setCfmTxnCode("3004");
        draw.setActSerialNo(serialNo);
        draw.setOperId(tellerID);
        draw.setDeptId(branchID);
        draw.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzDrawMapper drawMapper = session.getMapper(HmfsJzDrawMapper.class);
            drawMapper.updateByPrimaryKey(draw);
            HmfsJzActMapper actMapper = session.getMapper(HmfsJzActMapper.class);
            HmfsJzActExample actExample = new HmfsJzActExample();
            actExample.createCriteria().andHouseAccountEqualTo(draw.getHouseAccount());
            List<HmfsJzAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {
                logger.error("֧ȡ�ֻ��˺Ų����ڣ��ֻ���:" + draw.getHouseAccount() + " ֧ȡ���ţ�" + draw.getBillno());
                throw new RuntimeException("�ֻ��˺Ų�����:" + draw.getHouseAccount() + " ֧ȡ���ţ�" + draw.getBillno());
            } else {
                // ֧ȡ
                HmfsJzAct act = actList.get(0);
                String houseAct = act.getHouseAccount();

                // ֧ȡ��ˮ
                HmfsJzActTxn txn = new HmfsJzActTxn();
                ObjectFieldsCopier.copyFields(draw, txn);
                txn.setPkid(UUID.randomUUID().toString());
                txn.setActSerialNo(serialNo);
                txn.setTxnCode("3004");
                txn.setOperDate(txnDate.substring(0, 8));
                txn.setOperTime(txnDate.substring(8));
                txn.setBookType(BillBookType.DRAW.getCode());
                HmfsJzActTxnMapper acttxnMapper = session.getMapper(HmfsJzActTxnMapper.class);
                acttxnMapper.insert(txn);
                logger.info("[3004-֧ȡȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.DRAW_BILLNO);
                // ��Ϣ������ˮ
                CommonMapper cmnMapper = session.getMapper(CommonMapper.class);
                String maxSerialNo = cmnMapper.qryMaxSerialNo(houseAct);
                String interestSerialNo = getInterestNo(maxSerialNo);
                HmfsJzActTxn inttxn = new HmfsJzActTxn();
                ObjectFieldsCopier.copyFields(draw, inttxn);
                inttxn.setPkid(UUID.randomUUID().toString());
                inttxn.setActSerialNo(interestSerialNo);
                inttxn.setTxnCode("5002");
                inttxn.setOperDate(txnDate.substring(0, 8));
                inttxn.setOperTime(txnDate.substring(8));
                BigDecimal currentRate = actirtService.qryCurrentRate();    // ����������
                BigDecimal curRate = currentRate.divide(new BigDecimal("360.0"), 10, BigDecimal.ROUND_HALF_DOWN);
                BigDecimal interAmt = txn.getTxnAmt().multiply(curRate)
                        .multiply(new BigDecimal(actirtService.daysBetween(inttxn.getOperDate(), act.getIntDate())));
                inttxn.setBookType(BillBookType.INTEREST_DRAW_CURRENT.getCode());
                inttxn.setTxnAmt(interAmt);
                acttxnMapper.insert(inttxn);

                // ��Ϣ�ύ
                HmfsJzInterest interest = new HmfsJzInterest();
                interest.setPkid(UUID.randomUUID().toString());
                interest.setInterest(interAmt);
                interest.setInterestNo("JX" + houseAct + interestSerialNo);
                interest.setHouseId(act.getHouseId());
                interest.setHouseAccount(act.getHouseAccount());
                interest.setHouseLocation(act.getHouseLocation());
                interest.setOwner(act.getOwner());
                interest.setBeforeAmt(act.getBalAmt());     // ��Ϣǰ�ʽ�ָ��Ϣǰ���
                BigDecimal nowBal = act.getBalAmt().subtract(draw.getTxnAmt()).add(interAmt);
                interest.setAfterAmt(nowBal);               // ��Ϣ���ʽ�ָ��Ϣ�����
                interest.setBeginDate(toDate10(act.getIntDate()));    // ��Ϣ��
                interest.setEndDate(toDate10(inttxn.getOperDate()));  // ������
                interest.setCapital(draw.getTxnAmt());
                interest.setRate(currentRate.toString());
                interest.setOperTime(txnDate);
                interest.setOperId(tellerID);
                interest.setSerialNo(interestSerialNo);
                interest.setTxnCode("5002");
                interest.setIntName("֧ȡ��Ϣ");
                interest.setDeptId(branchID);
                interest.setSendFlag(SendFlag.UNSEND.getCode());
                HmfsJzInterestMapper interestMapper = session.getMapper(HmfsJzInterestMapper.class);
                interestMapper.insert(interest);
                // �����˻�
                act.setBalAmt(nowBal);
                act.setIntAmt(act.getIntAmt().add(interAmt));
                if (new BigDecimal("0.00").compareTo(act.getBalAmt()) > 0) {
                    throw new RuntimeException("�ֻ�����");
                }
                actMapper.updateByPrimaryKey(act);
                logger.info("�����³ɹ����ֻ���:" + draw.getHouseAccount() + " ���ţ�" + draw.getBillno());

                logger.info("[3004-֧ȡȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.DRAW_BILLNO);

                // ���׷���
                Toa3004 toa = (Toa3004) new SyncSocketClient().onRequest(tia);
                if (toa == null) throw new RuntimeException("�����쳣��");

                logger.info("[3004-֧ȡȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                        " ���ţ�" + toa.BODY.DRAW_BILLNO +
                        " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                        " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
                session.commit();
                return toa;
            }
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    private String toDate10(String date8) {
        return date8.substring(0, 4) + "-" + date8.substring(4, 6) + "-" + date8.substring(6, 8);
    }

    private String getInterestNo(String maxNo) {
        if(StringUtils.isEmpty(maxNo)) return "000001";
        else {
            int num = Integer.parseInt(maxNo);
            num++;
            return StringUtils.leftPad(String.valueOf(num), 6, "0");
        }
    }
}