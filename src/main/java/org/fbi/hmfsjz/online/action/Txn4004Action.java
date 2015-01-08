package org.fbi.hmfsjz.online.action;

import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia4004;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4004;
import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.fbi.hmfsjz.online.service.Txn4004Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 4004-���ܶ˷��𣬷ֻ���ˮ��ѯ
 */
public class Txn4004Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn4004Action.class);

    @Override
    public Toa process(Tia tia) throws Exception {
        Tia4004 tia4004 = (Tia4004) tia;
        logger.info("[4004�ֻ�������ˮ��ѯ]��ˮ�ţ�" + tia4004.INFO.REQ_SN +
                "  ����ID��" + tia4004.BODY.BANK_ID +
                "  �ֻ�����" + tia4004.BODY.ACCOUNT_NUM);

        String bankID = ProjectConfigManager.getInstance().getProperty("bank.id");
        if (!bankID.equals(tia4004.BODY.BANK_ID)) {
            throw new RuntimeException(TxnRtnCode.OTHER_EXCEPTION.getCode() + "|" + "����ID�Ǳ���");
        }

        Toa4004 toa = new Toa4004();
        toa.INFO.REQ_SN = tia4004.INFO.REQ_SN;
        toa.INFO.TXN_CODE = tia4004.INFO.TXN_CODE;
        toa.BODY.RESERVE = tia4004.BODY.RESERVE;
        toa.INFO.RET_CODE = TxnRtnCode.TXN_SECCESS.getCode();
        toa.BODY.DETAILS = new Txn4004Service().qryActTxns(tia4004.BODY.ACCOUNTS);
        toa.BODY.DETAIL_NUM = String.valueOf(toa.BODY.DETAILS.size());
        return toa;
    }
}
