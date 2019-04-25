package cos.mos.utils.ukotlin

import android.util.Log

/**
 * @Description:打日志
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 11:21
 * @Email: KosmoSakura@gmail.com
 */
object UKLog {
    fun commonD(str: String,tag: String = "Kosmos") {
        Log.d(tag, str)
    }

    fun commonV(str: String, tag: String = "Kosmos") {
        Log.v(tag, str)
    }

    fun commonE(str: String,tag: String = "Kosmos") {
        Log.e(tag, str)
    }

    fun commonW(str: String,tag: String = "Kosmos") {
        Log.w(tag, str)
    }

    fun commonI(str: String,tag: String = "Kosmos") {
        Log.i(tag, str)
    }
}