package org.fbi.hmfsjz.internal;

import org.fbi.hmfsjz.online.service.InterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * Created by lenovo on 2014-10-17.
 */
public class InterestTimerTask extends TimerTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // ��ʱ��Ϣ
    public void run() {
         try {
             InterestService interestService = new InterestService();
             // ��Ϣ
             interestService.accrualInterest();
             // ���ͼ�Ϣ��ϸ
             interestService.sendInterestTxns();
         } catch (Exception e) {
             logger.error("ҵ���쳣", e);
             return;
         }

    }
}
