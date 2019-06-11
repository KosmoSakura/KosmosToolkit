package cos.mos.toolkit.system;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import cos.mos.toolkit.java.UText;

/**
 * @Description: 手机相关信息
 * @Author: Kosmos
 * @Date: 2019.04.08 13:50
 * @Email: KosmoSakura@gmail.com
 * 需要权限：<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
public class UPhone {

    /**
     * @return 手机ip地址
     * 权限：android.permission.ACCESS_NETWORK_STATE
     */
    public static String getIPAddress(final Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            //当前使用2G/3G/4G网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return UText.isNull(inetAddress.getHostAddress());
                            }
                        }
                    }
                } catch (SocketException e) {
                    return "";
                }
                return "";
            }//当前使用无线网络
            else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
                return UText.isNull(intToStrIP(wifiManager.getConnectionInfo().getIpAddress()));//得到IPV4地址
            } else {
                return "";
            }
        } else {
            //当前无网络连接
            return "";
        }
    }

    /**
     * @return 手机是否已有root权限
     */
    public static boolean isDeviceRooted() {
        String buildTags = Build.TAGS;
        boolean rs1 = buildTags != null && buildTags.contains("test-keys");
        if (rs1) {
            return true;
        }

        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * @return 获取SerialNumber
     * @apiNote 需要权限
     * android.permission.READ_PHONE_STATE
     */
    public static String getSerialNumber(final Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return UText.isNull(Build.SERIAL);
        } else {
            if (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return UText.isNull(Build.getSerial());
            } else {
                return UText.isNull(Build.SERIAL);
            }
        }
    }

    /**
     * @return 获取手机厂商
     */
    public static String getDeviceBrand() {
        return UText.isNull(Build.BRAND);
    }

    /**
     * @return 获取手机型号
     */
    public static String getSystemModel() {
        return UText.isNull(Build.MODEL);
    }

    /**
     * @return 获取系统语言
     */
    public static String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            locale = LocaleList.getDefault().get(0);
        else
            locale = Locale.getDefault();
        return locale.getLanguage() + "-" + locale.getCountry();
    }


    /**
     * @return 获取当前时区
     */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        if (tz == null) {
            return "";
        }
        return UText.isNull(tz.getDisplayName(false, TimeZone.SHORT));
    }

    /**
     * @return 获取当前手机系统版本号
     */
    public static String getSystemVersion() {
        return UText.isNull(Build.VERSION.RELEASE);
    }

    /**
     * @return 获取当前应用版本号
     */
    public static String getVersionName(final Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return UText.isNull(packageInfo.versionName, "1.0.0");
        } catch (Exception e) {
            return "1.0.0";
        }
    }


    /**
     * @param ip Ip值
     * @return int类型的IP转换为String类型
     */
    public static String intToStrIP(final int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." +
            ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
    }

    public static String getAndroidId(final Context context) {
        return UText.isNull(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    /**
     * @return 手机是否安装了Sim卡
     */
    public static boolean isHasSimCard(final Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telMgr == null) {
            return false;
        }
        switch (telMgr.getSimState()) {
            case TelephonyManager.SIM_STATE_ABSENT:
                return false;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return false;
            default:
                return true;
        }
    }

    /**
     * @return 运行内存
     */
    public static String getRomTotalMemory(final Context context) {
        try {
            boolean isStart = false;
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String memTotal = bufferedReader.readLine();
            StringBuilder sb = new StringBuilder();
            for (char c : memTotal.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    isStart = true;
                }
                if (isStart && c != ' ') {
                    sb.append(c);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return 电池温度(摄氏度)
     */
    public static int getBatteryTemperature(final Context context) {
        Intent intent = new ContextWrapper(context)
            .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (intent == null) {
            return 0;
        }
        return intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
    }

    /**
     * @return 电池等级（失败返回-1）
     */
    public static int getBatteryLevel(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            if (batteryManager == null) {
                return -1;
            }
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (intent == null) {
                return -1;
            }
            return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
    }

    /**
     * @return 电池充电状态
     * @apiNote 返回值==下面↓
     * 充电状态:BatteryManager.BATTERY_STATUS_CHARGING
     * 放电状态:BatteryManager.BATTERY_STATUS_DISCHARGING
     * 未充电:BatteryManager.BATTERY_STATUS_NOT_CHARGING
     * 充满电:BatteryManager.BATTERY_STATUS_FULL
     * 未知状态:BatteryManager.BATTERY_STATUS_UNKNOWN
     */
    public static int getBatteryStatus(final Context context) {
        Intent intent = new ContextWrapper(context)
            .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (intent == null) {
            return 0;
        }
        return intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
    }

}
