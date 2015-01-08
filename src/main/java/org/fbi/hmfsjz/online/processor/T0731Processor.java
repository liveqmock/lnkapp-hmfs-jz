package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.enums.VoucherStatus;
import org.fbi.hmfsjz.online.service.Txn0631Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// ��ī����ά���ʽ�Ʊ��ʹ�ú�����
public class T0731Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // Ʊ�ݱ��
        String vchNo = fieldArray[1];
        // Ʊ��״̬
        String vchSts = fieldArray[2];
        // �ɿ���
        String billNo = fieldArray[0];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        if (VoucherStatus.USED.getCode().equals(vchSts)) {
            if (StringUtils.isEmpty(billNo)) {
                response.setHeader("rtnCode", TxnRtnCode.TXN_FAILED.getCode());
                response.setResponseBody("ʹ��Ʊ��ʱ��������ɿ��".getBytes(THIRDPARTY_SERVER_CODING));
            }
        }

        logger.info("[1500631Ʊ��ʹ��������][�����]" + branchID + "[��Ա��]" + tellerID
                + "  [Ʊ�ݱ��] " + vchNo + "[Ʊ��״̬]" + vchSts + "[�ɿ���]" + billNo);

        try {
            new Txn0631Service().process(branchID, tellerID, vchNo, billNo, vchSts);

        } catch (Exception e) {
            logger.error("[1500631][hmfsjz Ʊ��ʹ��������]ʧ��", e);
            throw new RuntimeException(e);
        }
    }
}
