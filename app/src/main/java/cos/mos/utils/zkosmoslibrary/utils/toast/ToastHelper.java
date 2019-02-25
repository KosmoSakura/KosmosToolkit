package cos.mos.utils.zkosmoslibrary.utils.toast;

import android.app.Application;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import cos.mos.utils.R;


/**
 * author : HJQ
 * github : https://github.com/getActivity/ToastUtils
 * time   : 2018/11/02
 * desc   : 自定义 Toast 辅助类
 */
final class ToastHelper implements Runnable {

    private static final long SHORT_DURATION_TIMEOUT = 2000; // 短吐司显示的时长
    private static final long LONG_DURATION_TIMEOUT = 4000; // 长吐司显示的时长

    // WindowManager 处理消息线程
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    // 当前的吐司对象
    private final Toast mToast;

    // WindowManager 辅助类
    private final WindowHelper mWindowHelper;

    ToastHelper(Toast toast, Application application) {
        mToast = toast;
        mWindowHelper = new WindowHelper(application);
    }

    /***
     * 显示吐司弹窗
     */
    void show() {
        if (mWindowHelper.getWindowManager() != null) {
            /*
             这里解释一下，为什么不复用 WindowManager.LayoutParams 这个对象
             因为如果复用了，不同 Activity 之间不能共用一个，第一个 Activity 调用显示方法可以显示出来，但是会导致后面的 Activity 都显示不出来
             又或者说，非第一次调用显示方法的 Activity 都会把这个显示请求推送给之前第一个调用显示的 Activity 上面，如果第一个 Activity 已经销毁，还会报以下异常
             android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@ef1ccb6 is not valid; is your activity running?
             */
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            // 为什么不能加 TYPE_TOAST，因为通知权限在关闭后设置显示的类型为Toast会报错
            // android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
            // params.type = WindowManager.LayoutParams.TYPE_TOAST;
            // 判断是否为 Android 6.0 及以上系统并且有悬浮窗权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(mToast.getView().getContext())) {
                // 解决使用 WindowManager 创建的 Toast 只能显示在当前 Activity 的问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
            }
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = R.style.ToastAnimation;
            params.setTitle(Toast.class.getSimpleName());
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            // 重新初始化位置
            params.gravity = mToast.getGravity();
            params.x = mToast.getXOffset();
            params.y = mToast.getYOffset();

            try {
                // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
                // java.lang.IllegalStateException: View android.widget.TextView{3d2cee7 V.ED..... ......ID 0,0-312,153} has already been added to the window manager.
                mWindowHelper.getWindowManager().addView(mToast.getView(), params);
            } catch (IllegalStateException | WindowManager.BadTokenException ignored) {
            }

            // 移除之前移除吐司的任务
            mHandler.removeCallbacks(this);
            // 添加一个移除吐司的任务
            mHandler.postDelayed(this, mToast.getDuration() == Toast.LENGTH_LONG ? LONG_DURATION_TIMEOUT : SHORT_DURATION_TIMEOUT);
        }
    }

    /**
     * 取消吐司弹窗
     */
    void cancel() {
        if (mWindowHelper.getWindowManager() != null) {
            try {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                // java.lang.IllegalArgumentException: View=android.widget.TextView{3d2cee7 V.ED..... ........ 0,0-312,153} not attached to window manager
                mWindowHelper.getWindowManager().removeView(mToast.getView());
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    /**
     * {@link Runnable}
     */
    @Override
    public void run() {
        cancel();
    }
}