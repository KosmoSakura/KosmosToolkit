package cos.mos.library.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @Description: 屏幕信息
 * @Author: Kosmos
 * @Date: 2018.11.26 11:27
 * @Email: KosmoSakura@gmail.com
 */
public class UScreen {
    /**
     * @return 获取DisplayMetrics对象
     */
    public static DisplayMetrics getDisPlayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (null != context) {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        }
        return metric;
    }

    /**
     * @return 获取屏幕的宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        return getDisPlayMetrics(context).widthPixels;
    }

    /**
     * @return 获取屏幕的高（像素）
     */
    public static int getScreenHeight(Context context) {
        return getDisPlayMetrics(context).heightPixels;
    }

    /**
     * @return 屏幕密度(0.75 / 1.0 / 1.5)
     */
    public static float getDensity(Context context) {
        return getDisPlayMetrics(context).density;
    }

    /**
     * @return 屏幕密度DPI(120 / 160 / 240)
     */
    public static int getDensityDpi(Context context) {
        return getDisPlayMetrics(context).densityDpi;
    }

}
