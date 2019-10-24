package cos.mos.utils.ukotlin

import android.text.Editable
import android.widget.TextView

/**
 * @Description 字符校验类
 * @Author Kosmos
 * @Date 2019.04.23 13:25
 * @Email KosmoSakura@gmail.com
 * @Tip 2019-10-24 优化空判断显示
 * */
object KtText {
    @JvmStatic
    fun isNull(str: String?, defaul: String = "") = str ?: defaul

    @JvmStatic
    fun isNull(charSequence: CharSequence?, defaul: String = "") = if (charSequence == null) defaul else isNull(charSequence.toString())

    @JvmStatic
    fun isNull(textView: TextView?, defaul: String = "") = if (textView == null) defaul else isNull(textView.text)

    @JvmStatic
    fun isNull(editable: Editable?, defaul: String = "") = if (editable == null) defaul else isNull(editable.toString())

    @JvmStatic
    fun isNull(digit: Double?, defaul: Double = -1.0): Double = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Float?, defaul: Float = -1f): Float = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Long?, defaul: Long = -1L): Long = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Int?, defaul: Int = -1): Int = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Short?, defaul: Short = -1): Short = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Byte?, defaul: Byte = -1): Byte = digit ?: defaul

    @JvmStatic
    fun isEmpty(digit: Boolean?, defaul: Boolean = false): Boolean = digit ?: defaul

    @JvmStatic
    fun isEmpty(str: String?) = str?.length == 0

    @JvmStatic
    fun isEmpty(sequence: CharSequence?) = sequence?.length == 0

    @JvmStatic
    fun isEmpty(textView: TextView?) = textView?.length() == 0

    @JvmStatic
    fun isEmpty(list: List<Any>?) = list?.isEmpty() ?: true
}