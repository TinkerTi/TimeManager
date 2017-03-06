package tinker.cn.timemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tinker.cn.timemanager.model.BaseConstant;

/**
 * Created by tiankui on 2/27/17.
 */

public class DateUtils {

    // 获得当天0点时间
    public static long getTodayMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得当天24点时间
    public static long getTodayNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    //获取前n天开始时间
    public static long getDayStartTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getTodayMorning()));
        calendar.add(Calendar.DAY_OF_YEAR, -count);
        return calendar.getTimeInMillis();
    }

    //获取前n天结束时间
    public static long getDayEndTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getDayStartTime(count)));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTimeInMillis();
    }

    // 获得本周一0点时间
    public static long getCurrentWeekMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis();
    }

    // 获得本周日24点时间
    public static long getCurrentWeekNight() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(new Date(getCurrentWeekMorning()));
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTimeInMillis();
    }

    //获取前n周开始时间
    public static long getWeekStartTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getCurrentWeekMorning()));
        calendar.add(Calendar.WEEK_OF_YEAR, -count);
        return calendar.getTimeInMillis();
    }

    //获取前n周结束时间
    public static long getWeekEndTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getWeekStartTime(count)));
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return calendar.getTimeInMillis();
    }


    // 获得本月第一天0点时间
    public static long getCurrentMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得本月最后一天24点时间
    public static long getCurrentMonthNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    //获取前n月的开始时间
    public static long getMonthStartTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getCurrentMonthMorning()));
        calendar.add(Calendar.MONTH, -count);
        return calendar.getTimeInMillis();
    }

    //获取前n月的结束时间
    public static long getMonthEndTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getMonthStartTime(count)));
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTimeInMillis();
    }


    //获取本年开始时间
    public static long getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取本年结束时间
    public static long getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getCurrentYearStartTime()));
        cal.add(Calendar.YEAR, 1);
        return cal.getTimeInMillis();
    }


    //获取前n年的开始时间
    public static long getYearStartTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getCurrentYearStartTime()));
        calendar.add(Calendar.YEAR, -count);
        return calendar.getTimeInMillis();
    }

    //获取前n年的结束时间
    public static long getYearEndTime(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getYearStartTime(count)));
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTimeInMillis();
    }


    public static long getStartTimeByType(int type, int count) {
        long time = 0;
        switch (type) {
            case BaseConstant.DAY_TYPE:
                time = getDayStartTime(count);
                break;
            case BaseConstant.WEEK_TYPE:
                time = getWeekStartTime(count);
                break;
            case BaseConstant.MONTH_TYPE:
                time = getMonthStartTime(count);
                break;
            case BaseConstant.YEAR_TYPE:
                time = getYearStartTime(count);
                break;
        }
        return time;
    }
    public static long getEndTimeByType(int type, int count) {
        long time = 0;
        switch (type) {
            case BaseConstant.DAY_TYPE:
                time = getDayEndTime(count);
                break;
            case BaseConstant.WEEK_TYPE:
                time = getWeekEndTime(count);
                break;
            case BaseConstant.MONTH_TYPE:
                time = getMonthEndTime(count);
                break;
            case BaseConstant.YEAR_TYPE:
                time = getYearEndTime(count);
                break;
        }
        return time;
    }

    // 获得昨天0点时间
    public static long getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTodayMorning() - 3600 * 24 * 1000);
        return cal.getTimeInMillis();
    }

    // 获得当天近7天时间
    public static long getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTodayMorning() - 3600 * 24 * 1000 * 7);
        return cal.getTimeInMillis();
    }


    //上月开始时间
    public static long getLastMonthStartMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getCurrentMonthMorning()));
        cal.add(Calendar.MONTH, -1);
        return cal.getTimeInMillis();
    }

    //去年开始时间
    public static long getLastYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getCurrentYearStartTime()));
        cal.add(Calendar.YEAR, -1);
        return cal.getTimeInMillis();
    }

    //获取本季度开始时间；
    public static long getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now.getTime();
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getCurrentQuarterStartTime()));
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }


}
