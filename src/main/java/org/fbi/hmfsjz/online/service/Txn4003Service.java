package org.fbi.hmfsjz.online.service;

import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.gateway.domain.txn.Toa4003;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.CommonMapper;
import org.fbi.hmfsjz.repository.model.HmTotalAct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * ���˻�����Ϣ��ѯ
 */
public class Txn4003Service {
    private static final Logger logger = LoggerFactory.getLogger(Txn4001Service.class);
    MybatisManager manager = new MybatisManager();

    //
    public Toa4003.Body qryTotalInfo() {
        SqlSession session = null;
        Toa4003.Body info = new Toa4003.Body();
        info.STS_CODE = "0000";
        info.STS_MSG = "�������";
        try {
            session = manager.getSessionFactory().openSession();
            CommonMapper mapper = session.getMapper(CommonMapper.class);
            HmTotalAct totalInfo = mapper.qryTotalActInfo();
            info.AREA_SUM = totalInfo.getSumArea();
            info.HOUSE_SUM = totalInfo.getHcnt();
            info.MONEY_SUM = totalInfo.getSumAmt().toString();

            BigDecimal depositAmt = mapper.qrySumAmtByType(BillBookType.DEPOSIT.getCode());
            BigDecimal refundAmt = mapper.qrySumAmtByType(BillBookType.REFUND.getCode());
            BigDecimal drawAmt = mapper.qrySumAmtByType(BillBookType.DRAW.getCode());
            BigDecimal intAmt = mapper.qrySumAmtByType(BillBookType.INTEREST_DRAW_CURRENT.getCode()
                    + "','" + BillBookType.INTEREST_SCHE_CURRENT.getCode()
                    + "','" + BillBookType.INTEREST_SCHE_FIXED.getCode());

            info.DRAW_SUM = (drawAmt == null ? "0.00" : drawAmt.toString());
            info.INTEREST_SUM = (intAmt == null ? "0.00" : intAmt.toString());
            info.PAY_SUM = depositAmt.subtract(refundAmt).toString();

        } catch (Exception e) {
            logger.error("�ֻ���Ϣ��ѯ���ݿ��쳣", e);
            info.STS_CODE = "0003";
            info.STS_MSG = "ϵͳ�쳣";
        } finally {
            if (session != null) session.close();
        }
        return info;
    }
}
