package cos.mos.toolkit.ukotlin

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import java.math.BigDecimal
import kotlin.math.*

/**
 * @Description Kotlin拓展函数库
 * @Author Kosmos
 * @Date 2020.03.24 17:06
 * @Email KosmoSakura@gmail.com
 * @tip 2020.4.26-四舍五入，精度问题
 * @tip 2020.4.27-四舍五入，取整
 * @tip 2020.6.8-三角函数，弧度转度
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

//保留1位有效小数 四舍五入: 5.25=>5.3
fun Double.keepRoundStr1(): String = BigDecimal(this).setScale(1, BigDecimal.ROUND_HALF_UP).toString()

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
//保留6位有效小数 四舍五入
fun Double.keepRoundStr6(): String = BigDecimal(this).setScale(6, BigDecimal.ROUND_HALF_UP).toString()

//保留6位有效小数 四舍五入
fun Double.keepRound6(): Double = BigDecimal(this).setScale(6, BigDecimal.ROUND_HALF_UP).toDouble()

//保留6位有效小数 进位:5.11111111=>5.111112
fun Double.keepRound6Up(): Double = BigDecimal(this).setScale(6, BigDecimal.ROUND_UP).toDouble()

//保留6位有效小数 进位:5.11111111=>5.111112
fun Double.keepRoundStr6Up(): String = BigDecimal(this).setScale(6, BigDecimal.ROUND_UP).toString()
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

//-----------------------------------------------------------------------------------------------------------
private const val PI_D: Double = 3.141592653589793
private const val PI_F: Float = 3.141592653589793f
fun Double.toDegrees(): Double = this * 180.0 / PI_D//弧度=>度
fun Double.toRadians(): Double = this / 180.0 * PI_D//度=>弧度
fun Float.toDegrees(): Float = this * 180.0f / PI_F//弧度=>度
fun Float.toRadians(): Float = this / 180.0f * PI_F//度=>弧度

fun Double.toSin() = sin(this.toRadians())//sin计算，传入度
fun Float.toSin() = sin(this.toRadians())//sin计算，传入度
fun Double.toCos() = cos(this.toRadians())//cos计算，传入度
fun Float.toCos() = cos(this.toRadians())//cos计算，传入度
fun Double.toTan() = tan(this.toRadians())//tan计算，传入度
fun Float.toTan() = tan(this.toRadians())//tan计算，传入度

//已知正弦求角度:siaθ=X => θ=asinX
fun Double.toAsin() = asin(this).toDegrees()//asin计算，返回度
fun Float.toAsin() = asin(this).toDegrees()//asin计算，返回度
fun Double.toAcos() = acos(this).toDegrees()//acos计算，传入度
fun Float.toAcos() = acos(this).toDegrees()//acos计算，传入度
fun Double.toAtan() = atan(this).toDegrees()//atan计算，传入度
fun Float.toAtan() = atan(this).toDegrees()//atan计算，传入度