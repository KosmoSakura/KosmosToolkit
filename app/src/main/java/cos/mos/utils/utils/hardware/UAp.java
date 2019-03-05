package cos.mos.utils.utils.hardware;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Method;

import cos.mos.utils.init.k.KApp;
import cos.mos.utils.utils.ULogBj;

/**
 * @Description: wifi热点工具类
 * @Author: Kosmos
 * @Date: 2018.11.26 18:08
 * @Email: KosmoSakura@gmail.com
 * * 通过WifiManager获取ssid
 * * 这种方式获取 需要2个权限
 * * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * * Manifest.permission.ACCESS_FINE_LOCATION
 * * Manifest.permission.ACCESS_COARSE_LOCATION
 * 最新修改日期：2018年11月25日 17:19
 */
public class UAp {
    private static UAp instance;
    private WifiManager wifiMgr;
    private WifiManager.LocalOnlyHotspotReservation mReservation;

    private UAp() {
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
     * @return wifi热点的ssid
     */
    public String getApSSID() {
        try {
            Method method = getWifiMgr().getClass().getDeclaredMethod("getWifiApConfiguration");
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(getWifiMgr());
            ULogBj.commonD("热点名：" + configuration.SSID);
            ULogBj.commonD("热点密码：" + configuration.preSharedKey);
            return configuration.SSID;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return wifi热点的password
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
    public boolean getApOpen() {
        if (isSystemO()) {
            return mReservation != null;
        } else {
            try {
                //反射获取 getWifiApState()方法
                Method method = getWifiMgr().getClass().getDeclaredMethod("isWifiApEnabled");
                method.setAccessible(true);
                return (boolean) method.invoke(getWifiMgr());
            } catch (Throwable ignored) {
                return false;
            }
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
        } catch (Throwable ignored) {
        }
    }

    /**
     * @apiNote 打开热点
     */
    public void openAp(String ssid, String password, Callbak callbak) {
        if (isSystemO()) {
            getWifiMgr().startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                @Override
                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                    mReservation = reservation;
                    callbak.onConnected(reservation.getWifiConfiguration().SSID, reservation.getWifiConfiguration().preSharedKey);
                }

                @Override
                public void onStopped() {
                    mReservation = null;
                }

                @Override
                public void onFailed(int reason) {
                }
            }, new Handler());
        } else {
            try {
                WifiConfiguration wificonfiguration = new WifiConfiguration();
                wificonfiguration.SSID = ssid;
                wificonfiguration.preSharedKey = password;
//                wificonfiguration.status = WifiConfiguration.Status.ENABLED;
//                wificonfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wificonfiguration.allowedKeyManagement.set(4);
                Method method = getWifiMgr().getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                Boolean result = (Boolean) method.invoke(getWifiMgr(), wificonfiguration, true);
                if (result != null && result) {
                    callbak.onConnected(ssid, password);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isSystemO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public interface Callbak {
        void onConnected(String ssid, String password);
    }
}
