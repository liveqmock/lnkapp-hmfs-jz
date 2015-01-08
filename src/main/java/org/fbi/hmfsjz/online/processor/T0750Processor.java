package org.fbi.hmfsjz.online.processor;

import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.gateway.domain.txn.Toa5001;
import org.fbi.hmfsjz.online.service.Txn0650Service;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// ���˻���ѯ
public class T0750Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // ����������
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");
        // ����
        String houseAccout = fieldArray[0];

        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        List<String> acts = new ArrayList<String>();
        acts.add(houseAccout);

        logger.info("[1500650�ֻ���Ϣ��ѯ][�����]" + branchID + "[��Ա��]" + tellerID + "  [�ֻ��˺�] " + houseAccout);
        try {
            Toa5001 toa = (Toa5001) new Txn0650Service().process(tellerID, branchID, acts);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));
        } catch (Exception e) {
            logger.error("[1500650][5001][hmfsjz �ֻ��˺Ų�ѯ]ʧ��", e);
            throw new RuntimeException(e);
        }
    }

    private String assembleStr(Toa5001 toa5001) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(nullToEmpty(toa5001.BODY.ACCOUNT_NUM)).append("|");
        for (Toa5001.Account act : toa5001.BODY.ACCOUNTS) {
            strBuilder.append(act.HOUSE_ACCOUNT).append(",")
                    .append(act.STATUS).append(",")
                    .append(act.HOUSE_ID).append(",")
                    .append(act.HOUSE_LOCATION).append(",")
                    .append(act.HOUSE_AREA).append(",")
                    .append(act.STANDARD).append(",")
                    .append(act.PAY_MONEY).append(",")
                    .append(act.BALAMT).append(",")
                    .append(act.ACCEPT_DATE).append("|");
        }
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
