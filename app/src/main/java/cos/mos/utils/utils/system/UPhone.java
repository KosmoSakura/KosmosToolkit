package cos.mos.utils.utils.system;

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
import android.os.Environment;
import android.os.LocaleList;
import android.os.StatFs;
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
import java.util.concurrent.Callable;

import cos.mos.utils.utils.java.UText;

/**
 * @Description: 手机相关信息
 * @Author: Kosmos
 * @Date: 2019.04.08 13:50
 * @Email: KosmoSakura@gmail.com
 */
public class UPhone {

    /**
     * @return 手机ip地址
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
                                return inetAddress.getHostAddress();
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
                return intToStrIP(wifiManager.getConnectionInfo().getIpAddress());//得到IPV4地址
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
            return UText.isNull(Build.SERIAL );
        } else {
            if (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return Build.getSerial();
            } else {
                return Build.SERIAL;
            }
        }
    }

    /**
     * @return 获取手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * @return 获取手机型号
     */
    public static String getSystemModel() {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Build.MODEL;
            }
        }));
    }

    public static String getSystemLanguage() {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Locale locale = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    locale = LocaleList.getDefault().get(0);
                else
                    locale = Locale.getDefault();
                return locale.getLanguage() + "-" + locale.getCountry();
            }
        }));
    }


    public static String getCurrentTimeZone() {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeZone tz = TimeZone.getDefault();
                String strTz = tz.getDisplayName(false, TimeZone.SHORT);
                return strTz;
            }
        }));
    }

    /**
     * @return 获取当前手机系统版本号
     */
    public static String getSystemVersion() {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Build.VERSION.RELEASE;
            }
        }));
    }

    public static String getVersionName(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String versionName = "1.0.0";
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    versionName = packageInfo.versionName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return versionName;
            }
        }));
    }

    public static String getApkVersion(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            }
        }));
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
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }));
    }

    public static boolean isHasSimCard(final Context context) {
        Boolean result = safetyFunction(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                int simState = telMgr.getSimState();
                boolean result = true;
                switch (simState) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        result = false;
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        result = false;
                        break;
                }
                return result;
            }
        });
        return result != null && result;
    }

    /**
     * @param context
     * @return 存储空间
     */
    public static String getRamTotalMemory(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String path = Environment.getDataDirectory().getPath();
                StatFs statFs = new StatFs(path);
                long blockSize = statFs.getBlockSize();
                long totalBlocks = statFs.getBlockCount();
                long rom_length = totalBlocks * blockSize;
                return String.valueOf(rom_length) + "byte";
            }
        }));
    }

    /**
     * @param context
     * @return 运行内存
     */
    public static String getRomTotalMemory(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    boolean isStart = false;
                    FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
                    String memTotal = bufferedReader.readLine();
                    StringBuffer sb = new StringBuffer();
                    for (char c : memTotal.toCharArray()) {
                        if (c >= '0' && c <= '9')
                            isStart = true;
                        if (isStart && c != ' ')
                            sb.append(c);
                    }
                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }));
    }

    public static String getBatteryTemperature(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                return intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 + "";
            }
        }));
    }

    public static String getBatteryLevel(final Context context) {
        return wrapString(safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) + "";
                } else {
                    Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) + "";
                }
            }
        }));
    }

    public static String getBatteryStatus(final Context context) {
        String result = safetyFunction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                return BatteryManager.BATTERY_STATUS_CHARGING == status ? "1" : "0";
            }
        });
        return result == null ? "0" : result;
    }

    private static String wrapString(String source) {
        return UText.isEmpty(source) ? "-2" : source;
    }

    private static <T> T safetyFunction(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
