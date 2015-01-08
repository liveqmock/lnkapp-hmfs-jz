package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.enums.BillTxnStatus;
import org.fbi.hmfsjz.gateway.domain.txn.Toa2001;
import org.fbi.hmfsjz.online.service.Txn0611Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// �ɿ�ȷ��
public class T0711Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        // �ɿ�����
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500611][2001][hmfsjz �ɿ���ɿ�ȷ��][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [�ɿ�����] " + billNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime").substring(0, 8);
        try {

            Toa2001 toa = (Toa2001) new Txn0611Service().process(tellerID, branchID, serialNo, billNo, txnDate);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
            if (!BillTxnStatus.PAYED_SECCESS.getCode().equals(toa.BODY.BILL_STS_CODE)
                    && !BillTxnStatus.CONFIRMED.getCode().equals(toa.BODY.BILL_STS_CODE)) {
                throw new RuntimeException(StringUtils.isEmpty(toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE) ?
                        (toa.INFO.RET_CODE + toa.INFO.ERR_MSG) : (toa.BODY.BILL_STS_CODE + toa.BODY.BILL_STS_TITLE));
            }
        } catch (Exception e) {
            logger.error("[1500611][2001][hmfsjz �ɿ�ɿ�ȷ��]ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa2001 toa2001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa2001.BODY.PAY_BILLNO).append("|")                   // �ɿ�����
                .append(nullToEmpty(toa2001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa2001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa2001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
