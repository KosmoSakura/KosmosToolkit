package cos.mos.toolkit.ukotlin

import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
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
    fun isNull(charSequence: CharSequence?, defaul: String = "") = if (isEmpty(charSequence)) defaul else charSequence

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
    fun isTrue(digit: Boolean?, defaul: Boolean = false) = digit ?: defaul

    @JvmStatic
    fun isEmpty(str: CharSequence?) = str == null || str.isEmpty() || str.trim().isEmpty() || str == "null"

    @JvmStatic
    fun isEmpty(textView: TextView?) = textView == null || isEmpty(textView.text)

    @JvmStatic
    fun isEmpty(list: List<Any?>?) = list == null || list.isEmpty()

    //--------------------------------------------------------------------------------------------------------------
    @JvmStatic
    fun isNotEmpty(str: CharSequence?) = str != null && str.isNotEmpty() && str.trim().isNotEmpty() && str != "null"

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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun isActNull(act: Activity?): Boolean {
        return act == null || act.isFinishing || act.isDestroyed
    }
}