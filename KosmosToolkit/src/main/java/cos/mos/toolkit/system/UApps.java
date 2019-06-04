package cos.mos.toolkit.system;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cos.mos.toolkit.init.KApp;
import cos.mos.toolkit.java.UText;
import cos.mos.toolkit.java.UUnit;

/**
 * @Description: 应用程序工具包
 * @Author: Kosmos
 * @Date: 2018.11.01 15:43
 * @Email: KosmoSakura@foxmail.com
 */
public class UApps {
    private PackageManager pkgMgr;
    private static List<AppBean> appList;
    private static UApps apps;

    private UApps() {
        loadAllApps();
        getPkgMgr();
    }

    public static UApps instance() {
        if (apps == null || UText.isEmpty(appList)) {
            apps = new UApps();
        }
        return apps;
    }

    private PackageManager getPkgMgr() {
        if (pkgMgr == null) {
            pkgMgr = KApp.instance().getPackageManager();
        }
        return pkgMgr;
    }

    /**
     * 获取系统所有安装应用（应用区分系统非系统）
     */
    public List<AppBean> getAppList() {
        if (UText.isEmpty(appList)) {
            loadAllApps();
        }
        return appList;
    }

    public void appListClear() {
        appList.clear();
    }

    /**
     * @apiNote 获取手机内非系统应用
     */
    private void loadUsersApp() {
        if (appList == null) {
            appList = new ArrayList<>();
        }
        //获取手机内所有应用
        List<ApplicationInfo> paklist = getPkgMgr().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        String name;
        String dir;
        for (ApplicationInfo info : paklist) {
            //用户自己安装的应用程序
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                name = info.loadLabel(getPkgMgr()).toString();
                dir = info.sourceDir;
                if (!UText.isEmpty(name) && !UText.isEmpty(dir)) {
                    AppBean app = new AppBean();
                    app.setPkgName(info.packageName);//包名
                    app.setAppName(name);//应用名
                    app.setImage(info.loadIcon(getPkgMgr()));
                    appList.add(app);
                }
            }
        }
    }

    /**
     * 加载所有app
     */
    private void loadAllApps() {
        if (appList == null) {
            appList = new ArrayList<>();
        }
        try {
            //获取手机上的所有应用
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfos = getPkgMgr().queryIntentActivities(intent, 0);
            //数据转换
            String pkgName;
            ApplicationInfo appInfo;
            AppBean appBean;
            for (ResolveInfo resolveInfo : resolveInfos) {
                pkgName = resolveInfo.activityInfo.packageName;
                appBean = new AppBean();
                appBean.setPkgName(pkgName);
                appInfo = getPkgMgr().getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
                if (appInfo != null) {
                    appBean.setAppName(getPkgMgr().getApplicationLabel(appInfo).toString());
                    appBean.setSysApp((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
                    appBean.setImage(appInfo.loadIcon(getPkgMgr()));
                }
                appList.add(appBean);
                Collections.sort(appList, (o1, o2) -> o1.getAppName().compareTo(o2.getAppName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pkgName 包名
     * @return 相关信息
     * 通过包名获取该应用的相关信息
     */
    public AppBean getAppByPkgName(String pkgName) {
        for (int i = 0; i < appList.size(); i++) {
            AppBean bean = appList.get(i);
            if (pkgName.equals(bean.getPkgName())) {
                return bean;
            }
        }
        return null;
    }

    /**
     * @apiNote 查询手机内所有支持分享的应用
     */
    public List<ResolveInfo> getShareApps() {
        List<ResolveInfo> mApps;
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        mApps = getPkgMgr().queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    /**
     * 通过包名启动第三方app
     */
    public void startThridApp(String pkgName) {
        try {
            Intent minIntent = getPkgMgr().getLaunchIntentForPackage(pkgName);
            KApp.instance().startActivity(minIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 语言列表
     * @apiNote 获取当前系统上的语言列表(Locale列表)
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * @return 获取应用数据大小
     */
    public float getAppSize(ApplicationInfo info) {
        //应用存放数据目录
        String dir = info.sourceDir;
        long length = new File(dir).length();//获取应用数据大小
        return length * 1f / 1024 / 1024;//转换为 M
    }

    public String getAppSize(String dir) {
//        long length = new File(dir).length();
//        float size = length * 1f / 1024 / 1024;
//        return new DecimalFormat("#0.0").format(size) + "MB";
        return UUnit.formatBit(new File(dir).length());
    }

    /**
     * @return 应用是否安装在外置储存空间
     */
    public boolean installExternalStorage(ApplicationInfo info) {
        int flags = info.flags;
        return (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
    }

    /**
     * @return 是否是系统应用
     */
    public boolean isSystemApp(ApplicationInfo info) {
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
    }

    /**
     * 轻量： 获取栈顶应用包名(非系统程序在在栈顶时有效)
     */
    public String getCurTopAppPkg() {
        ActivityManager am = (ActivityManager) KApp.instance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(Integer.MAX_VALUE);
        if (!UText.isEmpty(appTask)) {
            return appTask.get(0).topActivity.getPackageName();
        }
        return "";
    }

    private ActivityManager actMgr;
    private UsageStatsManager usgMgr;

    /**
     * 获取栈顶应用包名（所有程序有效）
     */
    public String getTopAppPkg() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (actMgr == null) {
                actMgr = (ActivityManager) KApp.instance().getSystemService(Context.ACTIVITY_SERVICE);
            }
            List<ActivityManager.RunningTaskInfo> appTasks = actMgr.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            //5.0以后需要用这方法
            if (usgMgr == null) {
                usgMgr = (UsageStatsManager) KApp.instance().getSystemService(Context.USAGE_STATS_SERVICE);
            }
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usgMgr.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                    //break;//注意，这里不能退出，因为这是获取一段时间内移到栈顶的包名，要取最新的才是当前栈顶的包名
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    /**
     * 获取当前正在运行的桌面launcher包名
     */
    public String getLauncherPkgName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPkgMgr().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return null;// 一般不会发生
        }
        if (res.activityInfo.packageName.equals("android")) {
            return null;// 有多个桌面程序存在，且未指定默认项时；
        } else {
            return res.activityInfo.packageName;
        }
    }
}