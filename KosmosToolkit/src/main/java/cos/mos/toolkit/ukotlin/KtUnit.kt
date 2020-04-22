package cos.mos.toolkit.ukotlin

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
            return keepDecimals2(size).toString() + "\tB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return keepDecimals2(kiloByte).toString() + "\tKB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return keepDecimals2(megaByte).toString() + "\tMB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            return keepDecimals2(gigaByte).toString() + "\tGB"
        }
        return keepDecimals2(teraBytes).toString() + "\tTB"
    }

    @JvmStatic
    fun sizeFormatbitTime(millisecond: Float): String {
        val second = millisecond / 1000
        if (second < 1) {
            return keepDecimals2(millisecond).toString() + "ms"
        }
        val min = second / 60
        if (min < 1) {
            return keepDecimals2(second).toString() + "s"
        }
        val h = min / 60
        if (h < 1) {
            return keepDecimals2(min).toString() + "min"
        }
        val day = h / 24
        if (day < 1) {
            return keepDecimals2(h).toString() + "h"
        }
        val mon = day / 30
        if (mon < 1) {
            return keepDecimals2(day).toString() + "D"
        }
        val year = mon / 12
        return if (year < 1) {
            keepDecimals2(mon).toString() + "M"
        } else keepDecimals2(year).toString() + "Y"
    }

    /**
     * @param digit 输入
     * @return 2位有效小数
     * 最优先使用
     */
    @JvmStatic
    fun keepDecimals2(digit: Float): Float = (digit * 100f).toInt() / 100f

    /**
     * @param digit 输入
     * @return 保留3位有效小数
     */
    fun keepDecimals3(digit: Float): Float = (digit * 1000f).toInt() / 1000f

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