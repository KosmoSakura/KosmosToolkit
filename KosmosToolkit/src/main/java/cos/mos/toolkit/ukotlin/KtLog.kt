package cos.mos.toolkit.ukotlin

import android.util.Log

/**
 * @Description:打日志
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 11:21
 * @Email: KosmoSakura@gmail.com
 */
object KtLog {
    @JvmStatic
    fun commonD(str: String,tag: String = "Kosmos") {
        Log.d(tag, str)
    }

    @JvmStatic
    fun commonV(str: String, tag: String = "Kosmos") {
        Log.v(tag, str)
    }

    @JvmStatic
    fun commonE(str: String,tag: String = "Kosmos") {
        Log.e(tag, str)
    }

    @JvmStatic
    fun commonW(str: String,tag: String = "Kosmos") {
        Log.w(tag, str)
    }

    @JvmStatic
    fun commonI(str: String,tag: String = "Kosmos") {
        Log.i(tag, str)
    }
}