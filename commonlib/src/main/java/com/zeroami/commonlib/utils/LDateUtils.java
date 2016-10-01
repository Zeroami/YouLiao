package com.zeroami.commonlib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：日期工具类</p>
 */
public class LDateUtils {
    /**
     * 日期类型 *
     */
    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss";
    public static final String DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss";


    private LDateUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将Date类型转换为日期字符串
     *
     * @param date Date对象
     * @param type 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期字符串转换为Date类型
     *
     * @param dateStr 日期字符串
     * @param type    日期字符串格式
     * @return Date对象
     */
    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 得到年
     *
     * @param date Date对象
     * @return 年
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到月
     *
     * @param date Date对象
     * @return 月
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    /**
     * 得到日
     *
     * @param date Date对象
     * @return 日
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将时间转为今天, 昨天, 前天, XXXX-XX-XX, ...
     *
     * @param timeMillis 时间
     * @return 当前日期转换为更容易理解的方式
     */
    public static String formatTimeToHumanDayString(long timeMillis) {
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long todayStartTime = today.getTimeInMillis();

        if (timeMillis >= todayStartTime && timeMillis < todayStartTime + oneDay) { // today
            return "今天";
        } else if (timeMillis >= todayStartTime - oneDay && timeMillis < todayStartTime) { // yesterday
            return "昨天";
        } else if (timeMillis >= todayStartTime - oneDay * 2 && timeMillis < todayStartTime - oneDay) { // the day before yesterday
            return "前天";
        } else if (timeMillis > todayStartTime + oneDay) { // future
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(timeMillis);
            return dateFormat.format(date);
        }
    }

    /**
     * 将时间转为1分钟前，2分钟前，今天 XX:XX ,昨天 XX:XX...
     * @param timeMillis
     * @return
     */
    public static String formatTimeToHumanTimeString(long timeMillis) {
        long curTime = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000;

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long todayStartTime = today.getTimeInMillis();
        if (timeMillis >= todayStartTime) {
            long d = (curTime - timeMillis) / 1000;
            if (d <= 60) {
                return "1分钟前";
            } else if (d <= 60 * 60) {
                long m = d / 60;
                if (m <= 0) {
                    m = 1;
                }
                return m + "分钟前";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
                Date date = new Date(timeMillis);
                String dateStr = dateFormat.format(date);
                return dateStr;
            }
        } else {
            if (timeMillis < todayStartTime && timeMillis > todayStartTime - oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm");
                Date date = new Date(timeMillis);
                String dateStr = dateFormat.format(date);
                return dateStr;
            } else if (timeMillis < todayStartTime - oneDay && timeMillis > todayStartTime - 2 * oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("前天 HH:mm");
                Date date = new Date(timeMillis);
                String dateStr = dateFormat.format(date);
                return dateStr;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(timeMillis);
                String dateStr = dateFormat.format(date);
                return dateStr;
            }
        }
    }
}
