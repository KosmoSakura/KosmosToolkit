package cos.mos.toolkit.ukotlin

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import java.math.BigDecimal

/**
 * @Description Kotlin拓展函数库
 * @Author Kosmos
 * @Date 2020.03.24 17:06
 * @Email KosmoSakura@gmail.com
 * @tip 2020.4.26-四舍五入，精度问题
 * @tip 2020.4.27-四舍五入，取整
 * */
//最后一次点击时间
var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)//获取保存时间
    get() = getTag(1766613352) as? Long ?: 0//获取点击时间

/**
 * @Description 防止重复点击
 * @Author Kosmos
 * @Date 2020/3/24 5:15 PM
 * @Email KosmoSakura@gmail.com
 * */
inline fun <T : View> T.makeSingleClick(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime > time) {
            lastClickTime = currentClickTime
            block(this)
        }
    }
}

//-----------------------------------------------------------------------------------------------------------


//取整 四舍五入：5.6=>6
fun Double.keepRound(): Int = BigDecimal(this).setScale(0, BigDecimal.ROUND_HALF_UP).toInt()
//取整 四舍五入：5.6=>6,效果同toInt，少一次转换
fun Double.keepRoundStr(): String = BigDecimal(this).setScale(0, BigDecimal.ROUND_HALF_UP).toString()

//保留1位有效小数 四舍五入: 5.25=>5.3
fun Double.keepRound1(): Double = BigDecimal(this).setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()

//保留2位有效小数 直接砍掉后面的:5.116=>5.11
fun Float.keep2(): Float = (this * 100f).toInt() / 100f

//保留2位有效小数 直接砍掉后面的:5.116=>5.11
fun Double.keep2(): Double = (this * 100.0).toLong() / 100.0

//保留2位有效小数 四舍五入=>返回：5.0 、5.12
fun Double.keepRound2(): Double = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

//保留2位有效小数 四舍五入=>返回：5.00 、5.12
fun Double.keepRoundStr2(): String = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toString()

//-----------------------------------------------------------------------------------------------------------

//保留3位有效小数 直接砍掉后面的:5.1235=>5.123
fun Float.keep3(): Float = (this * 1000f).toInt() / 1000f

//保留3位有效小数 直接砍掉后面的:5.1235=>5.123
fun Double.keep3(): Double = (this * 1000.0).toLong() / 1000.0

//保留3位有效小数 四舍五入：5.1235=>5.124 , 5.0=>5.0
fun Double.keepRound3(): Double = BigDecimal(this).setScale(3, BigDecimal.ROUND_HALF_UP).toDouble()

//保留3位有效小数 四舍五入：5.1235=>5.124 , 5.0=>5.000
fun Double.keepRoundStr3(): String = BigDecimal(this).setScale(3, BigDecimal.ROUND_HALF_UP).toString()

//-----------------------------------------------------------------------------------------------------------

fun String?.checkNull(defaul: String = "") = if (this.isNullOrEmpty()) defaul else this
fun String.makeUnderLine(): Spanned = getHtml("<u>$this</u>")
fun String.makeBold(): Spanned = getHtml("<b>$this</b>")
fun String.makeItalic(): Spanned = getHtml("<i>$this</i>")

fun String.makeColor(color: String) = getHtml("<font color='$color'>$this</font>")

private fun getHtml(html: String) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
    Html.fromHtml(html)
} else {
    Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
}

fun String.logD(tag: String = "Kosmos") {
    Log.d(tag, this)
}

fun String.logV(tag: String = "Kosmos") {
    Log.v(tag, this)
}

fun String.logW(tag: String = "Kosmos") {
    Log.w(tag, this)
}

fun String.logI(tag: String = "Kosmos") {
    Log.i(tag, this)
}

fun String.logE(tag: String = "Kosmos") {
    Log.e(tag, this)
}