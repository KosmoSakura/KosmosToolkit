package cos.mos.toolkit.java

import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description 日历工具
 * @Author Kosmos
 * @Date 2020.09.14 23:09
 * @Email KosmoSakura@gmail.com
 * */
object UCalendar {
    private val intSECOND = 0x00// 秒
    private val intMINUTE = 0x0001// 分钟
    private val intHOUR = 0x0002// 小时
    private val intDAY = 0x0003// 天
    private val intMONTH = 0x0004// 月
    private val intYEAR = 0x0005// 年
    private val SECOND: Long = 1000// 秒
    private val MINUTE = (60 * 1000).toLong()// 分钟
    private val HOUR = 60 * MINUTE// 小时
    private val DAY = 24 * HOUR// 天
    private val MONTH = 31 * DAY// 月
    private val YEAR = 12 * MONTH// 年
    const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"//默认的时间格式
    const val FORMAT_DEFAULT_No_SEPARATOR = "yyyyMMddHHmmss"//时间格式-没有分隔符
    const val FORMAT_DEFAULT_DATE = "yyyy-MM-dd"//默认的日期格式
    const val FORMAT_DEFAULT_TIME = "HH:mm:ss"//默认的时间格式

    /**
     * @return 当前日期:yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun getDateNow(format: String = FORMAT_DEFAULT): String = dateToStr(Date(), format)

    @JvmStatic
    fun dateToStr(date: Date, format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(date)

    fun getDateYear() = getDateYear(null)

    @JvmStatic//获取年月日
    fun getDateYear(date: Date?) = Calendar.getInstance().run {
        if (date != null) {
            time = date
        }
//        val hour=  get(Calendar.HOUR_OF_DAY)
//        val minute =  get(Calendar.MINUTE)
//        val second =  get(Calendar.SECOND)
        "${get(Calendar.YEAR)}年${get(Calendar.MONTH) + 1}月${get(Calendar.DAY_OF_MONTH)}日"
    }

    fun getDateYearAll() = getDateYearAll(null)

    @JvmStatic//获取年月日时分秒
    fun getDateYearAll(date: Date?) = Calendar.getInstance().run {
        if (date != null) {
            time = date
        }
        "${get(Calendar.YEAR)}年${get(Calendar.MONTH) + 1}月${get(Calendar.DAY_OF_MONTH)}日 ${get(Calendar.HOUR_OF_DAY)}:${get(Calendar.MINUTE)}:${get(Calendar.SECOND)}"
    }

    @JvmStatic//获取月日
    fun getMothDay(): String = getMothDay(null)

    @JvmStatic//获取月日
    fun getMothDay(date: Date?) = Calendar.getInstance().run {
        if (date != null) {
            time = date
        }
        "${get(Calendar.MONTH) + 1} 月 ${get(Calendar.DAY_OF_MONTH)} 日"
    }

    @JvmStatic//获取星期
    fun getWeekStr(date: Date?) = Calendar.getInstance().run {
        if (date != null) {
            time = date
        }
        when (get(Calendar.DAY_OF_WEEK)) {
            1 -> "星期 日"
            2 -> "星期 一"
            3 -> "星期 二"
            4 -> "星期 三"
            5 -> "星期 四"
            6 -> "星期 五"
            7 -> "星期 六"
            else -> "星期 0"
        }
    }

    @JvmStatic//获取星期
    fun getWeekStr() = getWeekStr(null)

    @JvmStatic //获取小时
    fun getHour(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.HOUR_OF_DAY]
    }

    @JvmStatic //获取分钟
    fun getMinute(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.MINUTE]
    }

    @JvmStatic//获取周
    fun getWeek(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_WEEK]
    }

    @JvmStatic//获取周
    fun getWeek(year: Int, moth: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar[year, moth - 1] = day
        return calendar[Calendar.DAY_OF_WEEK]
    }

    @JvmStatic//获取年
    fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR]
    }

    @JvmStatic//获取月
    fun getMoth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.MONTH] + 1
    }

    @JvmStatic//获取日
    fun getDay(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DATE]
    }

    @JvmStatic
    fun getDate(year: Int, moth: Int, day: Int, hour: Int, minute: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar[year, moth - 1, day, hour] = minute
        return calendar.time
    }
}