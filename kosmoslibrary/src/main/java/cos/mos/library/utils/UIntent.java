package cos.mos.library.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import cos.mos.library.init.KApp;

/**
 * @Description: 各中页面跳转
 * @Author: Kosmos
 * @Date: 2018.12.17 15:16
 * @Email: KosmoSakura@gmail.com
 */
public class UIntent {
    /**
     * 去系统授权页面
     * 普通权限
     */
    public static void goSys() {
        Uri packageURI = Uri.parse("package:" + KApp.getInstance().getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            packageURI);
        KApp.getInstance().startActivity(intent);
    }

    /**
     * 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限
     */
    public static void goSystemAdvanced() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        KApp.getInstance().startActivity(intent);
    }

    /**
     * 回到桌面
     */
    public static void goHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        KApp.getInstance().startActivity(homeIntent);
    }
}
