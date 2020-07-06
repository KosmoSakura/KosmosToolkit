package cos.mos.toolkit.ukotlin

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import java.math.BigDecimal
import kotlin.math.*

/**
 * @Description Kotlin拓展函数库
 * @Author Kosmos
 * @Date 2020.04.22 21:50
 * @Email KosmoSakura@gmail.com
 * */
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

//保留2位有效小数 四舍五入=>返回：5.0 、5.12
fun Double.keepRound2(): Double = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

//保留2位有效小数 四舍五入=>返回：5.00 、5.12
fun Double.keepRoundStr2(): String = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toString()

//-----------------------------------------------------------------------------------------------------------
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
//保留2位有效小数 直接砍掉后面的:5.116=>5.11
fun Float.keep2(): Float = (this * 100f).toInt() / 100f
fun Double.keep2(): Double = (this * 100.0).toLong() / 100.0
fun Float.keep3(): Float = (this * 1000f).toInt() / 1000f
fun Double.keep3(): Double = (this * 1000.0).toLong() / 1000.0
fun Float.keep5(): Float = (this * 100000).toInt() / 100000f
fun Double.keep5(): Double = (this * 100000).toInt() / 100000.0
fun Float.keep6(): Float = (this * 1000000).toInt() / 1000000f //注：保证Float和Double输出数据相同，则切6位
fun Double.keep6(): Double = (this * 1000000).toInt() / 1000000.0//注：保证Float和Double输出数据相同，则切6位
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
//舷角解析∈[0,360]：从0度计算到左右舷各180度（左舷为-，右舷为+）
fun Double.parseQwo() = (this % 360).let { if (it > 180) it - 360 else if (it < -180) 360 + it else it }
const val PI_D: Double = 3.141592653589793
const val PI_F: Float = 3.141592653589793f
fun Double.toDegrees(): Double = (this * 180.0 / PI_D).keep6()//弧度=>度
fun Double.toRadians(): Double = (this / 180.0 * PI_D).keep6()//度=>弧度
fun Float.toDegrees(): Float = (this * 180.0f / PI_F).keep6()//弧度=>度
fun Float.toRadians(): Float = (this / 180.0f * PI_F).keep6()//度=>弧度

fun Double.toSin() = sin(this.toRadians()).keep6()//sin计算，传入度
fun Float.toSin() = sin(this.toRadians()).keep6()//sin计算，传入度
fun Double.toCos() = cos(this.toRadians()).keep6()//cos计算，传入度
fun Float.toCos() = cos(this.toRadians()).keep6()//cos计算，传入度
fun Double.toTan() = tan(this.toRadians()).keep6()//tan计算，传入度
fun Float.toTan() = tan(this.toRadians()).keep6()//tan计算，传入度

//已知正弦求角度:siaθ=X => θ=asinX
fun Double.toAsin() = asin(this).toDegrees().keep6()//asin计算，返回度
fun Float.toAsin() = asin(this).toDegrees().keep6()//asin计算，返回度
fun Double.toAcos() = acos(this).toDegrees().keep6()//acos计算，返回度
fun Float.toAcos() = acos(this).toDegrees().keep6()//acos计算，返回度
fun Double.toAtan() = atan(this).toDegrees().keep6()//atan计算，返回度
fun Float.toAtan() = atan(this).toDegrees().keep6()//atan计算，返回度