package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzDrawMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzDraw;
import org.fbi.hmfsjz.repository.model.HmfsJzDrawExample;

import java.util.List;

/**
 * 支取单处理
 */
public class DrawService {
    MybatisManager manager = new MybatisManager();

    // 按单号查询支取单
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

    // 保存缴款单，已存在则更新
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
                throw new RuntimeException("支取单保存失败");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}
