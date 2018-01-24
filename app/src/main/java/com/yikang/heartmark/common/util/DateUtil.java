package com.yikang.heartmark.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shangxzheng on 2014/9/26.
 */
public class DateUtil {

    //Convert UTC Date to local Date
    @Deprecated
    public static Date utc2LocalDate(String utcDateTimeString) {
        String year = utcDateTimeString.substring(0, 4);
        String month = utcDateTimeString.substring(5, 7);
        String day = utcDateTimeString.substring(8, 10);
        String hour = utcDateTimeString.substring(11, 13);
        String minute = utcDateTimeString.substring(14, 16);
        String second = utcDateTimeString.substring(17, 19);
        Calendar calendar = new GregorianCalendar(Integer.valueOf(year),
                Integer.valueOf(month) - 1, Integer.valueOf(day),
                Integer.valueOf(hour), Integer.valueOf(minute),
                Integer.valueOf(second));
        calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        return new Date(calendar.getTimeInMillis());
    }

    @Deprecated
    public static Date utc2LocalDate(long utcTimeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        return utc2LocalDate(dateFormat.format(new Date(utcTimeStamp)));
    }

    public static String getUTCTimeString() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utcDateTime = new Date(cal.getTimeInMillis());

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        SimpleDateFormat timeFormat = new SimpleDateFormat();//new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(utcDateTime);// + "T" + timeFormat.format(utcDateTime) + "Z";
    }

    public static String formatUTCDate(Date utcDate, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        return null;
    }

    //input date is in UTC, so display it in local datetime value,
    public static String formatUTCDate(Date utcDate) {
        if (Locale.getDefault().equals(Locale.CHINA)) {
            return formatChatDateTime(utcDate);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            TimeZone tz = TimeZone.getDefault();
            int offset = tz.getRawOffset();
            long dateVal = utcDate.getTime();
            Date localDate = new Date(dateVal + offset);
            String localDtString = dateFormat.format(localDate);
            return localDtString;  //14-10-22 下午3:53
        }
    }

    public static boolean isCloseEnough(Date currentItemTime, Date lastItemTime) {

        long between = (currentItemTime.getTime() - lastItemTime.getTime());// 得到两者的毫秒数

        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        return min == 0;
    }

    public static String simpleFormatDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String formatyyyyMMddHHMMss(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    /**
     * 格式化时间
     */
    public static String formatChatDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();
        long dateVal = date.getTime();
        Date localDate = new Date(dateVal + offset);
        String time = format.format(localDate);

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            int t2 = Integer.valueOf(time.split(" ")[1].split(":")[0]);
            return (6 < t2 && t2 < 12 ? "上午 " : "下午 ") + t1;
        } else if (current.before(today) && current.after(yesterday)) {
            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            int t2 = Integer.valueOf(time.split(" ")[1].split(":")[0]);
            return "昨天" + (6 < t2 && t2 < 12 ? "上午 " : "下午 ") + t1;
        } else {
//            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            String newDate = time.split(" ")[0];
            int index = newDate.indexOf("-") + 1;
            return time.substring(index, newDate.length());
        }
    }


    public static String formatTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        TimeZone tz = TimeZone.getDefault();
//        int offset = tz.getRawOffset();
//        long dateVal = date.getTime();
//        Date localDate = new Date(dateVal + offset);
        String time = format.format(date);

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            int t2 = Integer.valueOf(time.split(" ")[1].split(":")[0]);
            return (6 < t2 && t2 < 12 ? "上午 " : "下午 ") + t1;
        } else if (current.before(today) && current.after(yesterday)) {
            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            int t2 = Integer.valueOf(time.split(" ")[1].split(":")[0]);
            return "昨天" + (6 < t2 && t2 < 12 ? "上午 " : "下午 ") + t1;
        } else {
//            String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
            String newDate = time.split(" ")[0];
            int index = newDate.indexOf("-") + 1;
            return time.substring(index, newDate.length());
        }
    }


    public static String formatChatSessionDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();
        long dateVal = date.getTime();
        Date localDate = new Date(dateVal + offset);
        String time = format.format(localDate);
        return time;
    }

    public static String formatNoteDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();
        long dateVal = date.getTime();
        Date localDate = new Date(dateVal + offset);
        String time = format.format(localDate);

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);


        current.setTime(date);

        if (current.after(today)) {
            SimpleDateFormat NoteFormat = new SimpleDateFormat("今天  HH:mm");
            return NoteFormat.format(date);
        } else {
            SimpleDateFormat norFormat = new SimpleDateFormat("MM月dd日  HH:mm");
            return norFormat.format(date);
        }
    }


    public static String formatNoticeDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("发布于yyyy.MM.dd HH:mm");
        return format.format(date);
    }

    public static String getLoacalDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    public static String formatRecommedationDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String time = format.format(date);
        String t1 = time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
        int t2 = Integer.valueOf(time.split(" ")[1].split(":")[0]);
        return time + (6 < t2 && t2 < 12 ? "AM " : "PM ");
    }

    public static String formatNoticeTime(Date date) {

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);


        current.setTime(date);

        if (current.after(today)) {
            SimpleDateFormat NoteFormat = new SimpleDateFormat("今天  HH:mm");
            return NoteFormat.format(date);
        } else {
            SimpleDateFormat norFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            return norFormat.format(date);
        }

    }


    public static String toStringTime(long l) {
        long h = l / 3600 / 1000;
        long m = l % (3600 * 1000) / 60000;
        long s = l % (3600 * 1000) % 60000 / 1000;
        float ms = l % (3600 * 1000) % 60000 % 1000;//不足一秒
        float mss = (float) s + ms / 1000f;

        String fs = ""; //String.format("%02d:%02d:%02d", h, m, mss);

        if (h > 0) {
            fs = fs + String.valueOf(h) + "小时";
        }
        if (m > 0) {
            fs = fs + String.valueOf(m) + "'";
        }
        if (s > 0 || mss > 0) {
            fs = fs + String.valueOf(Math.round(mss)) + "''";
        }
        return fs;
    }


    public static Timestamp getCurrentTime() {
        TimeZone timeZone = TimeZone.getDefault();
        int offset = timeZone.getRawOffset();
        Timestamp time = new Timestamp(System.currentTimeMillis() - offset);
        return time;
    }

    private static SimpleDateFormat LocalTimeFormatter = new SimpleDateFormat();

    //GMT Time
    private static SimpleDateFormat GMTTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    {
        GMTTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static long getLocalTime(String strLocalTime) {
        long millisecond = 0;
        try {
            LocalTimeFormatter.setTimeZone(TimeZone.getDefault());
            millisecond = LocalTimeFormatter.parse(strLocalTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return millisecond;
    }

    public static String getLocalTimeString(String strGMTTime) {
        String strLocalTime = null;
        try {
            GMTTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = GMTTimeFormatter.parse(strGMTTime);
            LocalTimeFormatter.setTimeZone(TimeZone.getDefault());
            strLocalTime = LocalTimeFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strLocalTime;
    }

    public static String getLocalTimeString(long millisecond) {
        LocalTimeFormatter.setTimeZone(TimeZone.getDefault());
        String strLocalTime = LocalTimeFormatter.format(millisecond);
        return strLocalTime;
    }

}
