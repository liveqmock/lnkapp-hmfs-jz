package org.fbi.hmfsjz.internal;

import org.fbi.hmfsjz.helper.ProjectConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class InterestTimer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static long PERIOD_DAY = 24 * 60 * 60 * 1000;

    @Override
    public void run() {


        Timer timer = new Timer();
        int hour = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.time.hour"));
        int minute = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.time.minute"));
        logger.info("HMFSJZ ��ʱ��Ϣ������...ʱ�䣺 " + hour + ":" + minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        // ��һ��ִ�ж�ʱ�����ʱ��
        // �����һ��ִ�ж�ʱ�����ʱ�� С�ڵ�ǰ��ʱ��
        //��ʱҪ�� ��һ��ִ�ж�ʱ�����ʱ���һ�죬�Ա���������¸�ʱ���ִ�С��������һ�죬���������ִ�С�
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        //����ָ����������ָ����ʱ�俪ʼ�����ظ��Ĺ̶��ӳ�ִ�С�
        timer.schedule(new InterestTimerTask(), date, PERIOD_DAY);
    }


    // ����һ��
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

}
