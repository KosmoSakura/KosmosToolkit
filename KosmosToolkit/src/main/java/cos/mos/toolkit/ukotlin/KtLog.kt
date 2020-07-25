package cos.mos.toolkit.ukotlin

import android.util.Log
import cos.mos.toolkit.constant.Code

/**
 * @Description:打日志
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 11:21
 * @Email: KosmoSakura@gmail.com
 */
object KtLog {
    @JvmStatic
    fun commonD(str: String, tag: String = "Kosmos") {
        if (Code.IsDebug) Log.d(tag, str)
    }

    @JvmStatic
    fun commonV(str: String, tag: String = "Kosmos") {
        if (Code.IsDebug) Log.v(tag, str)
    }

    @JvmStatic
    fun commonE(str: String, tag: String = "Kosmos") {
        if (Code.IsDebug) Log.e(tag, str)
    }

    @JvmStatic
    fun commonW(str: String, tag: String = "Kosmos") {
        if (Code.IsDebug) Log.w(tag, str)
    }

    @JvmStatic
    fun commonI(str: String, tag: String = "Kosmos") {
        if (Code.IsDebug) Log.i(tag, str)
    }
}