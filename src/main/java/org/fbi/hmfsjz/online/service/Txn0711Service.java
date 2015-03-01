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
 * 1500711 �ɿ�ɿ�ȷ�� ҵ���߼� ÿ���ɿ�Ŷ�Ӧ�ķֻ�����ֻ��һ��������ʱ�轫ԭ�ֻ�ɾ�������¿���
 * **** ע��ɿ����ο���ϢЭ�� ******
 * ����ʱ����¼�״����ڡ���Ϣ�ա��ϴδ����Ϊ��������
 * ���ɿ�ʱ�������ϴδ���ա���Ϣ��Ϊ��������
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
            throw new RuntimeException("�ɿ�Ų�����:" + billNo);
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
            if (actList.size() == 0) {                   // ����
                HmfsJzAct act = new HmfsJzAct();
                ObjectFieldsCopier.copyFields(bill, act);
                act.setBalAmt(bill.getTxnAmt());
                act.setIntAmt(new BigDecimal("0.00"));   // ��Ϣ��
                act.setMngAmt(new BigDecimal("0.00"));   // ��ֵ����
                act.setFirstDate(txnDate);               // �״�����
                act.setLastDepDate(txnDate);             // �ϴδ������
                act.setIntDate(txnDate);                 // ��Ϣ��Ϊ�״δ������
                act.setFirstAmt(bill.getTxnAmt());       // �״νɴ���
                actMapper.insert(act);
                logger.info("�ֻ������ɹ����ֻ���:" + bill.getHouseAccount() + " �ɿ�ţ�" + bill.getBillno());
                // ������ϸ
            } else {   // �ѿ�������Ϊ����.
                HmfsJzAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().add(bill.getTxnAmt()));
                act.setLastDepDate(txnDate);             // �ϴδ������
//                act.setIntDate(txnDate);
                actMapper.updateByPrimaryKey(act);
                logger.info("�����³ɹ����ֻ���:" + bill.getHouseAccount() + " ���ţ�" + bill.getBillno());
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
            logger.info("[2001-�ɿ�ȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.PAY_BILLNO);
            // ���׷���
            Toa2001 toa = (Toa2001) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("�����쳣��");

            logger.info("[2001-�ɿ�ȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                    " ���ţ�" + toa.BODY.PAY_BILLNO +
                    " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                    " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
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