
package com.ldh.androidlib.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    /**
     * 根据传人的时间戳,返回绝对时间差字符串
     *
     * @param timestamp   单位毫秒
     * @param currentTime 单位毫秒
     * @return 格式化的字符串
     */
    public static String getDiffTime(Long timestamp, Long currentTime) {
        String ret = "";
        Long sub = Math.abs(timestamp - currentTime) / 1000L;
        if (sub < 60) {
            ret = sub + "秒前";
            return ret;
        }
        if (sub < 3600) {
            ret = sub / 60 + "分钟前";
            return ret;
        }
        if (sub < 86400) {
            ret = sub / 3600 + "小时前";
            return ret;
        }
        ret = sub / 86400 + "天前";

        return ret;
    }

    public static Calendar string2Calendar(String arg, String format) {
        SimpleDateFormat sdf = null;
        String trimString = arg.trim();

        if (format != null) {
            sdf = new SimpleDateFormat(format);
        } else {
            if (trimString.length() > 14)
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            else
                sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        Date d = null;
        try {
            d = sdf.parse(trimString);
        } catch (ParseException e) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    public static Calendar string2Calendar(String arg) {
        return string2Calendar(arg, null);
    }

    /**
     * @param dateValue 单位毫秒
     * @return 日期时间格式: "yyyy-MM-dd HH:mm:ss.SSS"
     */
    public static String formatTime(Long dateValue) {
        return formatTime(dateValue, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * @param dateValue 单位毫秒
     * @return 日期时间格式: "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatTime_ymdHms(Long dateValue) {
        return formatTime(dateValue, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param dateValue 单位毫秒
     * @param format
     * @return
     */
    public static String formatTime(Long dateValue, String format) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTimeInMillis(dateValue);
        return formatTime(canlendar.getTime(), format);
    }

    public static String formatTime(Date dateValue, String format) {
        DateFormat dateformat = new SimpleDateFormat(format);
        String date = dateformat.format(dateValue);
        return date;
    }

}
