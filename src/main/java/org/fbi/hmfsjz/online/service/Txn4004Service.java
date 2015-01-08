package org.fbi.hmfsjz.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4004;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzActTxnMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzActTxn;
import org.fbi.hmfsjz.repository.model.HmfsJzActTxnExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * �ֻ�������ˮ��ѯ
 */
public class Txn4004Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn4004Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public List<Toa4004.Detail> qryActTxns(List<String> actNos) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzActTxnMapper mapper = session.getMapper(HmfsJzActTxnMapper.class);
            List<Toa4004.Detail> records = new ArrayList<Toa4004.Detail>();
            HmfsJzActTxnExample example = new HmfsJzActTxnExample();
            for (String act : actNos) {
                example.clear();
                example.createCriteria().andHouseAccountEqualTo(act);
                List<HmfsJzActTxn> dbActtxns = mapper.selectByExample(example);
                for (HmfsJzActTxn txn : dbActtxns) {
                    Toa4004.Detail record = new Toa4004.Detail();
                    record.HOUSE_ACCOUNT = act;
                    record.BILL_NO = txn.getBillno();
                    record.BOOK_FLAG = BillBookType.valueOfAlias(txn.getBookType()).typeToQryDetail();
                    record.TXN_MONEY = txn.getTxnAmt().toString();
                    record.TXN_TIME = txn.getOperDate();
                    records.add(record);
                }
            }
            return records;
        } finally {
            if (session != null) session.close();
        }
    }
}
