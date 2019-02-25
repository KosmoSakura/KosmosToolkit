package cos.mos.utils.utils.system;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.fragment.app.FragmentActivity;
import cos.mos.utils.init.k.KApp;
import cos.mos.utils.utils.ui.UDialog;
import cos.mos.utils.utils.java.UText;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: 权限工具，基于RxPermissions
 * @Author: Kosmos
 * @Date: 2018.11.27 11:03
 * @Email: KosmoSakura@gmail.com
 * @eg: 最新修改日期：2019年2月15日 14:00
 */
public class UPermissions {
    private RxPermissions rxPermissions;
    private static CompositeDisposable compositeDisposable;
    private FragmentActivity activity;
    private int count;
    private int maxNoticeCount = 2;//弹窗最大弹出次数

    public UPermissions(FragmentActivity activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
        count = 0;
    }

    /**
     * 清理资源
     */
    public void clear() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * @param max 通知最大弹出次数，默认为2次
     *            当次数为1，则响应式弹出通知
     * @apiNote 用户体验优化
     */
    public UPermissions setMaxNoticeCount(int max) {
        maxNoticeCount = max;
        return this;
    }

    /**
     * @return 是否有悬浮窗权限
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
     */
    public static boolean checkOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(KApp.getInstance());
        }
        return true;
    }

    /**
     * @param pers 权限们
     * @apiNote 校验是否有权限
     */
    public boolean checkOnly(String... pers) {
        for (String per : pers) {
            if (!rxPermissions.isGranted(per)) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param notice   权限申请提示
     * @param listener 权限监听,不需要监听传入null
     * @param pers     权限们
     * @return 已经拥有 权限返回true，只要当前没有被授予权限，返回false（用于流程判断用）
     * @apiNote 权限检查，没有申请
     */
    public boolean check(String notice, Listener listener, String... pers) {
        for (String per : pers) {
            if (!rxPermissions.isGranted(per)) {
                count++;
                UDialog.getInstance(activity, false, false)
                    .showNoticeWithOnebtn(notice, "Agreed", (result, dia) -> {
                        Disposable subscribe = rxPermissions.request(pers)
                            .subscribe(granted -> {
                                if (granted) {
                                    if (listener != null) {
                                        listener.permission(true);
                                    }
                                } else {
                                    if (maxNoticeCount == 1) {
                                        if (listener != null) {
                                            listener.permission(false);
                                        }
                                    } else {
                                        if (count > maxNoticeCount) {
                                            UDialog.getInstance(activity, false, false)
                                                .showNoticeWithOnebtn(notice,
                                                    "To authorize", (result1, dia1) -> {
                                                        UIntent.goSys();
                                                        activity.finish();
                                                    });

                                        } else {
                                            check(notice, listener, pers);
                                        }
                                    }
                                }
                            });
                        dia.dismiss();
                        rxDisposable(subscribe);
                    });
                return false;
            }
        }
        return true;
    }


    private void rxDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public interface Listener {
        void permission(boolean hasPermission);
    }

    /*-以下是特权权限的检测，
       不能申请，只能引导用户手动开，开启方法在UIntent里面
    ------------------------------------------------------------------------------------*/

    /**
     * 辅助功能服务/[无障碍通道]是否开启
     */
    public static boolean isAssistOn() {
        int accessibilityEnabled;//默认为0
        try {
            accessibilityEnabled = Settings.Secure.getInt(KApp.getInstance().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //找不到这个页面
            accessibilityEnabled = 0;
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(KApp.getInstance().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(KApp.getInstance().getPackageName().toLowerCase());
            }
        }
        return false;
    }

    /**
     * @return 查看是否[存在][有权限查看使用情况的应用]权限
     */
    public static boolean isNoOption() {
        PackageManager packageManager = KApp.getInstance().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        return !UText.isEmpty(packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY));
    }

    /**
     * @return 是否已经获取[有权限查看使用情况的应用]权限
     */
    public static boolean isStatAccessPermissionSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                PackageManager packageManager = KApp.getInstance().getPackageManager();
                ApplicationInfo info = packageManager
                    .getApplicationInfo(KApp.getInstance().getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) KApp.getInstance()
                    .getSystemService(Context.APP_OPS_SERVICE);
                appOpsManager
                    .checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return appOpsManager
                    .checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) ==
                    AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                return false;
            }
        } else {
            //小于Android4.4，用不着
            return true;
        }
    }
}