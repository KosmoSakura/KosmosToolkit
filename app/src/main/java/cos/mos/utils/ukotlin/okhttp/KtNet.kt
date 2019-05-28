package cos.mos.utils.ukotlin.okhttp

import android.content.Context
import android.net.ConnectivityManager
import cos.mos.utils.init.App
import java.lang.ref.WeakReference

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 17:29
 * @Email: KosmoSakura@gmail.com
 * <uses-cos.mos.utils.ui.permission android:name="android.cos.mos.utils.ui.permission.INTERNET"/>
 * <uses-cos.mos.utils.ui.permission android:name="android.cos.mos.utils.ui.permission.ACCESS_NETWORK_STATE" />
 */
class KtNet private constructor() {

    private var netMgr: ConnectivityManager? = null

    companion object {
        private var ref: WeakReference<Context>? = null
        private var net: KtNet? = null
            get() {
                if (field == null) {
                    field = KtNet()
                }
                return field
            }

        fun instance() = net!!

        fun instance(context: Context): KtNet {
            ref = WeakReference(context)
            return net!!
        }
    }

    private fun mgr(): ConnectivityManager {
        if (netMgr == null) {
            if (ref != null && ref?.get() != null) {
                netMgr = ref!!.get()!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            } else {
                netMgr = App.instance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }
        }
        return netMgr!!
    }

    /**
     * @return 当前是否为有网络可以使用（链接）
     */
    fun isNetCanlinked(): Boolean {
        val info = mgr().activeNetworkInfo
        return info != null && info.isAvailable
    }

    /**
     * @return 网络是否连接
     */
    fun isNetConnected(): Boolean {
        val info = mgr().activeNetworkInfo
        return info != null && info.isConnected
    }

    /**
     * @return wifi是否连接
     */
    fun isWifiConnected(): Boolean {
        val networkInfo = mgr().getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * @return 移动网络是否连接
     */
    fun isMobileConnected(): Boolean {
        val networkInfo = mgr().getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return networkInfo != null && networkInfo.isConnected
    }
}