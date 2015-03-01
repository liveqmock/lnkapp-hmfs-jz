package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia1001;
import org.fbi.hmfsjz.gateway.domain.txn.Toa1001;
import org.fbi.hmfsjz.repository.model.HmfsJzBill;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500710 �ɿ��ѯ���� ҵ���߼�
 */
public class Txn0710Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0710Service.class);
    private BillService billService = new BillService();

    public Toa process(String tellerID, String branchID, String billNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia1001 tia = new Tia1001();
        tia.BODY.PAY_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[1001-�ɿ��Ϣ��ѯ-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.PAY_BILLNO);
        // ���׷���
        Toa1001 toa = (Toa1001) new SyncSocketClient().onRequest(tia);

        if (toa == null) throw new RuntimeException("�����쳣��");

        logger.info("[1001-�ɿ��Ϣ��ѯ-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                " ���ţ�" + toa.BODY.PAY_BILLNO +
                " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                " ״̬˵����" + toa.BODY.BILL_STS_TITLE);

        HmfsJzBill bill = transToa1001ToBill(tellerID, branchID, toa);
        // ����
        if (billService.saveDepositBill(bill)) {
            return toa;
        } else {
            // ����ʧ��
            throw new RuntimeException("�ɿ��Ϣ����ʧ��");
        }
    }

    private HmfsJzBill transToa1001ToBill(String operid, String branchID, Toa1001 toa1001) {
        HmfsJzBill bill = new HmfsJzBill();
        bill.setBillno(toa1001.BODY.PAY_BILLNO);
        bill.setBillStsCode(toa1001.BODY.BILL_STS_CODE);
        bill.setBillStsTitle(toa1001.BODY.BILL_STS_TITLE);
        bill.setHouseId(toa1001.BODY.HOUSE_ID);
        bill.setHouseLocation(toa1001.BODY.HOUSE_LOCATION);
        bill.setHouseArea(toa1001.BODY.HOUSE_AREA);
        bill.setStandard(toa1001.BODY.STANDARD);
        bill.setTxnAmt(StringUtils.isEmpty(toa1001.BODY.TXN_AMT) ? null : new BigDecimal(toa1001.BODY.TXN_AMT.trim()));
        bill.setPayBank(toa1001.BODY.PAY_BANK);
        bill.setAreaAccount(toa1001.BODY.AREA_ACCOUNT);
        bill.setHouseAccount(toa1001.BODY.HOUSE_ACCOUNT);
        bill.setCardType(toa1001.BODY.CARD_TYPE);
        bill.setCardNo(toa1001.BODY.CARD_NO);
        bill.setOwner(toa1001.BODY.OWNER);
        bill.setTel(toa1001.BODY.TEL);
        bill.setReserve(toa1001.BODY.RESERVE);
        bill.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        bill.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
        bill.setQryTxnCode("1001");
        bill.setOperId(operid);
        bill.setDeptId(branchID);
        bill.setBookType(BillBookType.DEPOSIT.getCode());         // �������� 00-�տ�  10-�˿� 20-֧ȡ 99-����
        bill.setStsFlag(BillStsFlag.UNBOOK.getCode());            // ���ݱ�־ 0-δ���� 1-����δȷ�� 2-������ȷ��
        return bill;
    }


}
