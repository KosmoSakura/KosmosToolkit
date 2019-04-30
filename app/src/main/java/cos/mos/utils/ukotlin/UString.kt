package cos.mos.utils.ukotlin

import android.graphics.Paint
import android.text.Html
import android.text.Spanned
import android.widget.TextView

/**
 * @Description:字符处理类
 * @Author: Kosmos
 * @Date: 2019.04.30 19:34
 * @Email: KosmoSakura@gmail.com
 */
object UString {
    internal var aa: Array<String>? = null
    internal var bb: IntArray? = null
    /**
     * @param str "1967+2356-433*12/66"
     * @return [1967], [2356], [433], [12], [66]
     */
    fun split1(str: String): List<String> = str.split("+", "-", "*", "/")

    /**
     * 效果同上
     * */
    fun split2(str: String): List<String> = str.split(Regex("[+\\-*/]"))

    /**
     *@param str "5+3/2*3*0.5“
     * @return 6.0
     * */
    fun calculat(str: String): Float {
        var text = str
        var diff = 1f
        if ("+-/*".contains(str[0])) {
            if (str[0] == '-') {
                diff = -1f
            }
            text = str.substring(1, str.length)
        }
        val symbol = text.split(Regex("[0-9.]")).filter { it != "" }
        val digits = text.split(Regex("[+\\-*/]")).filter { it != "" }
        var calculat = 0f
        //5 6 2 1
        for (index in digits.indices) {
            if (index == 0) {
                calculat = digits[index].toFloat() * diff
            } else {
                when (symbol[index - 1]) {
                    "+" -> {
                        calculat += digits[index].toFloat()
                    }
                    "-" -> {
                        calculat -= digits[index].toFloat()
                    }
                    "*" -> {
                        calculat *= digits[index].toFloat()
                    }
                    "/" -> {
                        calculat /= digits[index].toFloat()
                    }
                }
            }
        }
        return calculat
    }

    /**
     * 给字符添加下划线
     */
    @JvmStatic
    fun setTextUnderLine(tv: TextView) {
        tv.paint.flags = Paint.UNDERLINE_TEXT_FLAG//下划线
        tv.paint.isAntiAlias = true//抗锯齿
    }

    /**
     * @return 给字符添加下划线
     */
    @JvmStatic
    fun getTextUnderLine(str: String): Spanned = Html.fromHtml("<u>$str</u>")

    /**
     * @return 文本加粗
     */
    @JvmStatic
    fun getTextBold(str: String): Spanned = Html.fromHtml("<b>$str</b>")

    /**
     * @return 文本斜体
     */
    @JvmStatic
    fun getTextItalic(str: String): Spanned = Html.fromHtml("<i>$str</i>")
}