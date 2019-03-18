package cos.mos.utils.utils.system;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import cos.mos.utils.init.k.KApp;
import cos.mos.utils.utils.ULog;
import cos.mos.utils.utils.java.UText;
import cos.mos.utils.utils.ui.toast.UToast;


/**
 * @Description: 各中页面跳转
 * @Author: Kosmos
 * @Date: 2018.12.17 15:16
 * @Email: KosmoSakura@gmail.com
 * @eg: 2018.12.24:特殊权限页面跳转
 * @eg: 2019.2.26:辅助通道页面跳转
 * @eg: 2019.3.5:打开系统视频播放器
 * @eg: 2019.3.7:分享文字
 */
public class UIntent {
    private static void start(Intent intent) {
        KApp.instance().startActivity(intent);
    }

    /**
     * 去系统授权页面
     * 普通权限
     */
    public static void goSys() {
        Uri packageURI = Uri.parse("package:" + KApp.instance().getPackageName());
        start(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI));
    }

    /**
     * 去系统授权页面（悬浮窗权限）
     * 高级权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void goSysOverlay() {
        Uri packageURI = Uri.parse("package:" + KApp.instance().getPackageName());
        start(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI));
    }

    /**
     * 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void goSysAdvanced() {
        start(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    /**
     * @param activity 页面
     * @param request  请求值
     * @apiNote 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限-带返回值
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void goSysAdvanced(Activity activity, int request) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, request);
    }

    /**
     * 去系统授权页面（自启动管理）
     * 高级权限
     * 注意，很多国外的手机没有这个页面，直接跳过去会崩
     */
    public static void goSysBKG() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.android.settings",
            "com.android.settings.BackgroundApplicationsManager");
        intent.setComponent(comp);
        start(intent);
    }

    /**
     * 去系统授权页面（自启动管理,适配重载）
     * 高级权限
     * 注意，很多国外的手机没有这个页面，直接跳过去会崩
     * 数据来自：https://blog.csdn.net/gxp1182893781/article/details/78027863
     */
    public static void goSysBKGAda() {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ULog.commonI("******************当前手机型号为：" + Build.MANUFACTURER);
            ComponentName componentName = null;
            switch (Build.MANUFACTURER) {
                case "Xiaomi":  // 红米Note4测试通过
                    componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
                    break;
                case "Letv":  // 乐视2测试通过
                    intent.setAction("com.letv.android.permissionautoboot");
                    break;
                case "samsung":  // 三星Note5测试通过
                    componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.ram.AutoRunActivity");
                    break;
                case "HUAWEI":  // 华为测试通过
                    componentName = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity");
                    break;
                case "vivo":  // VIVO测试通过
                    componentName = ComponentName.unflattenFromString("com.iqoo.secure" +
                        "/.safeguard.PurviewTabActivity");
                    break;
                case "Meizu":  //万恶的魅族
                    // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，
                    // 系统更新之后，完全找不到了，心里默默Fuck！
                    // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，
                    // 所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                    componentName = ComponentName.unflattenFromString("com.meizu.safe" +
                        "/.permission.PermissionMainActivity");
                    break;
                case "OPPO":  // OPPO R8205测试通过
                    componentName = ComponentName.unflattenFromString("com.oppo.safe" +
                        "/.permission.startup.StartupAppListActivity");
                    Intent intentOppo = new Intent();
                    intentOppo.setClassName("com.oppo.safe/.permission.startup",
                        "StartupAppListActivity");
                    if (KApp.instance().getPackageManager().resolveActivity(intentOppo, 0) == null) {
                        componentName = ComponentName.unflattenFromString("com.coloros.safecenter" +
                            "/.startupapp.StartupAppListActivity");
                    }

                    break;
                case "ulong":  // 360手机 未测试
                    componentName = new ComponentName("com.yulong.android.coolsafe",
                        ".ui.activity.autorun.AutoRunListActivity");
                    break;
                default:
                    // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                    // 针对于其他设备，我们只能调整当前系统app查看详情界面
                    // 在此根据用户手机当前版本跳转系统设置界面
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", KApp.instance().getPackageName(), null));
                    break;
            }
            intent.setComponent(componentName);
            start(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            start(intent);
        }
    }

    /**
     * 回到桌面
     */
    public static void goHome() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        start(homeIntent);
    }

    /**
     * 去无障碍授权页面
     */
    public static void goAssist() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(intent);
    }


    /**
     * @param dir 视频地址
     * @apiNote 打开系统视频播放器
     */
    public static void toVideo(String dir) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(dir), "video/*");
        try {
            start(intent);
        } catch (Exception e) {
            UToast.show("No default player");
        }
    }

    /**
     * @param dir 音频地址
     * @apiNote 打开系统音频播放器
     */
    public static void toAudio(String dir) {
        //
    }

    /**
     * @param link 地址
     * @apiNote 用默认浏览器打开
     */
    public static void toBrowser(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(intent);
    }

    /**
     * @param dir      音频地址
     * @param listener 链接开始、扫描结束回调监听
     * @apiNote 更新媒体库
     */
    public static void addMediaLibrary(String dir, MediaScannerConnection.MediaScannerConnectionClient listener) {
        MediaScannerConnection.scanFile(KApp.instance(), new String[]{dir}, null, listener);
    }

    /**
     * @param title   分享标题
     * @param content 分享内容
     * @apiNote 分享文字
     */
    public static void shareText(String title, String content) {
        if (UText.isEmpty(title) || UText.isEmpty(content)) {
            UToast.show("Data abnormity !");
            return;
        }
        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);
        share_intent.putExtra(Intent.EXTRA_TEXT, content);
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, title);
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(share_intent);
    }
}
