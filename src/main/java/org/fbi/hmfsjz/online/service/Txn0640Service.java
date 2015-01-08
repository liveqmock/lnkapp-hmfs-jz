package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.BillStsFlag;
import org.fbi.hmfsjz.enums.DrawQryStatus;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia3003;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3003;
import org.fbi.hmfsjz.repository.model.HmfsJzDraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 1500640 ֧ȡ��ѯ
 */
public class Txn0640Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0640Service.class);
    private DrawService drawService = new DrawService();

    public Toa process(String tellerID, String branchID, String billNo, String txnDate) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia3003 tia = new Tia3003();
        tia.BODY.DRAW_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        logger.info("[3001-�˿��Ϣ��ѯ-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.DRAW_BILLNO);

        // ���׷���
        Toa3003 toa = (Toa3003) new SyncSocketClient().onRequest(tia);
        if (toa == null) throw new RuntimeException("�����쳣��");

        logger.info("[3003-֧ȡ����Ϣ��ѯ-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                " ���ţ�" + toa.BODY.DRAW_BILLNO +
                " ������" + toa.BODY.DETAIL_NUM +
                " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                " ״̬˵����" + toa.BODY.BILL_STS_TITLE);

        List<HmfsJzDraw> drawList = transToa3003ToDraws(tellerID, branchID, toa, txnDate);
        // ����
        drawService.saveDrawBills(drawList, billNo);
        return toa;
    }

    private List<HmfsJzDraw> transToa3003ToDraws(String tellerID, String branchID, Toa3003 toa3003, String txnDate) {

        if (!DrawQryStatus.VALAID.getCode().equals(toa3003.BODY.BILL_STS_CODE)) {
            throw new RuntimeException("״̬��" + toa3003.BODY.BILL_STS_CODE + toa3003.BODY.BILL_STS_TITLE);
        }
        List<HmfsJzDraw> draws = new ArrayList<HmfsJzDraw>();
        for (Toa3003.Detail detail : toa3003.BODY.DETAILS) {
            HmfsJzDraw draw = new HmfsJzDraw();
            draw.setBillno(toa3003.BODY.DRAW_BILLNO);
            draw.setBillStsCode(toa3003.BODY.BILL_STS_CODE);
            draw.setBillStsTitle(toa3003.BODY.BILL_STS_TITLE);
            draw.setHouseId(detail.HOUSE_ID);
            draw.setHouseArea(detail.HOUSE_AREA);
            draw.setAreaAccount(detail.AREA_ACCOUNT);
            draw.setHouseAccount(detail.HOUSE_ACCOUNT);
            draw.setHouseLocation(detail.HOUSE_LOCATION);
            draw.setReserve(toa3003.BODY.RESERVE);
            draw.setOperDate(txnDate.substring(0, 8));
            draw.setOperTime(txnDate.substring(8));
            draw.setQryTxnCode("3003");
            draw.setBookType(BillBookType.DRAW.getCode());            // �������� 00-�տ�  10-�˿� 20-֧ȡ
            draw.setStsFlag(BillStsFlag.UNBOOK.getCode());            // ���ݱ�־ 0-δ���� 1-����δȷ�� 2-������ȷ��
            draw.setOperId(tellerID);
            draw.setDeptId(branchID);
            draw.setTxnAmt(StringUtils.isEmpty(detail.DRAW_MONEY) ? null : new BigDecimal(detail.DRAW_MONEY));
            draws.add(draw);
        }
        return draws;
    }
}
