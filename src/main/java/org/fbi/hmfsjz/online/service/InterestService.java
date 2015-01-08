package org.fbi.hmfsjz.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjz.enums.ActStatus;
import org.fbi.hmfsjz.enums.BillBookType;
import org.fbi.hmfsjz.enums.SendFlag;
import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.client.SyncSocketClient;
import org.fbi.hmfsjz.gateway.domain.txn.Tia5002;
import org.fbi.hmfsjz.gateway.domain.txn.Toa5002;
import org.fbi.hmfsjz.helper.ObjectFieldsCopier;
import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.fbi.hmfsjz.repository.MybatisManager;
import org.fbi.hmfsjz.repository.dao.CommonMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzActMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzActTxnMapper;
import org.fbi.hmfsjz.repository.dao.HmfsJzInterestMapper;
import org.fbi.hmfsjz.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * ��Ϣ
 */
public class InterestService {

    private static final Logger logger = LoggerFactory.getLogger(InterestService.class);
    MybatisManager manager = new MybatisManager();
    private ActirtService actirtService = new ActirtService();

    // ��Ϣ
    public void accrualInterest() throws IllegalAccessException {
        String sdf = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        logger.info("��ʼ��Ϣʱ��:" + sdf);
        String today = sdf.substring(0, 8);
        String time = sdf.substring(8);
        SqlSession session = null;
        BigDecimal curRate = actirtService.qryCurrentRate();       // ����������
        BigDecimal fixedRate = actirtService.qry3MonthRate();      // ����3����������
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJzActTxnMapper actTxnMapper = session.getMapper(HmfsJzActTxnMapper.class);
            HmfsJzActMapper actMapper = session.getMapper(HmfsJzActMapper.class);
            HmfsJzActExample example = new HmfsJzActExample();
            logger.info(today.substring(4));
            example.createCriteria().andIntDateLike("%" + today.substring(4))
                    .andActStatusEqualTo(ActStatus.VALID.getCode())
                    .andBalAmtGreaterThan(new BigDecimal("0.00"));
            List<HmfsJzAct> acts = actMapper.selectByExample(example);
            logger.info("��Ϣ�˻�����" + acts.size());
            for (HmfsJzAct act : acts) {
                logger.info("��Ϣ�˻���" + act.getHouseAccount());
                // ��Ϣ
                // �ϴδ���պͼ�Ϣ�յĲ�С��һ�꣬��ȡ��������
                int days = actirtService.daysBetween(act.getLastDepDate(), today);
                if (days == 0) {
                    continue;     // �����Ϣ��������һ�˻�
                }
                // ��Ϣ������ˮ
                CommonMapper cmnMapper = session.getMapper(CommonMapper.class);
                String maxSerialNo = cmnMapper.qryMaxSerialNo(act.getHouseAccount());
                String interestSerialNo = getInterestNo(maxSerialNo);
                HmfsJzActTxn inttxn = new HmfsJzActTxn();
                // ��Ϣ�ύ
                HmfsJzInterest interest = new HmfsJzInterest();

                ObjectFieldsCopier.copyFields(act, inttxn);
                inttxn.setPkid(UUID.randomUUID().toString());
                inttxn.setActSerialNo(interestSerialNo);
                inttxn.setTxnCode("5002");
                inttxn.setOperDate(today);
                inttxn.setOperTime(time);
                boolean isAfterOneYear = actirtService.isAfterOneYear(act.getLastDepDate(), today);
                BigDecimal interAmt = null;
                if (isAfterOneYear) { // ����
                    interAmt = act.getBalAmt().multiply(fixedRate);
                    interest.setRate(fixedRate.toString());
                    inttxn.setBookType(BillBookType.INTEREST_SCHE_FIXED.getCode());
                } else {
                    interAmt = act.getBalAmt().multiply(curRate);
                    interest.setRate(curRate.toString());
                    inttxn.setBookType(BillBookType.INTEREST_SCHE_CURRENT.getCode());
                }
                inttxn.setTxnAmt(interAmt);
                actTxnMapper.insert(inttxn);

                interest.setPkid(UUID.randomUUID().toString());
                interest.setInterest(interAmt);
                interest.setInterestNo("JX" + act.getHouseAccount() + interestSerialNo);
                interest.setHouseId(act.getHouseId());
                interest.setHouseAccount(act.getHouseAccount());
                interest.setHouseLocation(act.getHouseLocation());
                interest.setOwner(act.getOwner());
                interest.setBeforeAmt(act.getBalAmt());     // ��Ϣǰ�ʽ�ָ��Ϣǰ���
                BigDecimal nowBal = act.getBalAmt().add(interAmt);
                interest.setAfterAmt(nowBal);               // ��Ϣ���ʽ�ָ��Ϣ�����
                interest.setBeginDate(toDate10(act.getIntDate()));    // ��Ϣ��
                interest.setEndDate(toDate10(today));  // ������
                interest.setCapital(act.getBalAmt());
                interest.setOperTime(time);
                interest.setOperId("9999");
                interest.setSerialNo(interestSerialNo);
                interest.setTxnCode("5002");
                interest.setIntName("��Ƚ�Ϣ");
                interest.setDeptId("010");
                interest.setSendFlag(SendFlag.UNSEND.getCode());
                HmfsJzInterestMapper interestMapper = session.getMapper(HmfsJzInterestMapper.class);
                interestMapper.insert(interest);
                // �����˻�
                act.setBalAmt(act.getBalAmt().add(interAmt));
                act.setIntAmt(act.getIntAmt().add(interAmt));
                act.setIntDate(today);
                actMapper.updateByPrimaryKey(act);
            }
            session.commit();
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    // ���ͼ�Ϣ��ϸ(��ʱÿ�췢��δ���ͳɹ��ļ�Ϣ��¼)
    public void sendInterestTxns() {
        SqlSession session = null;
        int pkgCnt = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.pkg.details"));
        session = manager.getSessionFactory().openSession();
        HmfsJzInterestMapper interestMapper = session.getMapper(HmfsJzInterestMapper.class);
        HmfsJzInterestExample example = new HmfsJzInterestExample();
        example.createCriteria().andSendFlagEqualTo(SendFlag.UNSEND.getCode());
        List<HmfsJzInterest> interests = interestMapper.selectByExample(example);
        if (interests.isEmpty()) {
            logger.info("���ͱ�����0");
            session.close();
            return;
        }
        if (pkgCnt >= interests.size()) {
            sendInterestList(interests);
        } else {
            int num = interests.size() / pkgCnt + 1;
            if (interests.size() % pkgCnt == 0) num--;
            List<HmfsJzInterest> list = null;
            for (int i = 0; i < num; i++) {
                if (i == (num - 1)) {
                    list = interests.subList(pkgCnt * i, interests.size());
                } else {
                    list = interests.subList(pkgCnt * i, pkgCnt * (i + 1));
                }
                sendInterestList(list);
            }
        }
        session.close();
    }

    private void sendInterestList(List<HmfsJzInterest> list) {
        SqlSession session = null;
        String sendtime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
        try {
            Tia5002 tia = new Tia5002();
            tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());

            for (HmfsJzInterest record : list) {
                Tia5002.Detail detail = new Tia5002.Detail();
                detail.HOUSE_ACCOUNT = record.getHouseAccount();
                detail.INTEREST_NO = record.getInterestNo();
                detail.INTEREST_NAME = record.getIntName();
                detail.HOUSE_ID = record.getHouseId();
                detail.HOUSE_LOCATION = record.getHouseLocation();
                detail.OWNER = record.getOwner();
                detail.BEGIN_DATE = record.getBeginDate();
                detail.END_DATE = record.getEndDate();
                detail.BEFORE_AMT = record.getBeforeAmt().toString();
                detail.AFTER_AMT = record.getAfterAmt().toString();
                detail.CAPITAL = record.getCapital().toString();
                detail.RATE = record.getRate().toString();
                detail.INTEREST = record.getInterest().toString();
                tia.BODY.DETAILS.add(detail);
            }

            Toa5002 toa = (Toa5002) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("�����쳣");
            if (TxnRtnCode.TXN_SECCESS.getCode().equals(toa.BODY.STS_CODE)) {

                session = manager.getSessionFactory().openSession();
                HmfsJzInterestMapper interestMapper = session.getMapper(HmfsJzInterestMapper.class);
                for (HmfsJzInterest record : list) {
                    record.setSendDatetime(sendtime);
                    record.setSendFlag(SendFlag.SENT.getCode());
                    interestMapper.updateByPrimaryKey(record);
                }
                session.commit();
            } else {
                logger.info("���ͼ�Ϣ���ؽ����" + toa.BODY.STS_CODE);
            }
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("��Ϣ�����쳣", e);
        } finally {
            if (session != null) session.close();
        }
    }

    private String toDate10(String date8) {
        return date8.substring(0, 4) + "-" + date8.substring(4, 6) + "-" + date8.substring(6, 8);
    }

    private String getInterestNo(String maxNo) {
        if (StringUtils.isEmpty(maxNo)) return "000001";
        else {
            int num = Integer.parseInt(maxNo);
            num++;
            return StringUtils.leftPad(String.valueOf(num), 6, "0");
        }
    }
}


