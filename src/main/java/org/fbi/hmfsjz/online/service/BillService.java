package org.fbi.hmfsjz.online.service;

import org.fbi.hmfsjz.enums.BillQryStatus;
import org.fbi.hmfsjz.helper.ObjectFieldsCopier;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.HmfsJzBillMapper;
import org.fbi.hmfsjz.repository.model.HmfsJzBill;
import org.fbi.hmfsjz.repository.model.HmfsJzBillExample;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * �ɿ����
 */
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ�ɿ
    public HmfsJzBill qryBillByNo(String billNo) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzBillMapper mapper = session.getMapper(HmfsJzBillMapper.class);
            HmfsJzBillExample example = new HmfsJzBillExample();
            example.createCriteria().andBillnoEqualTo(billNo);
            List<HmfsJzBill> bills = mapper.selectByExample(example);
            return (bills.size() > 0) ? bills.get(0) : null;
        } finally {
            if (session != null) session.close();
        }
    }

    // ����ɿ���Ѵ��������
    public boolean saveDepositBill(HmfsJzBill bill) throws IllegalAccessException {
        if (BillQryStatus.VALAID.getCode().equals(bill.getBillStsCode()))  {
            SqlSession session = null;
            try {
                session = manager.getSessionFactory().openSession();
                HmfsJzBillMapper mapper = session.getMapper(HmfsJzBillMapper.class);
                HmfsJzBillExample example = new HmfsJzBillExample();
                example.createCriteria().andBillnoEqualTo(bill.getBillno());
                List<HmfsJzBill> bills = mapper.selectByExample(example);
                int cnt = 0;
                if (bills.size() > 0) {
                    // �Ѵ��������
                    HmfsJzBill origBill = bills.get(0);
                    String pkid = origBill.getPkid();
                    ObjectFieldsCopier.copyFields(bill, origBill);
                    origBill.setPkid(pkid);
                    cnt = mapper.updateByPrimaryKey(origBill);
                    session.commit();
                    return cnt == 1;
                } else {
                    cnt = mapper.insert(bill);
                    session.commit();
                    return cnt == 1;
                }
            } finally {
                if (session != null) session.close();
            }
        } else {
            throw new RuntimeException("״̬��" + bill.getBillStsCode() + bill.getBillStsTitle());
        }
    }
}
