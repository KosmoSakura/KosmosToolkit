package cos.mos.toolkit.ukotlin

import java.math.BigDecimal

/**
 * @Description:单位换算工具类
 * @Author: Kosmos
 * @Date: 2019.04.23 16:01
 * @Email: KosmoSakura@gmail.com
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

    //保留2位有效小数 直接砍掉后面的
    @JvmStatic
    fun keep2(digit: Float): Float = (digit * 100f).toInt() / 100f

    //保留2位有效小数 直接砍掉后面的
    fun keep2(digit: Double): Double = (digit * 100.0).toLong() / 100.0

    //保留2位有效小数 四舍五入=>返回：5.0 、5.12
    fun keepRound2(digit: Double): Double = BigDecimal(digit).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

    //保留3位有效小数 四舍五入 =>返回：5.00 、5.12
    fun keepRoundStr2(digit: Double): String = BigDecimal(digit).setScale(2, BigDecimal.ROUND_HALF_UP).toString()

    //保留3位有效小数 直接砍掉后面的
    fun keep3(digit: Float): Float = (digit * 1000f).toInt() / 1000f

    //保留3位有效小数 直接砍掉后面的
    fun keep3(digit: Double): Double = (digit * 1000.0).toLong() / 1000.0

    //保留3位有效小数 四舍五入 =>返回：5.0 、5.123
    fun keepRound3(digit: Double): Double = BigDecimal(digit).setScale(3, BigDecimal.ROUND_HALF_UP).toDouble()

    //保留3位有效小数 四舍五入 =>返回：5.000、5.123
    fun keepRoundStr3(digit: Double): String = BigDecimal(digit).setScale(3, BigDecimal.ROUND_HALF_UP).toString()

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
}