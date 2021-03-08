package cos.mos.toolkit.system;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cos.mos.toolkit.init.KApp;


/**
 * @Description 屏幕信息
 * @Author Kosmos
 * @Date 2018.11.26 11:27
 * @Email KosmoSakura@gmail.com
 * @Tip 2019.2.25:基本函数抽取
 * @Tip 2019.12.20:获取状态栏高度
 * @Tip 2020.3.8:代码封装
 */
public class UScreen {
    private static final DisplayMetrics metrics = KApp.instance().getResources().getDisplayMetrics();
    private static int statusBarHeight;//状态栏高度

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

    public static void setBarColor(Activity activity, int color) {
        setBarColor(activity, color, color);
    }

    /**
     * @param activity    Activity引用
     * @param colorTop    顶部颜色
     * @param colorBottom 底部颜色
     * @apiNote 设置顶部状态栏、底部导航栏颜色(只能在Activity内部调用)
     */
    public static void setBarColor(Activity activity, int colorTop, int colorBottom) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorTop);//顶部状态栏
                window.setNavigationBarColor(colorBottom);//底部导航栏
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    public static void fullScreen(Activity activity) {
        // 去除系统自带标题栏
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        } else {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
                window.setNavigationBarColor(Infos.ColorTheme);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * @return 文字的宽度
     * @tip 获取以paint为画笔要绘制的文字的宽度
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * @return 获取DisplayMetrics对象
     */
    public static DisplayMetrics getDisPlayMetrics() {
        return metrics;
    }

    /**
     * @return Px单位转dp单位
     */
    public static float px2dp(float pxValue) {
        return pxValue / metrics.density + 0.5f;
    }


    /**
     * @return dp单位转Px单位
     */
    public static float dp2px(float dpValue) {
        return dpValue * metrics.density + 0.5f;
    }

    /**
     * @return Sp单位转Px单位
     */
    public static float sp2px(float sp) {
        return metrics.scaledDensity * sp + 0.5f;
    }

    /**
     * @return Sp单位转Px单位
     */
    public static float px2sp(float px) {
        return px / metrics.scaledDensity + 0.5f;
    }

    /**
     * @param sp sp单位
     * @return 像素单位
     */
//    static int sp2px(int sp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
//            Resources.getSystem().getDisplayMetrics());
//    }

    /**
     * @return px单位转Pt单位
     */
    public static float px2Pt(float px) {
        return px * 72 / metrics.xdpi + 0.5f;
    }

    /**
     * @return px单位转Pt单位
     */
    public static float pt2Px(float pt) {
        return pt * metrics.xdpi / 72f + 0.5f;
    }

    /**
     * @return 屏幕宽度（像素）
     */
    public static int getScreenWidth() {
        return metrics.widthPixels;
    }

    /**
     * @return 屏幕高度（像素）
     */
    public static int getScreenHeight() {
        return metrics.heightPixels;
    }


    /**
     * @return 屏幕密度(0.75 / 1.0 / 1.5)
     */
    public static float getDensity() {
        return metrics.density;
    }

    /**
     * @return 屏幕密度DPI(120 / 160 / 240)
     */
    public static int getDensityDpi() {
        return metrics.densityDpi;
    }

    /**
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        if (statusBarHeight <= 0) {
            Resources res = activity.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }
}
