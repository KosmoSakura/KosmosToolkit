package cos.mos.utils.ukotlin

import android.graphics.Paint
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.widget.TextView

/**
 * @Description:字符工具类
 * @Author: Kosmos
 * @Date: 2019.04.23 13:25
 * @Email: KosmoSakura@gmail.com
 */
object UText {
    /**
     * isNull: 校验为空返回指定字符，默认""
     */
    fun isNull(tv: TextView?, defaul: String = ""): String {
        return if (tv != null) {
            if (UText.isEmpty(tv.text)) defaul else tv.text.toString()
        } else defaul
    }

    fun isNull(str: String?, defaul: String = ""): String {
        return if (str != null) {
            if (UText.isEmpty(str)) defaul else str
        } else {
            return defaul
        }
    }

    fun isNull(str: Editable?, defaul: String = ""): String {
        return if (str != null) {
            if (UText.isEmpty(str)) defaul else str.toString()
        } else {
            return defaul
        }
    }

    fun isNull(str: CharSequence?, defaul: String = ""): String {
        return if (str != null) {
            if (isEmpty(str)) defaul else str.toString()
        } else defaul
    }

    fun isNull(digit: Double?, defaul: Double = -1.0): Double = digit ?: defaul
    fun isNull(digit: Float?, defaul: Float = -1f): Float = digit ?: defaul
    fun isNull(digit: Long?, defaul: Long = -1L): Long = digit ?: defaul
    fun isNull(digit: Int?, defaul: Int = -1): Int = digit ?: defaul
    fun isNull(digit: Short?, defaul: Short = -1): Short = digit ?: defaul
    fun isNull(digit: Byte?, defaul: Byte = -1): Byte = digit ?: defaul

    /**
     * isEmpty: 字符是否为空(只有空格也为空  字符为null或长度为0均判定为空)
     * */
    fun isEmpty(digit: Boolean?, defaul: Boolean = false): Boolean = digit ?: defaul

    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty() || str.trim() == "" || str.equals("null", ignoreCase = false)
    }

    fun isEmpty(tv: TextView?): Boolean {
        return if (tv == null) true else UText.isEmpty(tv.text.toString())
    }

    fun isEmpty(sequence: CharSequence?): Boolean {
        return sequence == null || sequence.isEmpty()
    }

    fun isEmpty(list: List<Any>?): Boolean {
        return list?.isEmpty() ?: true
    }

    /**
     * 给字符添加下划线
     */
    fun setTextUnderLine(tv: TextView) {
        tv.paint.flags = Paint.UNDERLINE_TEXT_FLAG//下划线
        tv.paint.isAntiAlias = true//抗锯齿
    }

    /**
     * @return 给字符添加下划线
     */
    fun getTextUnderLine(str: String): Spanned = Html.fromHtml("<u>$str</u>")
    /**
     * @return 文本加粗
     */
    fun getTextBold(str: String): Spanned = Html.fromHtml("<b>$str</b>")
    /**
     * @return 文本斜体
     */
    fun getTextItalic(str: String): Spanned = Html.fromHtml("<i>$str</i>")
}