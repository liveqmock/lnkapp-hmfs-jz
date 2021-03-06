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

//	即墨房屋维修资金支取查询
public class T0740Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(request.getRequestBody()), "|");

        // 编号
        String billNo = fieldArray[0];
        String branchID = request.getHeader("branchId");
        String tellerID = request.getHeader("tellerId");

        logger.info("[1500740][3003][hmfsjz 支取查询][网点号]" + branchID + "[柜员号]" + tellerID
                + "  [支取编号] " + billNo);

        String txnTime = request.getHeader("txnTime");
        try {
            Toa3003 toa = (Toa3003) new Txn0740Service().process(tellerID, branchID, billNo, txnTime);
            response.setResponseBody(assembleStr(toa).getBytes(THIRDPARTY_SERVER_CODING));

        } catch (Exception e) {
            logger.error("[1500740][3003][hmfsjz 支取单查询]失败", e);
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
        strBuilder.append(toa3003.BODY.DRAW_BILLNO).append("|")                  // 支取单编号
                .append(nullToEmpty(toa3003.BODY.BILL_STS_CODE)).append("|")     // 状态代码
                .append(nullToEmpty(toa3003.BODY.BILL_STS_TITLE)).append("|")    // 状态说明
                .append(nullToEmpty(totalAmt.toString())).append("|")               // 支取总金额
                .append(nullToEmpty(toa3003.BODY.DETAIL_NUM)).append("|");       // 笔数
        if (DrawQryStatus.VALAID.getCode().equals(toa3003.BODY.BILL_STS_CODE)) {
            for (Toa3003.Detail detail : toa3003.BODY.DETAILS) {
                strBuilder.append(nullToEmpty(detail.HOUSE_ID)).append(",")          // 房屋编号
                        .append(nullToEmpty(detail.AREA_ACCOUNT)).append(",")      // 专户账号
                        .append(nullToEmpty(detail.HOUSE_ACCOUNT)).append(",")     // 分户账号
                        .append(nullToEmpty(detail.HOUSE_LOCATION)).append(",")      // 房屋坐落
                        .append(nullToEmpty(detail.HOUSE_AREA)).append(",")        // 建筑面积
                        .append(nullToEmpty(detail.DRAW_MONEY)).append("|");
            }
        }
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
