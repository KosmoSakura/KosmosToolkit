package cos.mos.utils.utils.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * @Description: 时间日期处理
 * @Author: Kosmos
 * @Date: 2018年1月29日 16:04
 * @Email: KosmoSakura@gmail.com
 * @eg 最新修改日期:2018.11.22
 * 获取当前短/长日期
 * @eg 最新修改日期：2018.11.11.
 * setFirstDayOfWeek的失效，修复获取当前星期的天数方法
 * @eg: 最新修改日期：2018.10.31
 * 默认的时间格式为：yyyy-MM-dd HH:mm:ss
 * 其他格式的时间需要自己传入相应的时间格式
 * 以下没有做特殊说明的时间格式都是：yyyy-MM-dd HH:mm:ss
 * <p>
 * 详见注释 (oﾟ▽ﾟ)o---o(◕ᴗ◕✿)
 */
public class UDate {
    private final static int intSECOND = 0x00;// 秒
    private final static int intMINUTE = 0x0001;// 分钟
    private final static int intHOUR = 0x0002;// 小时
    private final static int intDAY = 0x0003;// 天
    private final static int intMONTH = 0x0004;// 月
    private final static int intYEAR = 0x0005;// 年
    private final static long SECOND = 1000;// 秒
    private final static long MINUTE = 60 * 1000;// 分钟
    private final static long HOUR = 60 * MINUTE;// 小时
    private final static long DAY = 24 * HOUR;// 天
    private final static long MONTH = 31 * DAY;// 月
    private final static long YEAR = 12 * MONTH;// 年
    private final static String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";//服务器现在默认的时间格式

    /**
     * @Description: 不对外开发的东西，注释懒得写(oﾟ▽ﾟ)o
     * <p>
     * Calendar中：比如2018-2-7 15:33 星期三
     * <p>
     * Calendar.DATE：--打印->7<--今天是几号--打印->7
     * Calendar.DAY_OF_MONTH：--打印->7<--今天是一月的第几天
     * Calendar.DAY_OF_WEEK：--打印->4<-- 从星期天开始计算，如果今天星期二，那么返回3
     * Calendar.DAY_OF_YEAR：--打印->38<-- 今天是一年的第几天
     * Calendar.HOUR))：--打印->3<-- 现在是几点，12小时制
     * Calendar.HOUR_OF_DAY))：--打印->15<-- 现在是几点，24小时制，一般使用这个属性赋值
     * Calendar.MILLISECOND))：--打印->943<-- 现在的毫秒
     * Calendar.MINUTE))：--打印->33<--现在是几分
     * Calendar.SECOND))：--打印->20<--现在是几秒
     * Calendar.WEEK_OF_MONTH：--打印->2<--现在是一个月中的第几周
     * Calendar.WEEK_OF_YEAR：--打印->6<--现在是一个年中的第几周
     * Calendar.MONTH：--打印->1<--月份获取需要 +1，那么，赋值时需要 -1
     */
    public static void main(String[] args) {
        System.out.println(getDayOfWeek(new Date()));
//        System.out.println("输入日期所在周的周一:" + getMonday("2018-11-11 00:00:00"));
//        System.out.println("输入日期所在周的周七:" + getSunday("2018-11-11 00:00:00"));
//        System.out.println("1-起始时间--------------------------------------------");
//        System.out.println("输入日期所在周的周一:" + getMonday("2018-11-04 00:00:00"));
//        System.out.println("输入日期所在周的周七:" + getSunday("2018-11-04 00:00:00"));
//        System.out.println("1-起始时间--------------------------------------------");
//        System.out.println("输入日期所在周的周一:" + getMonday("2018-11-02 00:00:00"));
//        System.out.println("输入日期所在周的周七:" + getSunday("2018-11-02 00:00:00"));
//        System.out.println("当天:" + singleDateToStr("2018-02-07 15:27:06"));
//        System.out.println("当年:" + singleDateToStr("2018-06-07 15:27:06"));
//        System.out.println("不当年:" + singleDateToStr("2017-02-07 15:27:06"));
//        System.out.println("1-起始时间--------------------------------------");
//        System.out.println("1-当天：" + dateToSimpleStr("2018-02-7 05:02:06", "2018-02-07 15:02:06"));
//        System.out.println("1-当月：" + dateToSimpleStr("2018-02-4 05:02:06", "2018-02-07 15:02:06"));
//        System.out.println("1-当年：" + dateToSimpleStr("2018-06-7 05:02:06", "2018-02-07 15:02:06"));
//        System.out.println("1-不年：" + dateToSimpleStr("2014-02-7 05:02:06", "2018-02-07 15:02:06"));
//        System.out.println("2-距今----------------------------------------------------------------");
//        System.out.println("2-当天：" + dateToNow("2018-02-8 05:02:06"));
//        System.out.println("2-当月：" + dateToNow("2018-02-4 05:02:06"));
//        System.out.println("2-当年：" + dateToNow("2018-06-7 05:02:06"));
//        System.out.println("2-不年：" + dateToNow("2014-02-7 05:02:06"));
//        System.out.println("3-时长----------------------------------------------------------");
//        System.out.println("3-毫秒级：" + longToReadEasy(563));
//        System.out.println("3-秒秒级：" + longToReadEasy(SECOND * 33 + 563));
//        System.out.println("3-分分级：" + longToReadEasy(MINUTE * 12 + SECOND * 33 + 563));
//        System.out.println("3-时时级：" + longToReadEasy(HOUR * 6 + MINUTE * 12 + SECOND * 33 + 563));
//        System.out.println("3-天天级：" + longToReadEasy(DAY * 3 + HOUR * 6 + MINUTE * 12 + SECOND * 33 + 563));
//        System.out.println("3-月月级：" + longToReadEasy(MONTH * 2 + DAY * 3 + HOUR * 6 + MINUTE * 12 + SECOND * 33 + 563));
//        System.out.println("3-年年级：" + longToReadEasy(YEAR * 4 + MONTH * 2 + DAY * 3 + HOUR * 6 + MINUTE * 12 + SECOND * 33 + 563));
    }

