package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.gateway.domain.txn.Toa1001;
import org.fbi.hmfsjz.online.service.Txn0710Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// �ɿ��ѯ
public class T0710Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // �ɿ�����
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500710�ɿ�����Ϣ��ѯ][�����]" + branchID + "[��Ա��]" + tellerID + "  [�ɿ�����] " + billNo);
        try {
            Toa1001 toa = (Toa1001) new Txn0710Service().process(tellerID, branchID, billNo);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500710][1001][hmfsjz �ɿ��ѯ]ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa1001 toa1001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa1001.BODY.PAY_BILLNO).append("|")                   // �ɿ�����
                .append(nullToEmpty(toa1001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa1001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa1001.BODY.HOUSE_ID)).append("|")          // ���ݱ��
                .append(nullToEmpty(toa1001.BODY.HOUSE_LOCATION)).append("|")    // ��������
                .append(nullToEmpty(toa1001.BODY.HOUSE_AREA)).append("|")        // �������
                .append(nullToEmpty(toa1001.BODY.STANDARD)).append("|")          // �ɴ��׼
                .append(nullToEmpty(toa1001.BODY.TXN_AMT)).append("|")           // ���
                .append(nullToEmpty(toa1001.BODY.PAY_BANK)).append("|")          // �ɿ�����
                .append(nullToEmpty(toa1001.BODY.AREA_ACCOUNT)).append("|")      // ר���˺�
                .append(nullToEmpty(toa1001.BODY.HOUSE_ACCOUNT)).append("|")     // �ֻ��˺�
                .append(nullToEmpty(toa1001.BODY.CARD_TYPE)).append("|")         // ֤������
                .append(nullToEmpty(toa1001.BODY.CARD_NO)).append("|")           // ֤������
                .append(nullToEmpty(toa1001.BODY.OWNER)).append("|")             // ҵ������
                .append(nullToEmpty(toa1001.BODY.TEL)).append("|")               // ��ϵ�绰
                .append(nullToEmpty(toa1001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
