package cos.mos.toolkit.hardware;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Method;

import cos.mos.utils.init.k.KApp;
import cos.mos.toolkit.ULog;
import cos.mos.toolkit.java.UText;

/**
 * @Description: wifi热点工具类
 * @Author: Kosmos
 * @Date: 2018.11.26 18:08
 * @Email: KosmoSakura@gmail.com
 * * 通过WifiManager获取ssid
 * * 这种方式获取 需要2个权限
 * * * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * * * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * * * Manifest.permission.ACCESS_FINE_LOCATION
 * * * Manifest.permission.ACCESS_COARSE_LOCATION
 * 打开wifi需要权限
 * * * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * * * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * * * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * 最新修改日期：2018年11月25日 17:19
 * @eg 2018.11.25 优化华为手机热点打开
 * @eg 2019.3.21 重构
 */
public class UAp {
    private static UAp instance;
    private String sid, pwd;
    private WifiManager wifiMgr;
    private WifiManager.LocalOnlyHotspotReservation mReservation;

    private UAp() {
        sid = "";
        pwd = "";
    }

    private WifiManager getWifiMgr() {
        if (wifiMgr == null) {
            wifiMgr = (WifiManager) KApp.instance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        return wifiMgr;
    }

    public static UAp getsInstance() {
        synchronized (UAp.class) {
            if (instance == null) {
                instance = new UAp();
            }
            return instance;
        }
    }

    /**
     * @return 低于Android8的wifi热点的ssid
     */
    public String getApSSID() {
        try {
            Method method = getWifiMgr().getClass().getDeclaredMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(getWifiMgr());
            return configuration.SSID;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return 低于Android8的wifi热点的password
     */
    public String getApPassword() {
        try {
            Method method = getWifiMgr().getClass().getDeclaredMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(getWifiMgr());
            return configuration.preSharedKey;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return 获取热点状态
     */
    public int getApState() {
        try {
            //通过放射获取 getWifiApState()方法
            Method method = getWifiMgr().getClass().getDeclaredMethod("getWifiApState");
            //调用getWifiApState() ，获取返回值
            return (int) method.invoke(getWifiMgr());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @return 热点是否打开 {@code true} 打开
     */
    public boolean isApOpen() {
        try {
            //反射获取 getWifiApState()方法
            Method method = getWifiMgr().getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(getWifiMgr());
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * 关闭热点
     */
    public void closeAp() {
        try {
            if (isSystemO()) {
                if (mReservation != null) {
                    mReservation.close();
                    mReservation = null;
                }
            } else {
                Method method = getWifiMgr().getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
                method.invoke(getWifiMgr(), null, false);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * @apiNote 打开热点
     */
    public void openAp(Callbak callbak) {
        if (isSystemO()) {
            openApSystemO(callbak);
        } else {
            openApBelow8(callbak);
        }
    }

    /**
     * 高于Android8.0 开启热点
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openApSystemO(Callbak callbak) {
        if (isApOpen()) {
            if (!UText.isEmpty(sid) && !UText.isEmpty(pwd)) {
                callbak.onConnected("", sid, pwd);
                return;
            }
            //如果是自己开的可以关
            closeAp();
        }
        //关不掉，让用户关
        if (UAp.getsInstance().isApOpen()) {
            ULog.commonD("热点关闭失败");
            callbak.onConnected("close wifi ap first please", null, null);
            return;
        }
        getWifiMgr().startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                mReservation = reservation;
                sid = reservation.getWifiConfiguration().SSID;
                pwd = reservation.getWifiConfiguration().preSharedKey;
                callbak.onConnected("", sid, pwd);
            }

            @Override
            public void onStopped() {
                mReservation = null;
                sid = "";
                pwd = "";
            }

            @Override
            public void onFailed(int reason) {
                sid = "";
                pwd = "";
                callbak.onConnected("wifi ap is failed to open", null, null);
            }
        }, new Handler());
    }

    /**
     * 低于Android8.0 开启热点
     */
    private void openApBelow8(Callbak callbak) {
        if (getWifiMgr().isWifiEnabled()) {
            //如果wifi处于打开状态，则关闭wifi,
            getWifiMgr().setWifiEnabled(false);
        }
        if (isApOpen()) {
            sid = getApSSID();
            pwd = getApPassword();
            if (!UText.isEmpty(sid) && !UText.isEmpty(pwd)) {
                callbak.onConnected("", sid, pwd);
                return;
            } else {
                sid = "";
                pwd = "";
            }
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "Scan-Wifi";
        config.preSharedKey = "Scan-Wifi@233";
        config.hiddenSSID = false;//是否隐藏网络
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;
        try {
            Method method = getWifiMgr().getClass().getMethod("setWifiApEnabled",
                WifiConfiguration.class, Boolean.TYPE);
            Boolean result = (Boolean) method.invoke(getWifiMgr(), config, true);
            if (UText.isBoolean(result)) {
                sid = "Scan-Wifi";
                pwd = "Scan-Wifi@233";
                callbak.onConnected("", sid, pwd);
            } else {
                sid = "";
                pwd = "";
                callbak.onConnected("wifi ap is failed to open", null, null);
            }
        } catch (Exception e) {
            sid = "";
            pwd = "";
            callbak.onConnected("wifi ap is failed to open", null, null);
        }
    }

    private static boolean isSystemO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public interface Callbak {
        void onConnected(String error, String ssid, String password);
    }
}
