package org.fbi.hmfsjz.online.action;

import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia4001;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4001;
import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.fbi.hmfsjz.online.service.Txn4001Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 4001-���ܶ˷��𣬷ֻ���Ϣ��ѯ
 */
public class Txn4001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn4001Action.class);

    @Override
    public Toa process(Tia tia) throws Exception {
        Tia4001 tia4001 = (Tia4001) tia;

        String bankID = ProjectConfigManager.getInstance().getProperty("bank.id");
        if (!bankID.equals(tia4001.BODY.BANK_ID)) {
            throw new RuntimeException(TxnRtnCode.OTHER_EXCEPTION.getCode() + "|����ID�Ǳ���");
        }

        logger.info("[4001�ֻ���Ϣ��ѯ]��ˮ�ţ�" + tia4001.INFO.REQ_SN +
                "  ����ID��" + tia4001.BODY.BANK_ID +
                "  �ֻ�����" + tia4001.BODY.ACCOUNT_NUM);


        Toa4001 toa = new Toa4001();
        toa.INFO.REQ_SN = tia4001.INFO.REQ_SN;
        toa.INFO.TXN_CODE = tia4001.INFO.TXN_CODE;
        toa.BODY.RESERVE = tia4001.BODY.RESERVE;
        toa.INFO.RET_CODE = TxnRtnCode.TXN_SECCESS.getCode();
        toa.BODY.ACCOUNTS = new Txn4001Service().qryActs(tia4001.BODY.ACCOUNTS);
        toa.BODY.ACCOUNT_NUM = String.valueOf(toa.BODY.ACCOUNTS.size());
        return toa;
    }
}