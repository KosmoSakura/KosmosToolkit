package cos.mos.toolkit.hardware;

import android.content.Context;
import android.os.Vibrator;

/**
 * @Description: 震动
 * @Author: Kosmos
 * @Date: 2019.05.16 20:14
 * @Email: KosmoSakura@gmail.com
 * 权限：<uses-permission android:name="android.permission.VIBRATE"/>
 */
public class UVibrate {

    /**
     * @param context     调用震动的Context
     * @param millisecond 震动的时间，毫秒
     * @apiNote 简单震动
     */
    public static Vibrator vSimple(Context context, int millisecond) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(millisecond);
        return vibrator;
    }

    private static long[][] patternVibro = {{0L, 250L, 200L, 250L, 150L, 150L, 75L, 150L, 75L, 150L},
        {0L, 50L, 100L, 50L, 100L, 50L, 100L, 400L, 100L, 300L, 100L, 350L, 50L, 200L, 100L, 100L, 50L, 600L},
        {0L, 150L, 50L, 75L, 50L, 75L, 50L, 150L, 50L, 75L, 50L, 75L, 50L, 300L}};

    /**
     * @param context 调用震动的Context
     * @param pattern 震动形式:{100,400,100,400}---停止 开启 停止 开启
     * @param repeate 震动的次数，-1不重复，非-1为从pattern的指定下标开始重复
     * @apiNote 复杂的震动，eg:vComplicated(context, 1, patternVibro[0]);
     */
    public static Vibrator vComplicated(Context context, int repeate, long... pattern) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, repeate);
        return vibrator;
    }

    /**
     * @apiNote 停止震动
     */
    public static void stop(Vibrator vibrator) {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
