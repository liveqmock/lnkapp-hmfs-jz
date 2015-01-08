package org.fbi.hmfsjz.online.action;

import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia4003;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4003;
import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.fbi.hmfsjz.online.service.Txn4003Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 4003-���ܶ˷��𣬼��˻��ܲ�ѯ
 */
public class Txn4003Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn4003Action.class);

    @Override
    public Toa process(Tia tia) throws Exception {
        Tia4003 tia4003 = (Tia4003) tia;
        logger.info("[4003���˻��ܲ�ѯ]��ˮ�ţ�" + tia4003.INFO.REQ_SN +
                "  ����ID��" + tia4003.BODY.BANK_ID);
        String bankID = ProjectConfigManager.getInstance().getProperty("bank.id");
        Toa4003 toa = new Toa4003();
        toa.INFO.REQ_SN = tia4003.INFO.REQ_SN;
        toa.INFO.TXN_CODE = tia4003.INFO.TXN_CODE;
        toa.BODY.RESERVE = tia4003.BODY.RESERVE;
        if (!bankID.equals(tia4003.BODY.BANK_ID)) {
            toa.BODY.STS_CODE = "0004";
            toa.BODY.STS_MSG = "����ID�Ǳ���";
            throw new RuntimeException(TxnRtnCode.OTHER_EXCEPTION.getCode() + "|" + "����ID�Ǳ���");
        }
        toa.INFO.RET_CODE = TxnRtnCode.TXN_SECCESS.getCode();
        toa.BODY = new Txn4003Service().qryTotalInfo();
        return toa;
    }
}
