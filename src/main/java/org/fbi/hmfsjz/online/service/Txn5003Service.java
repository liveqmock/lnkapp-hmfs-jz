package org.fbi.hmfsjz.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.ActStatus;
import org.fbi.hmfsjz.gateway.domain.txn.Toa5003;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzActMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzAct;
import org.fbi.hmfsjz.repository.model.HmfsJzActExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * ���ܷ�������
 */
public class Txn5003Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn5003Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public Toa5003.Body cancelAct(String houseID, String houseAccount) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzActMapper mapper = session.getMapper(HmfsJzActMapper.class);
            HmfsJzActExample example = new HmfsJzActExample();

            example.createCriteria().andHouseAccountEqualTo(houseAccount).andHouseIdEqualTo(houseID);
            List<HmfsJzAct> dbActs = mapper.selectByExample(example);
            Toa5003.Body result = new Toa5003.Body();
            if (dbActs.isEmpty()) {
                result.STS_CODE = "0002";            // ������
                result.STS_MSG = "���˻�������";
            } else {
                HmfsJzAct dbact = dbActs.get(0);
                if (ActStatus.CANCEL.getCode().equals(dbact.getActStatus())) {
                    result.STS_CODE = "0000";            // ������
                    result.STS_MSG = "�����ɹ�";
                } else {
                    if (dbact.getBalAmt().compareTo(new BigDecimal("0.00")) != 0) {
                        result.STS_CODE = "0001";            // ������
                        result.STS_MSG = "���˻��ʽ�������0";
                    } else {
                        // ����
                        result.STS_CODE = "0000";
                        result.STS_MSG = "�����ɹ�";
                        dbact.setActStatus(ActStatus.CANCEL.getCode());
                        mapper.updateByPrimaryKey(dbact);
                    }
                }
            }
            session.commit();
            return result;
        } finally {
            if (session != null) session.close();
        }
    }

}
