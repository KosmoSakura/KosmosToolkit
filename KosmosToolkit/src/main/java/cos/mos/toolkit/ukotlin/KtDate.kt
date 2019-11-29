package cos.mos.toolkit.ukotlin

import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:时间日期处理
 * @Author: Kosmos
 * @Date: 2019.04.23 16:09
 * @Email: KosmoSakura@gmail.com
 */
object KtDate {
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
    private const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"//服务器现在默认的时间格式

    /**
     * @return 当前日期:yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun getDateNow(format: String = FORMAT_DEFAULT): String = dateToStr(Date(), format)

    @JvmStatic
    fun dateToStr(date: Date, format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(date)
}