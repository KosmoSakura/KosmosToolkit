package cos.mos.utils.widget.progress.bubble;

import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @Author: woxingxiao
 * @Date: 2017年03月14日
 * @apiNote 基于https://github.com/woxingxiao/BubbleSeekBar修改
 * BubbleSeekBar Created by woxingxiao on 2016-10-27.
 */
class KBubbleUtils {
    private static final String KEY_MIUI_MANE = "ro.miui.ui.version.name";
    private static Properties sProperties = new Properties();
    private static Boolean miui;

    static boolean isMIUI() {
        if (miui != null) {
            return miui;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                sProperties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            miui = sProperties.containsKey(KEY_MIUI_MANE);
        } else {
            Class<?> clazz;
            try {
                clazz = Class.forName("android.os.SystemProperties");
                Method getMethod = clazz.getDeclaredMethod("get", String.class);
                String name = (String) getMethod.invoke(null, KEY_MIUI_MANE);
                miui = !TextUtils.isEmpty(name);
            } catch (Exception e) {
                miui = false;
            }
        }

        return miui;
    }

    static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            Resources.getSystem().getDisplayMetrics());
    }

    static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
            Resources.getSystem().getDisplayMetrics());
    }
}