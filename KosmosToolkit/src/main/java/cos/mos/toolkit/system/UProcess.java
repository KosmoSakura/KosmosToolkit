package cos.mos.toolkit.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Process;

import java.util.List;

import cos.mos.toolkit.java.UText;

/**
 * @Description: 进程工具类
 * @Author: Kosmos
 * @Date: 2019.04.16 10:20
 * @Email: KosmoSakura@gmail.com
 */
public class UProcess {
    private static UProcess process;
    private ActivityManager actMgr;

    private UProcess() {
    }

    public static UProcess instance() {
        if (process == null) {
            synchronized (UProcess.class) {
                if (process == null) {
                    process = new UProcess();
                }
            }
        }
        return process;
    }

    private ActivityManager getMgr(final Context context) {
        if (actMgr == null) {
            actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return actMgr;
    }

    /**
     * @return 当前进程名
     */
    public String getProcessName(final Context context) {
        int pid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> processes = getMgr(context).getRunningAppProcesses();
        if (UText.isEmpty(processes)) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : processes) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**
     * @return true:处于前台
     * @apiNote 造成原因
     * 因软件正常运行而处于前台
     * 因服务默默运行而处于后台
     */
    public boolean isAppForeground(final Activity activity) {
        List<ActivityManager.RunningAppProcessInfo> processes = getMgr(activity).getRunningAppProcesses();
        if (UText.isEmpty(processes)) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : processes) {
            if (appProcess.processName.equals(activity.getPackageName())) {
                if (appProcess.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return false;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    return false;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return false;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE_PRE_26) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * @return true:本应用存活
     */
    public boolean isAPPALive(final Context context, String packageName) {
        boolean isAPPRunning = false;
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> processes = getMgr(context)
            .getRunningAppProcesses();
        // 遍历，进程名即包名
        for (ActivityManager.RunningAppProcessInfo appInfo : processes) {
            if (packageName.equals(appInfo.processName)) {
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }


    /**
     * @param receiverName 被检查接收器的类名
     * @param actionName   被检查接收器所注册的action（最好唯一）
     * @param pkgName      应用包名
     * @return true:广播接收器存活
     */
    public static boolean isReceiverRunning(Context context,
                                            String receiverName, String actionName, String pkgName) {
        Intent intent = new Intent();
        intent.setAction(actionName);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryBroadcastReceivers(intent, 0);
        int ProcessUid = 0;
        if (resolveInfos != null) {
            for (int index = 0; index < resolveInfos.size(); index++) {
                if (resolveInfos.get(index).activityInfo.packageName.trim().equals(pkgName)
                    && resolveInfos.get(index).activityInfo.name.contains(receiverName)
                    && ProcessUid == Process.myUid()) {
                    return true;
                }
                if (index == resolveInfos.size() - 1) {
                    ProcessUid = Process.myUid();
                }
            }
        }
        return false;
    }
}
