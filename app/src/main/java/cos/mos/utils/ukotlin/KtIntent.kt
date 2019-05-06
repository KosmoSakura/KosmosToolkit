package cos.mos.utils.ukotlin

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import cos.mos.toolkit.init.KApp

/**
 * @Description:各种页面跳转
 * @Author: Kosmos
 * @Date: 2019.04.23 13:10
 * @Email: KosmoSakura@gmail.com
 */
object KtIntent {
    private fun start(intent: Intent) {
        KApp.instance().startActivity(intent)
    }

    /**
     * 去系统授权页面
     * 普通权限
     * */
    @JvmStatic
    fun goSystem() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + KApp.instance().packageName))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(intent)
    }

    /**
     * 去系统授权页面（悬浮窗权限）
     * 高级权限
     */
    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun goSysOverlay() {
        val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + KApp.instance().packageName))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(intent)
    }

    /**
     * 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限
     */
    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun goSysAdvanced() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(intent)
    }

    /**
     * @param activity 页面
     * @param request  请求值
     * @apiNote 去系统授权页面（有权限查看使用情况的应用）
     * 高级权限-带返回值
     */
    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun goSysAdvanced(activity: Activity, request: Int) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivityForResult(intent, request)
    }

    /**
     * 去系统授权页面（自启动管理）
     * 高级权限
     * 注意，很多国外的手机没有这个页面，直接跳过去会崩
     */
    @JvmStatic
    fun goSysBKG() {
        val intent = Intent()
        val comp = ComponentName("com.android.settings", "com.android.settings.BackgroundApplicationsManager")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.component = comp
        start(intent)
    }

    /**
     * 去系统授权页面（自启动管理,适配重载）
     * 高级权限
     * 注意，很多国外的手机没有这个页面，直接跳过去会崩
     * 数据来自：https://blog.csdn.net/gxp1182893781/article/details/78027863
     */
    @JvmStatic
    fun goSysBKGAda() {
        var intent = Intent()
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            KtLog.commonI("******************当前手机型号为：" + Build.MANUFACTURER)
            var componentName: ComponentName? = null
            when (Build.MANUFACTURER) {
                "Xiaomi"  // 红米Note4测试通过
                -> componentName = ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity")
                // 乐视2测试通过
                "Letv" -> intent.action = "com.letv.android.permissionautoboot"
                "samsung"  // 三星Note5测试通过
                -> componentName =
                        ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity")
                "HUAWEI"  // 华为测试通过
                -> componentName = ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity")
                "vivo"  // VIVO测试通过
                -> componentName =
                        ComponentName.unflattenFromString("com.iqoo.secure" + "/.safeguard.PurviewTabActivity")
                "Meizu"  //万恶的魅族
                ->
                    // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，
                    // 系统更新之后，完全找不到了，心里默默Fuck！
                    // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，
                    // 所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                    componentName =
                            ComponentName.unflattenFromString("com.meizu.safe" + "/.permission.PermissionMainActivity")
                "OPPO"  // OPPO R8205测试通过
                -> {
                    componentName = ComponentName.unflattenFromString(
                            "com.oppo.safe" + "/.permission.startup.StartupAppListActivity")
                    val intentOppo = Intent()
                    intentOppo.setClassName("com.oppo.safe/.permission.startup", "StartupAppListActivity")
                    if (KApp.instance().packageManager.resolveActivity(intentOppo, 0) == null) {
                        componentName = ComponentName.unflattenFromString(
                                "com.coloros.safecenter" + "/.startupapp.StartupAppListActivity")
                    }
                }
                "ulong"  // 360手机 未测试
                -> componentName =
                        ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity")
                else -> {
                    // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                    // 针对于其他设备，我们只能调整当前系统app查看详情界面
                    // 在此根据用户手机当前版本跳转系统设置界面
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", KApp.instance().packageName, null)
                }
            }
            intent.component = componentName
            start(intent)
        } catch (e: Exception) { //抛出异常就直接打开设置页面
            intent = Intent(Settings.ACTION_SETTINGS)
            start(intent)
        }
    }

    /**
     * 回到桌面
     * */
    @JvmStatic
    fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        start(homeIntent)
    }

    /**
     * 去无障碍授权页面
     */
    @JvmStatic
    fun goAssist() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(intent)
    }

    /**
     * @param dir 视频地址
     * @apiNote 打开系统视频播放器
     */
    @JvmStatic
    fun toVideo(dir: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(Uri.parse(dir), "video/*")
        try {
            start(intent)
        } catch (e: Exception) {
            KtToast.show("No default player")
        }
    }


    /**
     * @param dir      音频地址
     * @param listener 链接开始、扫描结束回调监听
     * @apiNote 更新媒体库
     */
    @JvmStatic
    fun addMediaLibrary(dir: String, listener: MediaScannerConnection.MediaScannerConnectionClient) {
        MediaScannerConnection.scanFile(KApp.instance(), arrayOf(dir), null, listener)
    }

    /**
     * @param title   分享标题
     * @param content 分享内容
     *@apiNote 分享文字
     */
    @JvmStatic
    fun shareText(title: String, content: String) {
        if (KtText.isEmpty(title) || KtText.isEmpty(content)) {
            KtToast.show("Data abnormity !")
            return
        }
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        //创建分享的Dialog
        shareIntent = Intent.createChooser(shareIntent, title)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(shareIntent)
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
    @JvmStatic
    fun toPlayStore() {
        try {
            val uri = Uri.parse("market://details?id=" + KApp.instance().packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.android.vending")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            start(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param email 收件人地址
     * @apiNote 打开邮件，并填入收件人
     */
    @JvmStatic
    fun toEmail(email: String) {
        try {
            val data = Intent(Intent.ACTION_SENDTO)
            data.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data.data = Uri.parse("mailto:way.ping.li@gmail.com")
            data.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(email))
            data.putExtra(Intent.EXTRA_SUBJECT, "This is a title")
            data.putExtra(Intent.EXTRA_TEXT, "This is the content")
            start(data)
        } catch (e: Exception) {
            KtToast.show("No mail client found")
        }

    }

    /**
     * @param link 地址
     * @apiNote 用默认浏览器打开
     */
    @JvmStatic
    fun toBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(intent)
    }

    /**
     * @param telephone 电话号
     * @apiNote 打开拨号页面，并填入电话号
     */
    @JvmStatic
    fun toPhone(telephone: String) {
        val phone = Intent(Intent.ACTION_DIAL)
        phone.data = Uri.parse("tel:$telephone")
        phone.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start(phone)
    }
}