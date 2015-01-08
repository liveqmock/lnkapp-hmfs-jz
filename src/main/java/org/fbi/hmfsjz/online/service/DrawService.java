package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzDrawMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzDraw;
import org.fbi.hmfsjz.repository.model.HmfsJzDrawExample;

import java.util.List;

/**
 * ֧ȡ������
 */
public class DrawService {
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ֧ȡ��
    public HmfsJzDraw qryDrawByNo(String drawNo) {
        SqlSession session = null;

        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzDrawMapper mapper = session.getMapper(HmfsJzDrawMapper.class);
            HmfsJzDrawExample example = new HmfsJzDrawExample();
            example.createCriteria().andBillnoEqualTo(drawNo);
            List<HmfsJzDraw> draws = mapper.selectByExample(example);
            return (draws.size() > 0) ? draws.get(0) : null;
        } finally {
            if (session != null) session.close();
        }
    }

    // ����ɿ���Ѵ��������
    public void saveDrawBills(List<HmfsJzDraw> draws, String billNo) throws IllegalAccessException {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzDrawMapper mapper = session.getMapper(HmfsJzDrawMapper.class);
            HmfsJzDrawExample example = new HmfsJzDrawExample();
            example.createCriteria().andBillnoEqualTo(billNo);
            mapper.deleteByExample(example);
            for (HmfsJzDraw record : draws) {
                mapper.insert(record);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("֧ȡ������ʧ��");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}
