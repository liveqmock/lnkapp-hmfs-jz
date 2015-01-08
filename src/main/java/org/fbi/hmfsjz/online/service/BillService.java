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
 * 缴款单处理
 */
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    MybatisManager manager = new MybatisManager();

    // 按单号查询缴款单
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

    // 保存缴款单，已存在则更新
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
                    // 已存在则更新
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
            throw new RuntimeException("状态：" + bill.getBillStsCode() + bill.getBillStsTitle());
        }
    }
}
