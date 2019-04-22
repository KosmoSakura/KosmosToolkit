package cos.mos.toolkit.system;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cos.mos.toolkit.init.KApp;


/**
 * @Description: 屏幕信息
 * @Author: Kosmos
 * @Date: 2018.11.26 11:27
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.2.25:基本函数抽取
 */
public class UScreen {
    private static final DisplayMetrics metric = KApp.instance().getResources().getDisplayMetrics();
    private static final float scale = metric.density;

    /**
     * @return bp 截图
     * @apiNote 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap getSnapShotNoBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bp;
    }

    /**
     * @param view 目标控件
     * @return 目标控件的绝对坐标 位置
     */
    public static int[] getAbs(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标，含toolBar
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标，含statusBar
        return location;
    }

    /**
     * @param activity Activity引用
     * @param color    int型色值
     * @apiNote 设置顶部状态栏、底部导航栏颜色
     */
    public static void setBarColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);//顶部状态栏
                window.setNavigationBarColor(color);//底部导航栏
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public static float px2dp(float pxValue) {
        return pxValue / scale + 0.5f;
    }


    /**
     * @param dpValue dp单位
     * @return 像素单位
     */
    public static float dp2px(float dpValue) {
        return dpValue * scale + 0.5f;
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
