package tinker.cn.timemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tiankui on 2/27/17.
 */

public class DateUtils {

    // 获得当天0点时间
    public static long getTimesMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    // 获得当天24点时间
    public static long getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得本周一0点时间
    public static long getTimesWeekMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis();
    }
    // 获得本周日24点时间
    public static long getTimesWeekNight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getTimesWeekMorning()));
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTimeInMillis();
    }



    // 获得本月第一天0点时间
    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }
    // 获得本月最后一天24点时间
    public static long getTimesMonthNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
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

    //获取本年开始时间
    public static long getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTimeInMillis();
    }
    //获取本年结束时间
    public static long getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getCurrentYearStartTime()));
        cal.add(Calendar.YEAR, 1);
        return cal.getTimeInMillis();
    }


    // 获得昨天0点时间
    public static long getYesterdaymorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTimesMorning()-3600*24*1000);
        return cal.getTimeInMillis();
    }
    // 获得当天近7天时间
    public static long getWeekFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( getTimesMorning()-3600*24*1000*7);
        return cal.getTimeInMillis();
    }


    //上月开始时间
    public static long getLastMonthStartMorning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(getTimesMonthMorning()));
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
}
