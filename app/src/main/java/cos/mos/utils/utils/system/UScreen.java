package cos.mos.utils.utils.system;

import android.util.DisplayMetrics;

import cos.mos.utils.init.k.KApp;

/**
 * @Description: 屏幕信息
 * @Author: Kosmos
 * @Date: 2018.11.26 11:27
 * @Email: KosmoSakura@gmail.com
 * @eg: 最新修改日期：2019年2月25日
 */
public class UScreen {
    private static final DisplayMetrics metric = KApp.instance().getResources().getDisplayMetrics();
    private static final float scale = metric.density;

    /**
     * @return 获取DisplayMetrics对象
     */
    public static DisplayMetrics getDisPlayMetrics() {
        return metric;
    }

    /**
     * @param pxValue 像素单位
     * @return dp单位
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * @param dpValue dp单位
     * @return 像素单位
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @return 屏幕宽度（像素）
     */
    public static int getScreenWidth() {
        return metric.widthPixels;
    }

    /**
     * @return 高度（像素）
     */
    public static int getScreenHeight() {
        return metric.heightPixels;
    }


    /**
     * @return 屏幕密度(0.75 / 1.0 / 1.5)
     */
    public static float getDensity() {
        return metric.density;
    }

    /**
     * @return 屏幕密度DPI(120 / 160 / 240)
     */
    public static int getDensityDpi() {
        return metric.densityDpi;
    }

}
