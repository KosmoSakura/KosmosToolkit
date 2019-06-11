package cos.mos.utils.net.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

import cos.mos.utils.initial.App;

/**
 * @Description: 网络状态
 * @Author: Kosmos
 * @Date: 2019.05.06 10:09
 * @Email: KosmoSakura@gmail.com
 * 权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class UNet {
    private static UNet net;
    private WeakReference<Context> ref;
    private ConnectivityManager netMgr;

    private UNet() {

    }

    public static UNet instance(Context context) {
        if (net == null) {
            net = new UNet();
        }
        net.ref = new WeakReference<>(context);
        return net;
    }

    public static UNet instance() {
        if (net == null) {
            net = new UNet();
        }
        return net;
    }

    private ConnectivityManager mgr() {
        if (netMgr == null) {
            if (ref != null && ref.get() != null) {
                netMgr = (ConnectivityManager) ref.get().getSystemService(Context.CONNECTIVITY_SERVICE);
                ref.clear();
            } else {
                netMgr = (ConnectivityManager) App.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
            }
        }
        return netMgr;
    }

    /**
     * @return 当前是否为有网络可以使用（链接）
     */
    public boolean isNetCanlinked() {
        NetworkInfo info = mgr().getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * @return 网络是否连接
     */
    public boolean isNetConnected() {
        NetworkInfo info = mgr().getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * @return wifi是否连接
     */
    public boolean isWifiConnected() {
        NetworkInfo networkInfo = mgr().getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * @return 移动网络是否连接
     */
    public boolean isMobileConnected() {
        NetworkInfo networkInfo = mgr().getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected();
    }

}
