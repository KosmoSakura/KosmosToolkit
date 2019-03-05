package cos.mos.utils.utils.hardware;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import cos.mos.utils.utils.java.UText;
import cos.mos.utils.init.App;

/**
 * @Description: wifi工具类
 * @Author: Kosmos
 * @Date: 2018.11.23 15:12
 * @Email: KosmoSakura@gmail.com
 * @eg: 最新修改日期：2018年11月25日 17:19
 */
public class UWiFi {
    private static WifiManager wifiMgr;
    private static ConnectivityManager connectivityMgr;


    private static WifiManager getWifiMgr() {
        if (wifiMgr == null) {
            wifiMgr = (WifiManager) App.instance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        return wifiMgr;
    }

    private static ConnectivityManager getConnectivityMgr() {
        if (connectivityMgr == null) {
            connectivityMgr = (ConnectivityManager) App.instance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityMgr;
    }

    /**
     * 通过WifiManager获取ssid
     * 这种方式获取 需要2个权限
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     * Manifest.permission.ACCESS_FINE_LOCATION
     * Manifest.permission.ACCESS_COARSE_LOCATION
     */
    public static String getSSIDWithManager() {
        WifiInfo info = getWifiMgr().getConnectionInfo();
        String ssid = info == null ? "" : info.getSSID();
        if (UText.isEmpty(ssid)) {
            ssid = "";
        } else {
            ssid = ssid.replace("\"", "");
        }
        return ssid;
    }

    /**
     * 通过ConnectivityManager获取ssid
     * 不需要权限
     */
    public static String getSSID() {
        NetworkInfo networkInfo = getConnectivityMgr().getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String ssid = networkInfo.getExtraInfo();
        if (UText.isEmpty(ssid)) {
            ssid = "";
        } else {
            ssid = ssid.replace("\"", "");
        }
        return ssid;
    }

    /**
     * 打开wifi并连接，常规情况
     */
    public static void toLinkedWifi(String ssid, String password) {
        //wifi未打开 执行打开操作
        if (!getWifiMgr().isWifiEnabled()) {
            getWifiMgr().setWifiEnabled(true);
        }
        WifiConfiguration localWifiConfiguration = new WifiConfiguration();
        //公认的IEEE 802.11验证算法。
        localWifiConfiguration.allowedAuthAlgorithms.clear();
        localWifiConfiguration.allowedAuthAlgorithms.set(0);
        //公认的的公共组密码。
        localWifiConfiguration.allowedGroupCiphers.clear();
        localWifiConfiguration.allowedGroupCiphers.set(2);
        //公认的密钥管理方案。
        localWifiConfiguration.allowedKeyManagement.clear();
        localWifiConfiguration.allowedKeyManagement.set(1);
        //密码为WPA。
        localWifiConfiguration.allowedPairwiseCiphers.clear();
        localWifiConfiguration.allowedPairwiseCiphers.set(1);
        localWifiConfiguration.allowedPairwiseCiphers.set(2);
        //公认的安全协议。
        localWifiConfiguration.allowedProtocols.clear();
        localWifiConfiguration.SSID = ("\"" + ssid + "\"");
        localWifiConfiguration.preSharedKey = ("\"" + password + "\"");
        //不广播其SSID的网络
        localWifiConfiguration.hiddenSSID = true;
        //添加一个网络并连接
        int wcgID = getWifiMgr().addNetwork(localWifiConfiguration);
        getWifiMgr().enableNetwork(wcgID, true);
    }

    /**
     * 打开wifi并连接，区分加密方式
     * 热点的加密分为三种情况：
     * 1-没有密码
     * 2-用wep加密
     * 3-用wpa加密
     */
    public static void toLinkedWifi(int type, String ssid, String password) {
        //wifi未打开 执行打开操作
        if (!getWifiMgr().isWifiEnabled()) {
            getWifiMgr().setWifiEnabled(true);
        }
        WifiConfiguration config = new WifiConfiguration();
        //清理配置
        config.allowedAuthAlgorithms.clear();//公认的IEEE 802.11验证算法。
        config.allowedGroupCiphers.clear();//公认的的公共组密码
        config.allowedKeyManagement.clear();//公认的密钥管理方案
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();//公认的安全协议

        //开始配置
        config.SSID = "\"" + ssid + "\"";
        //没有密码
        if (type == 1) {
            //连接无密码热点时加上这两句会出错
            /* config.wepKeys[0] = "";
             config.wepTxKeyIndex = 0;*/
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //用wep加密
        if (type == 2) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        //用wpa加密
        if (type == 3) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        //添加一个网络并连接
        int wcgID = getWifiMgr().addNetwork(config);
        getWifiMgr().enableNetwork(wcgID, true);
    }

    /**
     * @apiNote 跳转到wifi设置页面
     */
    public static void jumpToWifiSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
        intent.putExtra("extra_prefs_show_button_bar", true);
        intent.putExtra("wifi_enable_next_on_connect", true);
        activity.startActivity(intent);
    }
}
