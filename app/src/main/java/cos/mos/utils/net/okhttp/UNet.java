package cos.mos.utils.net.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cos.mos.utils.init.App;

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
    private Context cont;
    private ConnectivityManager netMgr;

    private UNet(Context context) {
        this.cont = context;
    }

    public static UNet instance(Context context) {
        if (net == null) {
            net = new UNet(context);
        }
        return net;
    }

    public static UNet instance() {
        if (net == null) {
            net = new UNet(null);
        }
        return net;
    }

    private ConnectivityManager mgr() {
        if (netMgr == null) {
            if (cont == null) {
                netMgr = (ConnectivityManager) App.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
            } else {
                netMgr = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
            }
        }
        return netMgr;
    }

    /**
     * @return 当前手机是否有正确连接网络
     */
    public boolean WhetherConnectNet(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mgr != null && mgr.getActiveNetworkInfo() != null)
            return mgr.getActiveNetworkInfo().isAvailable();
        return false;
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
