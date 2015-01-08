package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.gateway.domain.txn.Toa3001;
import org.fbi.hmfsjz.online.service.Txn0620Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//	��ī����ά���ʽ��˿��ѯ
public class T0720Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        // ���
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500620][3001][hmfsjz �˿��ѯ][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [�˿�����] " + billNo);

        String serialNo = request.getHeader("serialNo");
        String txnDate = request.getHeader("txnTime");
        try {
            Toa3001 toa = (Toa3001)new Txn0620Service().process(tellerID, branchID, billNo, txnDate);

            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));

        } catch (Exception e) {
            logger.error("[1500620][3001][hmfsjz �˿��ѯ]ʧ��", e);
            throw new RuntimeException(e);
        }
    }


    private String assembleStr(Toa3001 toa3001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(toa3001.BODY.REFUND_BILLNO).append("|")                // �ɿ�����
                .append(nullToEmpty(toa3001.BODY.BILL_STS_CODE)).append("|")     // �ɿ״̬����
                .append(nullToEmpty(toa3001.BODY.BILL_STS_TITLE)).append("|")    // �ɿ״̬˵��
                .append(nullToEmpty(toa3001.BODY.RP_TYPE)).append("|")           // �˿����
                .append(nullToEmpty(toa3001.BODY.RP_MEMO)).append("|")           // �˿�����
                .append(nullToEmpty(toa3001.BODY.RP_MONEY)).append("|")          // �˿���
                .append(nullToEmpty(toa3001.BODY.PAY_BILL_NO)).append("|")       // �ɴ�֪ͨ����
                .append(nullToEmpty(toa3001.BODY.PAY_BANK)).append("|")          // �ɿ�����
                .append(nullToEmpty(toa3001.BODY.BANK_USER)).append("|")         // �����տ���
                .append(nullToEmpty(toa3001.BODY.BANK_CFM_DATE)).append("|")     // �����տ�����
                .append(nullToEmpty(toa3001.BODY.PAY_MONEY)).append("|")         // �����տ���
                .append(nullToEmpty(toa3001.BODY.HOUSE_ID)).append("|")          // ���ݱ��
                .append(nullToEmpty(toa3001.BODY.HOUSE_LOCATION)).append("|")    // ��������
                .append(nullToEmpty(toa3001.BODY.HOUSE_AREA)).append("|")        // �������
                .append(nullToEmpty(toa3001.BODY.STANDARD)).append("|")          // �ɴ��׼
                .append(nullToEmpty(toa3001.BODY.AREA_ACCOUNT)).append("|")      // ר���˺�
                .append(nullToEmpty(toa3001.BODY.HOUSE_ACCOUNT)).append("|")     // �ֻ��˺�
                .append(nullToEmpty(toa3001.BODY.CARD_TYPE)).append("|")         // ֤������
                .append(nullToEmpty(toa3001.BODY.CARD_NO)).append("|")           // ֤������
                .append(nullToEmpty(toa3001.BODY.OWNER)).append("|")             // ҵ������
                .append(nullToEmpty(toa3001.BODY.TEL)).append("|")               // ��ϵ�绰
                .append(nullToEmpty(toa3001.BODY.ACCEPT_DATE)).append("|")       // �ɴ���������
                .append(nullToEmpty(toa3001.BODY.RESERVE)).append("|");          // ������
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
