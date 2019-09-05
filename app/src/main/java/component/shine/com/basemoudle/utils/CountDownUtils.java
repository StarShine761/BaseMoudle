package component.shine.com.basemoudle.utils;

import android.os.CountDownTimer;
import android.util.Log;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cc
 * On 2019/8/29.
 * 倒计时时间工具类
 */
public class CountDownUtils {
    private static String TAG = "CountDownUtils";

    public static CountDownTimer cdTimer;

    public static CountDownTimer fiveTimer;

    public static long delayTime = 2;
    public static ScheduledThreadPoolExecutor executor;


    public static void startCount(long minutes) {

        cdTimer = new CountDownTimer(minutes * 1000 * 60 + 400, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {

                long remainTime = millisUntilFinished / 1000 / 60;
                Log.e(TAG, "还剩" + remainTime + "分钟");

            }

            @Override
            public void onFinish() {
                if (cdTimer != null) {
                    cdTimer.cancel();

                }
                executor = new ScheduledThreadPoolExecutor(1);
                executor.scheduleWithFixedDelay(new TimeThread(), 0, 60, TimeUnit.SECONDS);

                Log.e(TAG, "结束倒计时，开始计时逾时时间");
            }
        }.start();

    }


    public static void startCountFiveMinute() {

        fiveTimer = new CountDownTimer(delayTime * 1000 * 60 + 400, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (fiveTimer != null) {
                    fiveTimer.cancel();

                }

            }
        }.start();

    }


    /**
     * 返回两个距离某个值得时间差
     *
     * @param endTimeendTime
     */
    public static String timeDifference(String endTimeendTime) {
        Log.e(TAG, "时间是" + endTimeendTime);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = new Date();
            Date d2 = df.parse(endTimeendTime);
            long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long trueminutes = diff / (1000 * 60);
            startCount(trueminutes);
            Log.e(TAG, "" + days + "天" + hours + "小时" + minutes + "分" + "------" + trueminutes + "分钟数");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";


    }


    /**
     * 逾时时间
     *
     * @param starttime
     */
    public static long nowDiffrent(String starttime) {
//        Log.e(TAG, "时间是" + starttime);
        long trueminutes = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date d1 = new Date();
            Date d2 = df.parse(starttime);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            trueminutes = diff/ (1000 * 60);
            Log.e(TAG, "逾时------" + trueminutes + "分钟数");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trueminutes;


    }


    /**
     * @param day 指定时间  2009-03-23 08:00:00
     * @param x   1
     * @return
     */
    public static String addDate(String day, int x) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, x);//24小时制
        cal.add(Calendar.MINUTE, 5);//5分钟测试用
        date = cal.getTime();
//        Log.e(TAG,date+"");
        System.out.println("front:" + date);
        cal = null;
        return format.format(date);
    }


    public static String getHourMinute(long minuute) {

        long hours = (int) Math.floor(minuute / 60);
        long minute = minuute % 60;
        return hours + "," + minute;
    }


    /**
     * 60s获取一次时间
     */
    public static class TimeThread implements Runnable {

        @Override
        public void run() {
//            long nowDiffrent = nowDiffrent(Constant.endTime);
//            Log.e(TAG, "逾时时间---" + nowDiffrent);
//            EventBus.getDefault().postSticky(new TimeEvent(Constant.TIME_RED, getHourMinute(nowDiffrent)));

        }
    }


    public static void main(String[] args) throws ParseException {
//
//        long time = 45;
//        long hours = (int) Math.floor(time / 60);
//        long minute = time % 60;
//        System.out.println(hours + "小时" + minute + "分钟");


//        addDate("2019-09-04 14:12:01",1);
        getHourMinute(nowDiffrent("2019-09-05 14:10:01"));
        getHourMinute(nowDiffrent("2019-09-05 14:10:33"));
        getHourMinute(nowDiffrent("2019-09-05 14:10:55"));
    }
}
