package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3004;
import org.fbi.hmfsjz.online.service.Txn0641Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// ֧ȡȷ��
public class T0741Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");
        // ֧ȡ���
        String drawNo = fieldArray[0];

        logger.info("[1500641][3004][hmfsjz ֧ȡȷ��][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [֧ȡ���] " + drawNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime");
        try {
            // ���׷���
            Toa3004 toa = (Toa3004) new Txn0641Service().process(tellerID, branchID, serialNo, drawNo, txnDate);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500641][3004][hmfsjz ֧ȡȷ��]ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa3004 toa3004) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3004.BODY.DRAW_BILLNO).append("|")                  // ֧ȡ���
                .append(nullToEmpty(toa3004.BODY.BILL_STS_CODE)).append("|")     // ״̬����
                .append(nullToEmpty(toa3004.BODY.BILL_STS_TITLE)).append("|")    // ״̬˵��
                .append(nullToEmpty(toa3004.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
