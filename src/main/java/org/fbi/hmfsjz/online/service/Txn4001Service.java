package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.ActStatus;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4001;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzActMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzAct;
import org.fbi.hmfsjz.repository.model.HmfsJzActExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分户信息查询
 */
public class Txn4001Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn4001Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public List<Toa4001.Account> qryActs(List<String> actNos) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzActMapper mapper = session.getMapper(HmfsJzActMapper.class);
            List<Toa4001.Account> records = new ArrayList<Toa4001.Account>();
            HmfsJzActExample example = new HmfsJzActExample();
            for (String act : actNos) {
                example.clear();
                example.createCriteria().andHouseAccountEqualTo(act);
                List<HmfsJzAct> dbActs = mapper.selectByExample(example);
                Toa4001.Account record = new Toa4001.Account();
                record.HOUSE_ACCOUNT = act;
                if (dbActs.isEmpty()) {
                    record.STATUS = ActStatus.NOT_EXIST.getCode();     // 不存在

                } else {
                    HmfsJzAct dbact = dbActs.get(0);
                    if (ActStatus.CANCEL.getCode().equals(dbact.getActStatus())) {
                        record.STATUS = ActStatus.CANCEL.getCode();     // 销户
                    } else {
                        record.STATUS = dbact.getActStatus();
                        record.HOUSE_ID = dbact.getHouseId();
                        record.HOUSE_LOCATION = dbact.getHouseLocation();
                        record.HOUSE_AREA = dbact.getHouseArea();
                        record.STANDARD = dbact.getStandard();
                        record.PAY_MONEY = dbact.getFirstAmt().toString();
                        record.BALAMT = dbact.getBalAmt().toString();
                        if (StringUtils.isEmpty(dbact.getFirstDate()) || dbact.getFirstDate().length() != 8) {
                            record.ACCEPT_DATE = new SimpleDateFormat("yyyy-mm-dd").format(new Date());
                        } else {
                            record.ACCEPT_DATE = toDate10(dbact.getFirstDate());
                        }
                    }
                }
                records.add(record);
            }
            return records;
        } finally {
            if (session != null) session.close();
        }
    }

    private String toDate10(String date8) {
        return date8.substring(0, 4) + "-" + date8.substring(4, 6) + "-" + date8.substring(6);
    }
}
