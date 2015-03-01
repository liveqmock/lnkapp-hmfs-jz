package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia3001;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3001;
import org.fbi.hmfsjz.repository.model.HmfsJzRefund;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500720 �˿��ѯ���� ҵ���߼�  ÿ���ɿ�Ŷ�Ӧ�ķֻ�����ֻ��һ��������ʱ�轫ԭ�ֻ�ɾ�������¿���
 */
public class Txn0720Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0720Service.class);
    private RefundService refundService = new RefundService();

    public Toa process(String tellerID, String branchID, String billNo, String txnDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia3001 tia = new Tia3001();
        tia.BODY.REFUND_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[3001-�˿��Ϣ��ѯ-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.REFUND_BILLNO);

        // ���׷���
        Toa3001 toa = (Toa3001) new SyncSocketClient().onRequest(tia);
        if (toa == null) throw new RuntimeException("�����쳣��");

        logger.info("[3001-�˿��Ϣ��ѯ-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                " ���ţ�" + toa.BODY.REFUND_BILLNO +
                " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                " ״̬˵����" + toa.BODY.BILL_STS_TITLE);

        HmfsJzRefund refund = transToa3001ToRefund(tellerID, branchID, toa, txnDate);
        // ����
        if (refundService.saveRefundBill(refund)) {
            return toa;
        } else {
            throw new RuntimeException("�˿��Ϣ����ʧ��");
        }
    }

    private HmfsJzRefund transToa3001ToRefund(String tellerID, String branchID, Toa3001 toa3001, String txnDate) {
        HmfsJzRefund refund = new HmfsJzRefund();
        refund.setBillno(toa3001.BODY.REFUND_BILLNO);
        refund.setBillStsCode(toa3001.BODY.BILL_STS_CODE);
        refund.setBillStsTitle(toa3001.BODY.BILL_STS_TITLE);
        refund.setHouseId(toa3001.BODY.HOUSE_ID);
        refund.setHouseLocation(toa3001.BODY.HOUSE_LOCATION);
        refund.setHouseArea(toa3001.BODY.HOUSE_AREA);
        refund.setStandard(toa3001.BODY.STANDARD);
        refund.setPayBank(toa3001.BODY.PAY_BANK);
        refund.setAreaAccount(toa3001.BODY.AREA_ACCOUNT);
        refund.setHouseAccount(toa3001.BODY.HOUSE_ACCOUNT);
        refund.setCardType(toa3001.BODY.CARD_TYPE);
        refund.setCardNo(toa3001.BODY.CARD_NO);
        refund.setOwner(toa3001.BODY.OWNER);
        refund.setTel(toa3001.BODY.TEL);
        refund.setReserve(toa3001.BODY.RESERVE);
        refund.setOperDate(txnDate.substring(0, 8));
        refund.setOperTime(txnDate.substring(8));
        refund.setQryTxnCode("3001");
        refund.setBookType(BillBookType.REFUND.getCode());         // �������� 00-�տ�  10-�˿� 20-֧ȡ
        refund.setStsFlag(BillStsFlag.UNBOOK.getCode());            // ���ݱ�־ 0-δ���� 1-����δȷ�� 2-������ȷ��
        refund.setRelateBillno(toa3001.BODY.PAY_BILL_NO);
        refund.setBankUser(toa3001.BODY.BANK_USER);
        refund.setOperId(tellerID);
        refund.setDeptId(branchID);
        refund.setBankCfmDate(toa3001.BODY.BANK_CFM_DATE);
        refund.setPayMoney(StringUtils.isEmpty(toa3001.BODY.PAY_MONEY) ? null : new BigDecimal(toa3001.BODY.PAY_MONEY));
        refund.setAcceptDate(toa3001.BODY.ACCEPT_DATE);
        refund.setRpType(toa3001.BODY.RP_TYPE);
        refund.setRpMemo(toa3001.BODY.RP_MEMO);
        refund.setTxnAmt(StringUtils.isEmpty(toa3001.BODY.PAY_MONEY) ? null : new BigDecimal(toa3001.BODY.RP_MONEY));
        return refund;
    }
}
