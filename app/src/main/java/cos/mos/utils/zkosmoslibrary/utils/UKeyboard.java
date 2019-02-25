package cos.mos.utils.zkosmoslibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @Description: 键盘输入工具类
 * @Author: Kosmos
 * @Date: 2018年2月8日 16:12
 * @Email: KosmoSakura@gmail.com
 * @eg: 修改日期：2018年09月24日 16:19
 */
public class UKeyboard {
    /**
     * @return true:软键盘已经显示
     * @apiNote 是否显示软件盘
     */
    public boolean isKeyboardShow() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * @apiNote 已经显示，则隐藏，反之则显示
     */
    public void openOrhide() {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param editText 接受软键盘输入的视图
     * @apiNote 强制显示键盘
     */
    public void showForce(EditText editText) {
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        editText.setFocusable(true);//可聚焦
        editText.requestFocus();//聚焦
    }

    /**
     * @param editText 接受软键盘输入的视图
     * @apiNote 强制隐藏键盘
     */
    public void hideForce(EditText editText) {
        if (isKeyboardShow()) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

//--------------------------------------------------------------------------------------------------

    private Activity activity;
    private InputMethodManager imm;//软键盘管理类

    /**
     * 外部静态调用
     */
    public static UKeyboard with(Activity activity) {
        UKeyboard keyboard = new UKeyboard();
        keyboard.activity = activity;
        keyboard.imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return keyboard;
    }

    /**
     * 获取软件盘的高度
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /*
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /*
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        return softInputHeight;
    }

    /**
     * @apiNote 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}
