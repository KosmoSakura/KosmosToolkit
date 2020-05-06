package cos.mos.toolkit.ukotlin

import java.math.BigDecimal

/**
 * @Description:单位换算工具类
 * @Author: Kosmos
 * @Date: 2019.04.23 16:01
 * @Email: KosmoSakura@gmail.com
 * @tip 2020.4.27-四舍五入，取整、精度问题
 */
object KtUnit {
    @JvmStatic
    fun sizeFormatbit(size: Float): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return keep2(size).toString() + "\tB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return keep2(kiloByte).toString() + "\tKB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return keep2(megaByte).toString() + "\tMB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            return keep2(gigaByte).toString() + "\tGB"
        }
        return keep2(teraBytes).toString() + "\tTB"
    }

    @JvmStatic
    fun sizeFormatbitTime(millisecond: Float): String {
        val second = millisecond / 1000
        if (second < 1) {
            return keep2(millisecond).toString() + "ms"
        }
        val min = second / 60
        if (min < 1) {
            return keep2(second).toString() + "s"
        }
        val h = min / 60
        if (h < 1) {
            return keep2(min).toString() + "min"
        }
        val day = h / 24
        if (day < 1) {
            return keep2(h).toString() + "h"
        }
        val mon = day / 30
        if (mon < 1) {
            return keep2(day).toString() + "D"
        }
        val year = mon / 12
        return if (year < 1) {
            keep2(mon).toString() + "M"
        } else keep2(year).toString() + "Y"
    }

    //-----------------------------------------------------------------------------------------------------------

    @JvmStatic//取整 四舍五入：5.6=>6
    fun keepRound(digit: Double): Int = BigDecimal(digit).setScale(0, BigDecimal.ROUND_HALF_UP).toInt()

    @JvmStatic//取整 四舍五入：5.6=>6,效果同toInt，少一次转换
    fun keepRoundStr(digit: Double): String = BigDecimal(digit).setScale(0, BigDecimal.ROUND_HALF_UP).toString()

    //-----------------------------------------------------------------------------------------------------------

    @JvmStatic//保留1位有效小数 四舍五入: 5.25=>5.3
    fun keepRound1(digit: Double): Double = BigDecimal(digit).setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()

    @JvmStatic//保留2位有效小数 直接砍掉后面的:5.116=>5.11
    fun keep2(digit: Float): Float = (digit * 100f).toInt() / 100f

    @JvmStatic //保留2位有效小数 直接砍掉后面的:5.1235=>5.123
    fun keep2(digit: Double): Double = (digit * 100.0).toLong() / 100.0

    @JvmStatic//保留2位有效小数 四舍五入=>返回：5.0 、5.12
    fun keepRound2(digit: Double): Double = BigDecimal(digit).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    @JvmStatic//保留2位有效小数 四舍五入 =>返回：5.00 、5.12
    fun keepRoundStr2(digit: Double): String = BigDecimal(digit).setScale(2, BigDecimal.ROUND_HALF_UP).toString()

    //-----------------------------------------------------------------------------------------------------------

    @JvmStatic//保留3位有效小数 直接砍掉后面的:5.11677=>5.116
    fun keep3(digit: Float): Float = (digit * 1000f).toInt() / 1000f

    @JvmStatic//保留3位有效小数 直接砍掉后面的:5.11677=>5.116
    fun keep3(digit: Double): Double = (digit * 1000.0).toLong() / 1000.0

    @JvmStatic //保留3位有效小数 四舍五入：5.1235=>5.124 , 5.0=>5.0
    fun keepRound3(digit: Double): Double = BigDecimal(digit).setScale(3, BigDecimal.ROUND_HALF_UP).toDouble()

    @JvmStatic//保留3位有效小数 四舍五入：5.1235=>5.124 , 5.0=>5.000
    fun keepRoundStr3(digit: Double): String = BigDecimal(digit).setScale(3, BigDecimal.ROUND_HALF_UP).toString()

    //-----------------------------------------------------------------------------------------------------------

    /**
     * @param celsius 摄氏度
     * @return 华氏度
     */
    @JvmStatic
    fun cToF(celsius: Float): Float = 9 * celsius / 5 + 32

    /**
     * @param fahrenheit 华氏度
     * @return 摄氏度
     */
    @JvmStatic
    fun fToC(fahrenheit: Float): Float = (fahrenheit - 32) * 5 / 9

    //-------------------------------------------------------------------------------------------------------------------
    /**
     * @param version    要比较的版本号
     * @param versionOld 被比较的版本号
     * @return 比较版本号的大小, 前者大则返回一个正数, 后者大返回一个负数, 相等则返回0
     */
    fun compareVersion(version: String?, versionOld: String?): Int {
        if (version == null || versionOld == null) {
            throw NullPointerException("传入对象为空")
        }
        val versionArray1 = version.split("\\.").toTypedArray() //注意此处为正则匹配，不能用"."；
        val versionArray2 = versionOld.split("\\.").toTypedArray()
        var idx = 0
        val minLength = versionArray1.size.coerceAtMost(versionArray2.size) //取最小长度值
        var diff = 0
        while (idx < minLength && versionArray1[idx].length - versionArray2[idx].length.also { diff = it } == 0 //先比较长度
            && versionArray1[idx].compareTo(versionArray2[idx]).also { diff = it } == 0) { //再比较字符
            ++idx
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = if (diff != 0) diff else versionArray1.size - versionArray2.size
        return diff
    }
}