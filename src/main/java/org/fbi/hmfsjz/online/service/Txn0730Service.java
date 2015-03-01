package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.*;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzVoucherMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzVoucher;
import org.fbi.hmfsjz.repository.model.HmfsJzVoucherExample;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500730 Ʊ������
 */
public class Txn0730Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn0730Service.class);
    MybatisManager manager = new MybatisManager();

    public boolean process(String branchID, String tellerID, long startNo, long endNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzVoucherMapper vchMapper = session.getMapper(HmfsJzVoucherMapper.class);
            HmfsJzVoucherExample example = new HmfsJzVoucherExample();
            long cnt = 0;
            for (long i = startNo; i <= endNo; i++) {
                example.clear();
                example.createCriteria().andVchNumEqualTo(String.valueOf(i));
                if (vchMapper.countByExample(example) > 0) {
                    throw new RuntimeException("�����ظ�������Ʊ�ݺ�" + i);
                }
                HmfsJzVoucher record = new HmfsJzVoucher();
                record.setVchSts(VoucherStatus.CHECK.getCode());
                record.setVchNum(String.valueOf(i));
                record.setTxnDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                record.setTxnTime(new SimpleDateFormat("HHmmss").format(new Date()));
                record.setOperId(tellerID);
                record.setDeptId(branchID);
                record.setSendFlag(SendFlag.UNSEND.getCode());
                cnt += vchMapper.insert(record);
            }
            if (cnt == (endNo - startNo + 1)) {
                session.commit();
                return true;
            } else {
                throw new RuntimeException("Ʊ��δ��ȫ����");
            }
        } catch (Exception e) {
            session.rollback();
            logger.error("����Ʊ�ݱ���ʧ��", e);
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("����Ʊ�ݱ���ʧ��");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }

}
