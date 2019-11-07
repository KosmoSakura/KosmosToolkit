package cos.mos.utils.ukotlin

import android.text.Editable
import android.widget.TextView

/**
 * @Description 字符校验类
 * @Author Kosmos
 * @Date 2019.04.23 13:25
 * @Email KosmoSakura@gmail.com
 * @Tip 优化空判断显示:2019.10.24
 * @Tip 优化判断逻辑执行效率：2019.10.25
 * */
object KtText {
    @JvmStatic
    fun isNull(str: String?, defaul: String = "") = if (str.isNullOrEmpty()) defaul else str

    @JvmStatic

    fun isNull(charSequence: CharSequence?, defaul: String = "") = if (isEmpty(charSequence)) defaul else isNull(charSequence.toString())

    @JvmStatic
    fun isNull(textView: TextView?, defaul: String = "") = if (textView == null) defaul else isNull(textView.text)

    @JvmStatic
    fun isNull(editable: Editable?, defaul: String = "") = if (editable == null) defaul else isNull(editable.toString())

    @JvmStatic
    fun isNull(digit: Double?, defaul: Double = 0.0) = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Float?, defaul: Float = 0f) = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Long?, defaul: Long = 0L) = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Int?, defaul: Int = 0) = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Short?, defaul: Short = 0) = digit ?: defaul

    @JvmStatic
    fun isNull(digit: Byte?, defaul: Byte = 0) = digit ?: defaul

    //--------------------------------------------------------------------------------------------------------------
    @JvmStatic
    fun isEmpty(digit: Boolean?, defaul: Boolean = false) = digit ?: defaul

    @JvmStatic
    fun isEmpty(str: String?) = str == null || str.isEmpty() || str.trim().isEmpty() || str.equals("null", ignoreCase = true)

    @JvmStatic
    fun isEmpty(sequence: CharSequence?) = isEmpty(sequence.toString())

    @JvmStatic
    fun isEmpty(textView: TextView?) = textView == null || isEmpty(textView.text)

    @JvmStatic
    fun isEmpty(list: List<Any?>?) = list == null || list.isEmpty()

    //--------------------------------------------------------------------------------------------------------------
    @JvmStatic
    fun isNotEmpty(str: String?) = str != null && str.isNotEmpty() && str.trim().isNotEmpty() && !str.equals("null", ignoreCase = true)

    @JvmStatic
    fun isNotEmpty(sequence: CharSequence?) = isNotEmpty(sequence.toString())

    @JvmStatic
    fun isNotEmpty(textView: TextView?) = textView != null && isNotEmpty(textView.text)

    @JvmStatic
    fun isNotEmpty(list: List<Any?>?) = list == null || list.isNotEmpty()

    //------------------------------------------------------------------------------------------------

    /**
     * @return 返回str长度，为空返回指定值
     */
    fun getLength(str: String, defaul: Int = 0) = if (isEmpty(str)) defaul else str.length

    fun getLength(tv: TextView, defaul: Int = 0) = if (isEmpty(tv)) defaul else tv.length()
    fun getLength(arr: List<Any>?) = arr?.size ?: 0
}