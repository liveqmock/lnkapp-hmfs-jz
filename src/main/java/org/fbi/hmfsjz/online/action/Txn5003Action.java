package org.fbi.hmfsjz.online.action;

import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia5003;
import org.fbi.hmfsjz.gateway.domain.txn.Toa5003;
import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.fbi.hmfsjz.online.service.Txn5003Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 5003-���ܶ˷��𣬷ֻ�����
 */
public class Txn5003Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn5003Action.class);

    @Override
    public Toa process(Tia tia) throws Exception {
        Tia5003 tia5003 = (Tia5003) tia;
        logger.info("[5003�ֻ�����]��ˮ�ţ�" + tia5003.INFO.REQ_SN +
                "  ����ID��" + tia5003.BODY.BANK_ID +
                "  �ֻ���" + tia5003.BODY.HOUSE_ACCOUNT);

        String bankID = ProjectConfigManager.getInstance().getProperty("bank.id");
        if (!bankID.equals(tia5003.BODY.BANK_ID)) {
            throw new RuntimeException(TxnRtnCode.OTHER_EXCEPTION.getCode() + "|" + "����ID�Ǳ���");
        }

        Toa5003 toa = new Toa5003();
        toa.INFO.REQ_SN = tia5003.INFO.REQ_SN;
        toa.INFO.TXN_CODE = tia5003.INFO.TXN_CODE;
        toa.BODY.RESERVE = tia5003.BODY.RESERVE;
        toa.INFO.RET_CODE = TxnRtnCode.TXN_SECCESS.getCode();
        toa.BODY = new Txn5003Service().cancelAct(tia5003.BODY.HOUSE_ID, tia5003.BODY.HOUSE_ACCOUNT);
        return toa;
    }
}
