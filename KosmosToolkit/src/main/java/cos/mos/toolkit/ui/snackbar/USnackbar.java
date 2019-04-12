package cos.mos.toolkit.ui.snackbar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import cos.mos.toolkit.R;
import cos.mos.toolkit.java.UText;


/**
 * @Description: Snackbar工具类
 * @Author: Kosmos
 * @Date: 2018年09月28日
 * @Email: KosmoSakura@foxmail.com
 */
public class USnackbar {
    private static WeakReference<Snackbar> snackbarWeakReference;
    private static final int NoColor = -1;

    /**
     * @apiNote 取消snackbar显示
     */
    public static void dismissSnackbar() {
        if (snackbarWeakReference != null && snackbarWeakReference.get() != null) {
            snackbarWeakReference.get().dismiss();
            snackbarWeakReference = null;
        }
    }

    /**
     * @param parent 当前页面Context（不能传AppContext)
     * @param text   展示文字
     * @apiNote 弹出短窗口
     */
    public static void showShort(Context parent, CharSequence text) {
        showColorNo(parent, text, Snackbar.LENGTH_SHORT, null, null);
    }

    /**
     * @param parent 当前页面Context（不能传AppContext)
     * @param text   展示文字
     * @apiNote 弹出长窗口
     */
    public static void showLong(Context parent, CharSequence text) {
        showColorNo(parent, text, Snackbar.LENGTH_LONG, null, null);
    }

    /**
     * @param parent   当前页面Context（不能传AppContext)
     * @param text     展示文字
     * @param action   事件文字
     * @param listener 事件监听
     * @apiNote 弹出带事件窗口
     */
    public static void showAction(Context parent, CharSequence text, String action, View.OnClickListener listener) {
        showColorNo(parent, text, Snackbar.LENGTH_SHORT, action, listener);
    }

    /**
     * @param parent 当前页面Context（不能传AppContext)
     * @param text   展示文字
     * @apiNote 展示默认颜色短弹窗
     */
    public static void showShortColor(Context parent, CharSequence text) {
        showColorDefault(parent, text, Snackbar.LENGTH_SHORT, null, null);
    }

    /**
     * @param parent 当前页面Context（不能传AppContext)
     * @param text   展示文字
     * @apiNote 弹出默认颜色长窗口
     */
    public static void showLongColor(Context parent, CharSequence text) {
        showColorDefault(parent, text, Snackbar.LENGTH_LONG, null, null);
    }

    /**
     * @param parent   当前页面Context（不能传AppContext)
     * @param text     展示文字
     * @param action   事件文字
     * @param listener 事件监听
     * @apiNote 弹出默认颜色带事件窗口
     */
    public static void showActionColor(Context parent, CharSequence text, String action, View.OnClickListener listener) {
        showColorDefault(parent, text, Snackbar.LENGTH_SHORT, action, listener);
    }

    /**
     * @param parent   当前页面Context（不能传AppContext)
     * @param text     展示文字
     * @param action   事件文字
     * @param listener 事件监听
     * @apiNote 展示默认颜色弹窗
     */
    private static void showColorDefault(Context parent, CharSequence text, int duration,
                                         CharSequence action, View.OnClickListener listener) {
        base(getView(parent), text, duration, action, listener,
            ContextCompat.getColor(parent, R.color.white),
            ContextCompat.getColor(parent, R.color.fun_txt_black),
            ContextCompat.getColor(parent, R.color.white));
    }

    /**
     * @param parent     当前页面Context（不能传AppContext)
     * @param text       展示文字
     * @param duration   弹窗时间
     * @param actionText 事件文字
     * @param listener   事件监听
     * @apiNote 展示无色弹窗
     */
    private static void showColorNo(Context parent, CharSequence text, int duration,
                                    CharSequence actionText, View.OnClickListener listener) {
        base(getView(parent), text, duration, actionText, listener, NoColor, NoColor, NoColor);
    }

    /**
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param duration        显示时长
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionTextColor 事件文本颜色
     * @param actionText      事件文本
     * @param listener        监听器
     */
    private static void base(View parent, CharSequence text, int duration,
                             CharSequence actionText, View.OnClickListener listener,
                             @ColorInt int textColor, @ColorInt int bgColor, @ColorInt int actionTextColor) {
        snackbarWeakReference = new WeakReference<>(Snackbar.make(parent, text, duration));
        Snackbar snackbar = snackbarWeakReference.get();
        if (textColor != NoColor) {
            snackbar.setActionTextColor(textColor);
        }
        if (bgColor != NoColor) {
            snackbar.getView().setBackgroundColor(bgColor);
        }
        if (!UText.isEmpty(actionText) && listener != null) {
            if (actionTextColor != NoColor) {
                snackbar.setActionTextColor(actionTextColor);
            }
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }

    /**
     * @return xml级别布局底层View，弹窗位置于可见视图底部（比如：区别于华为
     */
    private static ViewGroup getView(Context context) {
        return (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    /**
     * @return 页面最底层View，弹窗位置无视可折叠底部导航栏（比如：华为
     */
    private static ViewGroup getRootView(Context context) {
        return (ViewGroup) getView(context).getRootView();
    }
}
