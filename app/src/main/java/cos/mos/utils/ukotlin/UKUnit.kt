package cos.mos.utils.ukotlin

/**
 * @Description:单位换算工具类
 * @Author: Kosmos
 * @Date: 2019.04.23 16:01
 * @Email: KosmoSakura@gmail.com
 */
object UKUnit {
    @JvmStatic
    fun sizeFormatbit(size: Float): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return fotmatMax(size).toString() + "\tB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return fotmatMax(kiloByte).toString() + "\tKB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return fotmatMax(megaByte).toString() + "\tMB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            return fotmatMax(gigaByte).toString() + "\tGB"
        }
        return fotmatMax(teraBytes).toString() + "\tTB"
    }

    @JvmStatic
    fun sizeFormatbitTime(millisecond: Float): String {
        val second = millisecond / 1000
        if (second < 1) {
            return fotmatMax(millisecond).toString() + "ms"
        }
        val min = second / 60
        if (min < 1) {
            return fotmatMax(second).toString() + "s"
        }
        val h = min / 60
        if (h < 1) {
            return fotmatMax(min).toString() + "min"
        }
        val day = h / 24
        if (day < 1) {
            return fotmatMax(h).toString() + "h"
        }
        val mon = day / 30
        if (mon < 1) {
            return fotmatMax(day).toString() + "D"
        }
        val year = mon / 12
        return if (year < 1) {
            fotmatMax(mon).toString() + "M"
        } else fotmatMax(year).toString() + "Y"
    }

    /**
     * @param digit 输入
     * @return 2位有效小数
     * 最优先使用
     */
    @JvmStatic
    fun fotmatMax(digit: Float): Float = (digit * 100f).toInt() / 100f

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