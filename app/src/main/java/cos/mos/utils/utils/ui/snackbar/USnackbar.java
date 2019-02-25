package cos.mos.utils.utils.ui.snackbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import java.lang.ref.WeakReference;

import androidx.annotation.ColorInt;
import cos.mos.utils.utils.java.UText;

/**
 * @Description: Snackbar工具类 改
 * @Author: Kosmos
 * @Date: 2018年09月12日 16:19
 * @Email: KosmoSakura@foxmail.com
 * @eg: 基于MySnackBar封装 https://github.com/guoyoujin/MySnackBar
 * @eg: 最新修改日期：2018年09月2日 23:24
 */
public class USnackbar {
    private static WeakReference<TSnackbar> snackbarWeakReference;

    /**
     * 取消snackbar显示
     */
    public static void dismissSnackbar() {
        if (snackbarWeakReference != null && snackbarWeakReference.get() != null) {
            snackbarWeakReference.get().dismiss();
            snackbarWeakReference = null;
        }
    }

    public static void showNormal(Context parent, CharSequence text) {
        showNoAction(getRootView(parent), text, Prompt.SUCCESS, TSnackbar.LENGTH_SHORT, true);
    }

    public static void showWarning(Context parent, CharSequence text) {
        showNoAction(getRootView(parent), text, Prompt.WARNING, TSnackbar.LENGTH_SHORT, true);
    }

    public static void showError(Context parent, CharSequence text) {
        showNoAction(getRootView(parent), text, Prompt.ERROR, TSnackbar.LENGTH_SHORT, true);
    }

    public static void showProgress(Context parent, CharSequence text) {
        showNoColor(getRootView(parent), text, Prompt.SUCCESS, TSnackbar.LENGTH_SHORT, true, true, null, null);
    }

    public static void showNormalDown(Context parent, CharSequence text) {
        showNoAction(getView(parent), text, Prompt.SUCCESS, TSnackbar.LENGTH_SHORT, false);
    }

    public static void showWarningDown(Context parent, CharSequence text) {
        showNoAction(getView(parent), text, Prompt.WARNING, TSnackbar.LENGTH_SHORT, false);
    }

    public static void showErrorDown(Context parent, CharSequence text) {
        showNoAction(getView(parent), text, Prompt.ERROR, TSnackbar.LENGTH_SHORT, false);
    }

    public static void showProgressDown(Context parent, CharSequence text) {
        showNoColor(getView(parent), text, Prompt.SUCCESS, TSnackbar.LENGTH_SHORT, false, true, null, null);
    }

    private static void showNoAction(View parent, CharSequence text, Prompt prompt, int duration, boolean top) {
        showNoColor(parent, text, prompt, duration, top, false, null, null);
    }

    private static void showNoColor(View parent, CharSequence text, Prompt prompt, int duration,
                                    boolean top, boolean progress, CharSequence actionText, View.OnClickListener listener) {
        base(parent, text, prompt, duration, top, progress, -1, -1, -1, actionText, null);
    }

    private static ViewGroup getView(Context context) {
        return (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
    }

    private static ViewGroup getRootView(Context context) {
        return (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
    }

    /**
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param prompt          显示类型
     * @param duration        显示时长
     * @param top             是否显示在顶部
     * @param showProgress    是否显示进度条
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionTextColor 事件文本颜色
     * @param actionText      事件文本
     * @param listener        监听器
     */
    private static void base(View parent, CharSequence text, Prompt prompt, int duration, boolean top, boolean showProgress,
                             @ColorInt int textColor, @ColorInt int bgColor, @ColorInt int actionTextColor,
                             CharSequence actionText, View.OnClickListener listener) {
        snackbarWeakReference = new WeakReference<>(TSnackbar.make(parent, text, duration,
            top ? TSnackbar.APPEAR_FROM_TOP_TO_DOWN : TSnackbar.APPEAR_FROM_BOTTOM_TO_TOP));
        TSnackbar snackbar = snackbarWeakReference.get();
        if (textColor > 0) {
            snackbar.setActionTextColor(textColor);
        }
        if (bgColor > 0) {
            snackbar.getView().setBackgroundColor(bgColor);
        }
        if (!UText.isEmpty(actionText) && listener != null) {
            if (actionTextColor > 0) {
                snackbar.setActionTextColor(actionTextColor);
            }
            snackbar.setAction(actionText, listener);
        }
        snackbar.setPromptThemBackground(prompt);
        if (showProgress) {
            snackbar.addIconProgressLoading(0, true, false);
        }
        snackbar.show();
    }
}
