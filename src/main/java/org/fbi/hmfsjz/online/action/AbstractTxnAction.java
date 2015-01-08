package org.fbi.hmfsjz.online.action;

import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    public Toa run(Tia tia) {
        try {
            return process(tia);
        } catch (Exception e) {
            logger.error("ҵ�����쳣", e);
            throw new RuntimeException((e.getMessage() == null) ? TxnRtnCode.TXN_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected Toa process(Tia tia) throws Exception;
}
