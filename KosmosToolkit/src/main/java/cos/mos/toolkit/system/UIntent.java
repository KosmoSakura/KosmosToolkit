package cos.mos.toolkit.system;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import cos.mos.toolkit.init.k.KApp;
import cos.mos.toolkit.ULog;
import cos.mos.toolkit.java.UText;
import cos.mos.toolkit.ui.toast.ToastUtil;
import cos.mos.toolkit.ui.toast.UToast;


/**
 * @Description: 各中页面跳转
 * @Author: Kosmos
 * @Date: 2018.12.17 15:16
 * @Email: KosmoSakura@gmail.com
 * @eg: 2018.12.24:特殊权限页面跳转
 * @eg: 2019.2.26:辅助通道页面跳转
 * @eg: 2019.3.5:打开系统视频播放器
 * @eg: 2019.3.7:分享文字
 * @eg: 2019.3.18: 跳转应用商店
 * @eg 2019.3.21: 跳转邮箱、拨号等界面
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
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(intent);
    }

    /**
     * 去系统授权页面（悬浮窗权限）
     * 高级权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void goSysOverlay() {
        Uri packageURI = Uri.parse("package:" + KApp.instance().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(intent);
    }

    /**
     * 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void goSysAdvanced() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(intent);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(dir), "video/*");
        try {
            start(intent);
        } catch (Exception e) {
            ToastUtil.show("No default player");
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
            ToastUtil.show("Data abnormity !");
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

    /**
     * @apiNote 跳转当前应用的GooglePlay商店
     * Google Play:com.android.vending
     * 应用宝:com.tencent.android.qqdownloader
     * 360手机助手:com.qihoo.appstore
     * 百度手机助:com.baidu.appsearch
     * 小米应用商店:com.xiaomi.market
     * 豌豆荚:com.wandoujia.phoenix2
     * 华为应用市场;com.huawei.appmarket
     * 淘宝手机助手：com.taobao.appcenter
     * 安卓市场：com.hiapk.marketpho
     * 安智市场：cn.goapk.market
     */
    public static void toPlayStore() {
        try {
            Uri uri = Uri.parse("market://details?id=" + KApp.instance().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.vending");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            start(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param email 收件人地址
     * @apiNote 打开邮件，并填入收件人
     */
    public static void toEmail(String email) {
        try {
            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            data.setData(Uri.parse("mailto:way.ping.li@gmail.com"));
            data.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            data.putExtra(Intent.EXTRA_SUBJECT, "This is a title");
            data.putExtra(Intent.EXTRA_TEXT, "This is the content");
            start(data);
        } catch (Exception e) {
            UToast.show("No mail client found");
        }
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
     * @param telephone 电话号
     * @apiNote 打开拨号页面，并填入电话号
     */
    public static void toPhone(String telephone) {
        Intent phone = new Intent(Intent.ACTION_DIAL);
        phone.setData(Uri.parse("tel:" + telephone));
        phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start(phone);
    }
}
