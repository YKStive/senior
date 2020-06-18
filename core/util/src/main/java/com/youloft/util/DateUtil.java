package com.youloft.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 日期时间工具类
 *
 * @author xll
 * @date 2018/7/25 18:27
 */
public class DateUtil {
    public static final int EPOCH_JULIAN_DAY = 0;
    static final long EPOCH_JU_MS = 2208988800000L;
    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;

    /**
     * 获取间隔月
     *
     * @param begin
     * @param end
     * @return
     */
    public static int getMonths(Calendar begin, Calendar end) {
        int detalYear = end.get(Calendar.YEAR) - begin.get(Calendar.YEAR) - 1;
        int fmonths = 11 - begin.get(Calendar.MONTH);
        int emonths = end.get(Calendar.MONTH) + 1;
        return detalYear * 12 + fmonths + emonths;
    }


    /**
     * 获取下一个整点时
     *
     * @param ms
     * @return
     */
    public static long getNextHour(long ms) {
        Calendar calendar = getCalendar(ms);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取到下一个整点时的时差
     *
     * @param ms
     * @return
     */
    public static int getNextHourInterval(long ms) {
        return (int) (getNextHour(ms) - ms);
    }

    /**
     * 获取当前时间到第二天午夜12点的时间间隔
     *
     * @return ms
     */
    public static long getMidnightInterval() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    /**
     * 根据毫秒获取Calendar对象
     *
     * @param ms
     * @return
     */
    public static Calendar getCalendar(long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar;
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.CHINA);

    /**
     * 转换DateTime
     *
     * @param date
     * @return
     */
    public static long getHttpTime(String date) {
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 判断两个时间是否为同一天
     *
     * @param time
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time, long time2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time2);
        return sameDay(calendar, calendar1);

    }

    /**
     * 同天
     *
     * @param calendar
     * @return
     */
    public static boolean sameDay(Calendar calendar, Calendar calendar1) {
        return sameMonth(calendar, calendar1) && calendar.get(Calendar.DATE) == calendar1.get(Calendar.DATE);
    }

    /**
     * 同年
     *
     * @param calendar
     * @return
     */
    public static boolean sameYear(Calendar calendar, Calendar calendar1) {
        return calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR);
    }

    /**
     * 是否为同月
     *
     * @param calendar
     * @return
     */
    public static boolean sameMonth(Calendar calendar, Calendar calendar1) {
        return sameYear(calendar, calendar1) && calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH);
    }

    /**
     * 获取到1901年到现在有多少月
     *
     * @param date
     * @return
     */
    public static final int getMonthSince1901(Calendar date) {
        int year = date.get(Calendar.YEAR);
        int diffYear = year - 1900;
        return diffYear * 12 + date.get(Calendar.MONTH);
    }

    /**
     * 获取
     *
     * @param month
     * @return
     */
    public static final Calendar getCalendarByMonthSince1901(int month) {
        int yearOffset = month / 12;
        int monthOffset = month % 12;
        Calendar cal = Calendar.getInstance();
        cal.set(1900 + yearOffset, monthOffset, 1);
        return cal;
    }

    /**
     * 获取相差多少天
     *
     * @param cal
     * @param cal1
     * @return
     */
    public static final int getDayOffset(Calendar cal, Calendar cal1) {
        return getJuliDay(cal) - getJuliDay(cal1);
    }

    /**
     * 获取X周
     *
     * @param weeks
     * @param firstDayOfWeek
     * @return
     */
    public static final Calendar getFirstDayOfJuliWeek(int weeks, int firstDayOfWeek) {
        int diff = firstDayOfWeek - Calendar.MONDAY;
        int julianDay = weeks * 7 + EPOCH_JULIAN_DAY + diff;
        return getCalendarByJuliDay(julianDay);
    }

    /**
     * 获取当前有多少周
     *
     * @param calendar
     * @param firstDayOfWeek
     * @return
     */
    public static int getJuliWeek(Calendar calendar, int firstDayOfWeek) {
        return getWeeksSinceEpochFromJulianDay(getJuliDay(calendar), firstDayOfWeek);
    }

    /**
     * @param firstDayOfWeek
     * @return
     */
    public static int getJuliWeekNow(int firstDayOfWeek) {
        return getWeeksSinceEpochFromJulianDay(getJuliDay(Calendar.getInstance()), firstDayOfWeek);
    }

    /**
     * 相差多少周
     *
     * @param julianDay
     * @param firstDayOfWeek
     * @return
     */
    public static int getWeeksSinceEpochFromJulianDay(int julianDay, int firstDayOfWeek) {
        int diff = Calendar.MONDAY - firstDayOfWeek;
        if (diff < 0) {
            diff += 7;
        }
        int refDay = EPOCH_JULIAN_DAY - diff;
        return (julianDay - refDay) / 7;
    }

    /**
     * 根据JuliDay获取 时间
     *
     * @param julianDay
     * @return
     */
    public static Calendar getCalendarByJuliDay(int julianDay) {
        Calendar cal = Calendar.getInstance();
        long millis = (julianDay - EPOCH_JULIAN_DAY) * DAY_IN_MILLIS - EPOCH_JU_MS;
        cal.setTimeInMillis(millis);
        int approximateDay = getJuliDay(cal);
        int diff = julianDay - approximateDay;
        cal.add(Calendar.DATE, diff);
        return cal;
    }


    /**
     * 以天为单位获取
     *
     * @param cal1
     * @return
     */
    public static final int getJuliDay(Calendar cal1) {
        Calendar cal = (Calendar) cal1.clone();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        int utfOffset = zoneOffset + dstOffset;
        cal.add(java.util.Calendar.MILLISECOND, utfOffset);

        long juliDay = (cal.getTimeInMillis() + EPOCH_JU_MS) / DAY_IN_MILLIS; //(cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET));// * 1000;
        return (int) (juliDay + EPOCH_JULIAN_DAY);
    }

    /**
     * 以当前日期为基准获取一共有多少周
     *
     * @param baseDate
     * @return
     */
    public static int getWeeks(Calendar baseDate) {
        return baseDate.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 是否为同一月
     *
     * @param date
     * @param date1
     * @return
     */
    public static boolean isSameMonth(Calendar date, Calendar date1) {
        return date.get(Calendar.YEAR) == date1.get(Calendar.YEAR)
                && date.get(Calendar.MONTH) == date1.get(Calendar.MONTH);
    }

    /**
     * 根据日期指示获取月最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getEndOfMonth(Calendar date) {
        Calendar end = (Calendar) date.clone();
        end.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return end;
    }

    /**
     * 是否为谁敢天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Calendar date) {
        return sameDay(date, Calendar.getInstance());
    }

    /**
     * 是否为周末
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Calendar date) {
        final int week = date.get(Calendar.DAY_OF_WEEK);
        return week == Calendar.SUNDAY || week == Calendar.SATURDAY;
    }

    public static int getWeeks(Calendar date, int firstDayOfWeek) {
        Calendar calendar = (Calendar) date.clone();
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        return getWeeks(calendar);
    }
}
