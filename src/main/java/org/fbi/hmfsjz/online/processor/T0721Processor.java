package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3002;
import org.fbi.hmfsjz.online.service.Txn0721Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class T0721Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        // �˿�����
        String refundNo = fieldArray[0];

        logger.info("[1500721][3002][hmfsjz �˿����˿�ȷ��][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [�˿�����] " + refundNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime");
        try {
            // ���׷���
            Toa3002 toa = (Toa3002) new Txn0721Service().process(tellerID, branchID, serialNo, refundNo, txnDate);

            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500721][3002][hmfsjz �˿�˿�ȷ��]ʧ��", e);
            throw new RuntimeException(e);
        }
    }


    private String assembleStr(Toa3002 toa3002) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3002.BODY.REFUND_BILLNO).append("|")                // �˿�����
                .append(nullToEmpty(toa3002.BODY.BILL_STS_CODE)).append("|")     // �˿״̬����
                .append(nullToEmpty(toa3002.BODY.BILL_STS_TITLE)).append("|")    // �˿״̬˵��
                .append(nullToEmpty(toa3002.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
