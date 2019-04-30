package cos.mos.utils.ukotlin

import android.text.Editable
import android.widget.TextView

/**
 * @Description:字符校验类
 * @Author: Kosmos
 * @Date: 2019.04.23 13:25
 * @Email: KosmoSakura@gmail.com
 */
object UKText {
    /**
     * isNull: 校验为空返回指定字符，默认""
     */
    @JvmStatic
    fun isNull(tv: TextView?, defaul: String = ""): String {
        return if (tv != null) {
            if (UKText.isEmpty(tv.text)) defaul else tv.text.toString()
        } else defaul
    }

    @JvmStatic
    fun isNull(str: String?, defaul: String = ""): String {
        return if (str != null) {
            if (UKText.isEmpty(str)) defaul else str
        } else {
            return defaul
        }
    }

    @JvmStatic
    fun isNull(str: Editable?, defaul: String = ""): String {
        return if (str != null) {
            if (UKText.isEmpty(str)) defaul else str.toString()
        } else {
            return defaul
        }
    }

    @JvmStatic
    fun isNull(str: CharSequence?, defaul: String = ""): String {
        return if (str != null) {
            if (isEmpty(str)) defaul else str.toString()
        } else defaul
    }

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

    /**
     * isEmpty: 字符是否为空(只有空格也为空  字符为null或长度为0均判定为空)
     * */
    @JvmStatic
    fun isEmpty(digit: Boolean?, defaul: Boolean = false): Boolean = digit ?: defaul

    @JvmStatic
    fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty() || str.equals("null", ignoreCase = false)
    }

    @JvmStatic
    fun isEmpty(tv: TextView?): Boolean {
        return if (tv == null) true else UKText.isEmpty(tv.text.toString())
    }

    @JvmStatic
    fun isEmpty(sequence: CharSequence?): Boolean {
        return sequence == null || sequence.isEmpty()
    }

    @JvmStatic
    fun isEmpty(list: List<Any>?): Boolean {
        return list?.isEmpty() ?: true
    }
}