    /**
     * @return 当前日期:yyyy-MM-dd HH:mm:ss
     */
    public static String getDateNow(String format) {
        return dateToStrSafety(new Date(), format);
    }

    /**
     * @return 当前短日期
     */
    public static String getDateNowShort() {
        return dateToStrSafety(new Date(), "yyyy-MM-dd");
    }

    /**
     * @return 当前短日期
     */
    public static String getDateNowLong() {
        return dateToStrSafety(new Date(), FORMAT_DEFAULT);
    }

    /**
     * @param future 是否未来
     * @return X周的当天的日期
     */
    public static String getAnotherWeekDate(boolean future, int x, String aims) {
        Calendar calendar = strToCalendar(aims);
        calendar.add(Calendar.DATE, future ? 7 * x : -7 * x);
        return dateToStrSafety(calendar.getTime(), FORMAT_DEFAULT);
    }

    /**
     * @param future 是否未来
     * @return X周的当天的日期
     */
    public static String getAnotherWeekDate(boolean future, int x, Date aims) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aims);
        calendar.add(Calendar.DATE, future ? 7 * x : -7 * x);
        return dateToStrSafety(calendar.getTime(), FORMAT_DEFAULT);
    }

    /**
     * @param future 是否未来
     * @return X周的今天的日期
     */
    public static String getAnotherWeekDateByNow(boolean future, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, future ? 7 * x : -7 * x);
        return dateToStrSafety(calendar.getTime(), FORMAT_DEFAULT);
    }

    /**
     * @param future 是否未来
     * @return X月的今天的日期
     */
    public static String getAnotherMonthDate(boolean future, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, future ? x : -x);
        return dateToStrSafety(calendar.getTime(), FORMAT_DEFAULT);
    }

    /**
     * @param future 是否未来
     * @return X年的今天的日期
     */
    public static String getAnotherYearDate(boolean future, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, future ? x : -x);
        return dateToStrSafety(calendar.getTime(), FORMAT_DEFAULT);
    }

    /**
     * @param date 日期
     * @return 输入日期为该周的第几天（第一天为周一计算）
     * @eg 踩坑记录：setFirstDayOfWeek的2个误区
     * Calendar中的星期一、二、三、四、五、六、日、DAY_OF_WEEK等都是以一个常量的形式存在的。
     * 1.并不能改变这些常量的值，因此DAY_OF_WEEK的值不变
     * 2.仅能影响：getFirstDayOfWeek的值（但是 必须在设置时间之前，否则失效）
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * @param str 日期
     * @return 输入日期所在周的周一
     */
    public static String getSunday(String str) {
        return getSunday(strToDateSafety(str, FORMAT_DEFAULT));
    }

    /**
     * @param date 日期
     * @return 输入日期所在周的周一
     */
    public static String getSunday(Date date) {
        return dateToStrSafety(getSundayOfDate(date), FORMAT_DEFAULT);
    }

    /**
     * @param date 日期
     * @return 输入日期所在周的周七
     */
    public static Date getSundayOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getMondayOfDate(date));
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }

    /**
     * @param str 日期
     * @return 输入日期所在周的周一
     */
    public static String getMonday(String str) {
        return getMonday(strToDateSafety(str, FORMAT_DEFAULT));
    }

    /**
     * @param date 日期
     * @return 输入日期所在周的周一
     */
    public static String getMonday(Date date) {
        return dateToStrSafety(getMondayOfDate(date), FORMAT_DEFAULT);
    }

    /**
     * @param date 日期
     * @return 输入日期所在周的周一
     */
    public static Date getMondayOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (1 == calendar.get(Calendar.DAY_OF_WEEK)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        //第一天为周一:仅能影响：getFirstDayOfWeek的值
        //第一天为周日，必须在设置时间之前，否则失效
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //当前日期是一个星期的第几天
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - dayWeek);
        return calendar.getTime();
    }

    /**
     * @param birthDay 生日
     * @return 年龄
     * @apiNote 如果生日在未来，返回0
     */
    public static int getAge(String birthDay) {
        Date date = strToDate(birthDay, FORMAT_DEFAULT);
        if (date == null) {
            return 0;
        } else return getAge(date);
    }

    /**
     * @param birthDay 生日
     * @return 年龄
     * @apiNote 如果生日在未来，返回0
     */
    public static int getAge(Date birthDay) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(birthDay)) {
            return 0;
        }
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthDay);

        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH);
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * @param str 2018-02-01 15:27:06
     * @return 15:27
     * @Description: 时间的简写
     * 当天：15:27
     * 当年：11-06 15:27
     * 不同年：2017-11-06 05 15:27
     */
    public static String singleDateToStr(String str) {
        return singleDateToStr(strToCalendar(str), Calendar.getInstance());
    }

    public static String singleDateToStr(String str, String format) {
        return singleDateToStr(strToCalendar(str, format), Calendar.getInstance());
    }

    public static String singleDateToStr(String str, String str2, String format) {
        return singleDateToStr(strToCalendar(str, format), strToCalendar(str2, format));
    }

    /**
     * @apiNote 不对外开发
     */
    private static String singleDateToStr(Calendar c, Calendar cTo) {
        if (c.get(Calendar.YEAR) == cTo.get(Calendar.YEAR) &&
            c.get(Calendar.MONTH) == cTo.get(Calendar.MONTH) &&
            c.get(Calendar.DAY_OF_MONTH) == cTo.get(Calendar.DAY_OF_MONTH)) {
            return zeroAdd(c.get(Calendar.HOUR_OF_DAY)) + ":" + zeroAdd(c.get(Calendar.MINUTE));
        } else if (c.get(Calendar.YEAR) == cTo.get(Calendar.YEAR)) {
            return zeroAdd(c.get(Calendar.MONTH) + 1) + "-" + zeroAdd(c.get(Calendar.DAY_OF_MONTH)) + " " + zeroAdd(c.get(Calendar.HOUR)
            ) + ":" + zeroAdd(c.get(Calendar.MINUTE));
        } else {
            return c.get(Calendar.YEAR) + "-" + zeroAdd(c.get(Calendar.MONTH) + 1) + "-" + zeroAdd(c.get(Calendar.DAY_OF_MONTH)) + " " +
                zeroAdd(c.get(Calendar.HOUR_OF_DAY)) + ":" + zeroAdd(c.get(Calendar.MINUTE));
        }
    }

    /**
     * @param d1 2018-01-10 16:57:00
     * @param d2 2018-01-10 16:57:00
     * @return 同一天:2018-10-05 15:30 至 15:40
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年:2018-10-05 15:30 至  2019-11-06 15:30
     * @Description: 两个时间段的简写
     */
    public static String dateToSimpleStr(String d1, String d2) {
        return dateToSimpleStr(strToDate(d1), strToDate(d2));
    }

    /**
     * @param format yyyy-MM-dd HH:mm:ss
     * @param d1     2018-01-10 16:57:00
     * @param d2     2018-01-10 16:57:00
     * @return 同一天:2018-10-05 15:30 至 15:40
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年:2018-10-05 15:30 至  2019-11-06 15:30
     * @Description: 两个时间段的简写
     */
    public static String dateToSimpleStr(String d1, String d2, String format) {
        return dateToSimpleStr(strToDate(d1, format), strToDate(d2, format));
    }

    /**
     * @param date 2018-01-10 16:57:00
     * @return 同一天:2018-10-05 15:30 至 15:40
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年:2018-10-05 15:30 至  2019-11-06 15:30
     * @Description: 某个时间段和现在时间的简要写法
     */
    public static String dateTodaySimpleStr(String date) {
        return dateToSimpleStr(strToDate(date), new Date());
    }

    /**
     * @param date   2018-01-10 16:57:00
     * @param format 非默认时间格式的时间需要自己传入时间格式
     * @return 同一天:2018-10-05 15:30 至 15:40
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年:2018-10-05 15:30 至  2019-11-06 15:30
     * @Description: 某个时间段和现在时间的简要写法
     */
    public static String dateTodaySimpleStr(String date, String format) {
        return dateToSimpleStr(strToDate(date, format), new Date());
    }

    /**
     * @param date 目标时间
     * @return 同一天:2018-10-05 15:30 至 15:40
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年2018-10-05 15:30 至  2019-11-06 15:30
     * @Description: 某个时间段和现在时间的简要写法
     */
    public static String dateToSimpleStr(Date date) {
        return dateToSimpleStr(date, new Date());
    }

    /**
     * 同一天:2018-1-26 16:00 至 16:39
     * 同一月:
     * 同一年:2018-10-05 15:30 至 11-06 15:30
     * 不同年2018-10-05 15:30 至  2019-11-06 15:30
     *
     * @Description: 万恶之源，外面基本不会用到
     */
    private static String dateToSimpleStr(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return "????";
        }
        if (isSameDay(d1, d2)) {
            return dateToStrSafety(d1, "yyyy-MM-dd HH:mm") + " 至 " + dateToStrSafety(d2, "HH:mm");
        } else if (isSameYear(d1, d2)) {
            return dateToStrSafety(d1, "yyyy-MM-dd HH:mm") + " 至 " + dateToStrSafety(d2, "MM-dd HH:mm");
        } else {
            return dateToStrSafety(d1, "yyyy-MM-dd HH:mm") + " 至 " + dateToStrSafety(d2, "yyyy-MM-dd HH:mm");
        }
    }

    /**
     * @param date 目标时间
     * @Description: 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前
     * @eg: 2年前
     */
    public static String dateToNow(String date) {
        return dateToNow(strToDateSafety(date));
    }

    public static String dateToNow(Date date) {
        if (date == null) {
            return "????";
        }
        long diff = new Date().getTime() - date.getTime();
        if (diff > YEAR) {
            return (diff / YEAR) + "年前";
        } else if (diff > MONTH) {
            return (diff / MONTH) + "个月前";
        } else if (diff > DAY) {
            return (diff / DAY) + "天前";
        } else if (diff > HOUR) {
            return (diff / HOUR) + "小时前";
        } else if (diff > MINUTE) {
            return (diff / MINUTE) + "分钟前";
        } else if (diff < -YEAR) {
            return -(diff / YEAR) + "年后";
        } else if (diff < -MONTH) {
            return -(diff / MONTH) + "个月后";
        } else if (diff < -DAY) {
            return -(diff / DAY) + "天后";
        } else if (diff < -HOUR) {
            return -(diff / HOUR) + "小时后";
        } else if (diff < -MINUTE) {
            return -(diff / MINUTE) + "分钟后";
        } else {
            return "小于一分钟";
        }
    }

    /**
     * @param millisecond 目标时间毫秒数
     * @return 14年2月1天11小时9分钟
     * @Description: 目标时间和现在时间的长度
     */
    public static String longToReadEasy(long millisecond) {
        String str = "";
        if (millisecond / YEAR > 0) {
            str += String.valueOf(millisecond / YEAR) + "年";
            millisecond = millisecond % YEAR;
        }
        if (millisecond / MONTH > 0) {
            str += String.valueOf(millisecond / MONTH) + "月";
            millisecond = millisecond % MONTH;
        }
        if (millisecond / DAY > 0) {
            str += String.valueOf(millisecond / DAY) + "天";
            millisecond = millisecond % DAY;
        }
        if (millisecond / HOUR > 0) {
            str += String.valueOf(millisecond / HOUR) + "小时";
            millisecond = millisecond % HOUR;
        }
        if (millisecond / MINUTE > 0) {
            str += String.valueOf(millisecond / MINUTE) + "分钟";
            millisecond = millisecond % MINUTE;
        }
        if ("".equals(str) && millisecond / SECOND > 0) {
            str = String.valueOf(millisecond / SECOND) + "秒";
        }
        if ("".equals(str)) {
            str = "小于一秒";
        }
        return str;
    }

    /**
     * @param format yyyy-MM-dd HH:mm
     * @Description: Date转换指定格式的字符串
     */
    public static String dateToStrSafety(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }


    /**
     * @param str          待转换的时间：2018-01-10 16:57:00
     * @param targetFormat 要转换的时间格式：yyyy-MM-dd HH:mm
     * @Description: 一种格式的时间 转换另一个 格式的时间
     */
    public static String strToStrSafety(String str, String targetFormat) {
        return strToStrSafety(str, FORMAT_DEFAULT, targetFormat);
    }

    /**
     * @param str          待转换的时间：2018-01-10 16:57:00
     * @param oldFormat    原时间的格式：yyyy-MM-dd HH:mm:ss
     * @param targetFormat 目标格式：yyyy年MM月dd HH:mm
     * @Description: 一种格式的时间 转换另一个 格式的时间
     */
    public static String strToStrSafety(String str, String oldFormat, String targetFormat) {
        DateFormat dfTarget = new SimpleDateFormat(targetFormat, Locale.getDefault());
        try {
            return dfTarget.format(strToDate(str, oldFormat));
        } catch (Exception e) {
            return "????";
        }
    }

    /**
     * @param time 目标时间：2018-01-10 16:57:00
     * @Description: 以默认格式转换时间
     * 返回null表示出错了
     */
    public static Date strToDate(String time) {
        return strToDate(time, FORMAT_DEFAULT);
    }

    /**
     * @param time 目标时间：2018-01-10 16:57:00
     * @Description: 楼上版本的安全版
     * 以默认格式转换时间
     * 传入数据有误，返回当前时间，
     */
    public static Date strToDateSafety(String time) {
        return strToDateSafety(time, FORMAT_DEFAULT);
    }

    /**
     * @param time 目标时间：2018-01-10 16:57:00
     * @Description: 以指定格式转换时间
     * 返回null表示出错了
     */
    public static Date strToDate(String time, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(true);
            return df.parse(time);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param time 目标时间：2018-01-10 16:57:00
     * @Description: 楼上版本的安全版
     * 以指定格式转换时间
     * 传入数据有误，返回当前时间，
     */
    public static Date strToDateSafety(String time, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(true);
            return df.parse(time);
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * @return 返回当前日期xxxx年x月xx日 星期x
     * @eg 2015年1月28日  星期三
     */
    public static String dateToWeek(String time) {
        return dateToWeek(strToDate(time));
    }

    public static String dateToWeek() {
        return dateToWeek(new Date());
    }

    /**
     * @param date 目标日期
     * @return 返回当前日期xxxx年x月xx日 星期x
     * @eg 2015年1月28日  星期三
     */
    public static String dateToWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int w = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return c.get(Calendar.YEAR) + "年" + zeroAdd((c.get(Calendar.MONTH) + 1)) + "月"
            + zeroAdd(c.get(Calendar.DATE)) + "日  " + weekDays[w];
    }

    /**
     * @param millis 目标时间毫秒
     * @param format 转换格式
     * @return 你懂的 （๑》ڡ《）☆
     */
    private static String longToStr(long millis, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * @param time   目标时间
     * @param format 转换格式
     * @return 当前时间的毫秒数
     * 不过我感觉这个使用的场景比较少(；´д｀)ゞ
     * 报错返回-1.ヽ(￣▽￣)ﾉ
     */
    private static long strToLong(String time, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(time).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param time   目标时间
     * @param format 该时间的格式
     * @Description: 判断是否是今天
     */
    public static boolean isToday(String time, String format) {
        return isToday(strToDate(time, format));
    }

    /**
     * @param date 目标时间
     * @Description: 判断是否是今天
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    /**
     * @param format 时间格式
     * @Description: 判断两个时间是否是同一天
     */
    public static boolean isSameDay(String t1, String t2, String format) {
        return isSameDay(strToDate(t1, format), strToDate(t2, format));
    }

    /**
     * @Description: 判断两个时间是否是同一天
     */
    public static boolean isSameDay(Date d1, Date d2) {
        return compare(dateToCalendar(d1), dateToCalendar(d2), intDAY);
    }

    /**
     * @param date 目标时间
     * @Description: 判断是否是今年
     */
    public static boolean isThisYear(Date date) {
        return isSameYear(date, new Date());
    }

    /**
     * @Description: 判断两个时间是否是同一年
     */
    public static boolean isSameYear(Date d1, Date d2) {
        return compare(dateToCalendar(d1), dateToCalendar(d2), intYEAR);
    }

    /**
     * @param date 目标时间
     * @Description: 判断是否是当月
     */
    public static boolean isThisMonth(Date date) {
        return isSameMonth(date, new Date());
    }

    /**
     * @Description: 判断两个时间是否是在用一个月
     */
    public static boolean isSameMonth(Date d1, Date d2) {
        return compare(dateToCalendar(d1), dateToCalendar(d2), intMONTH);
    }


    public static Calendar strToCalendar(String str) {
        return strToCalendar(str, FORMAT_DEFAULT);
    }

    public static Calendar strToCalendar(String str, String fotmat) {
        Calendar calendar = Calendar.getInstance();
        Date date = strToDateSafety(str, fotmat);
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar dateToCalendar(Date d1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        return calendar;
    }

    private static String zeroAdd(int x) {
        if (x < 10) {
            return String.valueOf("0" + x);
        } else {
            return String.valueOf(x);
        }
    }

    /**
     * @param norm 标准
     * @Description: 不对外开发的东西，注释懒得写(oﾟ▽ﾟ)o
     * <p>
     */
    private static boolean compare(Calendar c1, Calendar c2, int norm) {
        switch (norm) {
            case intYEAR:
                return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
            case intMONTH:
                return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                    c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
            case intDAY:
                return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                    c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                    c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
            default:
                throw new NoSuchElementException("参数传入不合理");
        }
    }
}
