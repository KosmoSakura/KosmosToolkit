package cos.mos.toolkit.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import cos.mos.utils.ui.MainActivity;


/**
 * @Description: 创建桌面快捷方式
 * @Author: Kosmos
 * @Date: 2019.03.19 13:25
 * @Email: KosmoSakura@gmail.com
 * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
 * <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT"/>
 */
public class UShortcut {
    /**
     * @param activity Activity对象
     * @param name     快捷方式名称
     * @param iconRes  快捷方式图标(R.mipmap.icon
     * @apiNote 添加桌面图标快捷方式
     * 8.0以下手机提示应用未安装：AndroidManifest的acitivity标签添加 android:exported="true"
     */
    public static void addShortcut(Activity activity, String name, int iconRes) {
        //快捷方式图标点击动作
        Intent actionIntent = new Intent(activity, MainActivity.class);
        actionIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //  创建快捷方式的intent广播
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);//快捷名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,//快捷图标-ShortcutIconResource
                Intent.ShortcutIconResource.fromContext(activity, iconRes));
//            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);//快捷图标-Bitmap
            shortcut.putExtra("duplicate", false);//允许重复创建(不一定有效)
            // 添加携带的下次启动要用的Intent信息
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
            activity.sendBroadcast(shortcut);
        } else {
            ShortcutManager shortcutManager = (ShortcutManager) activity.getSystemService(Context.SHORTCUT_SERVICE);
            if (null == shortcutManager) {
                return;
            }
            if (shortcutManager.isRequestPinShortcutSupported()) {
                ShortcutInfo shortcutInfo = new ShortcutInfo
                    .Builder(activity, name)
                    .setShortLabel(name)
//                    .setIcon(Icon.createWithBitmap(iconRes))//快捷图标-Bitmap
                    .setIcon(Icon.createWithResource(activity, iconRes))
                    .setIntent(actionIntent)
                    .setLongLabel(name)
                    .build();
                //当添加快捷方式的确认弹框弹出来时，将被回调
//                PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(activity, 0,
//                    new Intent(context, MyReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                shortcutManager.requestPinShortcut(shortcutInfo,
                    PendingIntent
                        .getActivity(activity, 132, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        .getIntentSender());
            }
        }
    }

    public static void delShortcut(Activity activity, String name) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//        }
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);//快捷名称
        shortcut.putExtra("duplicate", true);//是否循环删除（比如多次创建）
        activity.sendBroadcast(shortcut);
    }
}
