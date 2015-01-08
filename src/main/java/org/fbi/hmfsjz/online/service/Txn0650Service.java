package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.txn.Tia5001;
import org.fbi.hmfsjz.gateway.domain.txn.Toa5001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500650 �ֻ���ѯ
 */
public class Txn0650Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0650Service.class);

    public Toa process(String tellerID, String branchID, List<String> houseAccounts) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        Tia5001 tia = new Tia5001();
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        tia.BODY.ACCOUNT_NUM = String.valueOf(houseAccounts.size());
        tia.BODY.ACCOUNTS = houseAccounts;
        // ���׷���
        Toa5001 toa = (Toa5001) new SyncSocketClient().onRequest(tia);
        if (toa == null) throw new RuntimeException("�����쳣��");
        return toa;
    }
}
