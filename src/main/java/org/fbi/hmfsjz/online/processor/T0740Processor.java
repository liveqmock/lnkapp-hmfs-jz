package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.enums.DrawQryStatus;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3003;
import org.fbi.hmfsjz.online.service.Txn0740Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

//	��ī����ά���ʽ�֧ȡ��ѯ
public class T0740Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        // ���
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500740][3003][hmfsjz ֧ȡ��ѯ][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [֧ȡ���] " + billNo);

        String txnTime = request.getHeader("txnTime");
        try {
            Toa3003 toa = (Toa3003) new Txn0740Service().process(tellerID, branchID, billNo, txnTime);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));

        } catch (Exception e) {
            logger.error("[1500740][3003][hmfsjz ֧ȡ����ѯ]ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa3003 toa3003) {
        StringBuilder strBuilder = new StringBuilder();
        BigDecimal totalAmt = new BigDecimal("0.00");
        if (DrawQryStatus.VALAID.getCode().equals(toa3003.BODY.BILL_STS_CODE)) {
            for (Toa3003.Detail detail : toa3003.BODY.DETAILS) {
                totalAmt = totalAmt.add(new BigDecimal(detail.DRAW_MONEY));
            }
        }
        strBuilder.append(toa3003.BODY.DRAW_BILLNO).append("|")                  // ֧ȡ�����
                .append(nullToEmpty(toa3003.BODY.BILL_STS_CODE)).append("|")     // ״̬����
                .append(nullToEmpty(toa3003.BODY.BILL_STS_TITLE)).append("|")    // ״̬˵��
                .append(nullToEmpty(totalAmt.toString())).append("|")               // ֧ȡ�ܽ��
                .append(nullToEmpty(toa3003.BODY.DETAIL_NUM)).append("|");       // ����
        if (DrawQryStatus.VALAID.getCode().equals(toa3003.BODY.BILL_STS_CODE)) {
            for (Toa3003.Detail detail : toa3003.BODY.DETAILS) {
                strBuilder.append(nullToEmpty(detail.HOUSE_ID)).append(",")          // ���ݱ��
                        .append(nullToEmpty(detail.AREA_ACCOUNT)).append(",")      // ר���˺�
                        .append(nullToEmpty(detail.HOUSE_ACCOUNT)).append(",")     // �ֻ��˺�
                        .append(nullToEmpty(detail.HOUSE_LOCATION)).append(",")      // ��������
                        .append(nullToEmpty(detail.HOUSE_AREA)).append(",")        // �������
                        .append(nullToEmpty(detail.DRAW_MONEY)).append("|");
            }
        }
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
