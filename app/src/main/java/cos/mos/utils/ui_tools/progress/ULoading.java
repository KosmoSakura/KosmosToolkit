package cos.mos.utils.ui_tools.progress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import cos.mos.utils.R;


/**
 * @Description 加载动画
 * @Author Kosmos
 * @Date 2020-4-13-013 21:54
 * @Email KosmoSakura@gmail.com
 * @tip 2020.5.19 添加加载动画样式
 */
public class ULoading extends Dialog {
    private volatile static ULoading dialog;
    private TextView tMsg;

    private ULoading(Context context) {
        this(context, R.style.SakuraDialog);
//        this(context, -1);
    }

    private ULoading(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dia_loading);
        tMsg = findViewById(R.id.tv_message);
    }

    public static synchronized ULoading instance(Context context) {
        return instance(context, true);
    }

    public static synchronized ULoading instance(Context context, boolean cancelable) {
        if (dialog == null) {
            dialog = new ULoading(context);
        }
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public synchronized void dialogShow() {
        dialogShow("数据加载中...");
    }

    public synchronized void dialogShow(CharSequence msg) {
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = ((Activity) context);
            if (activity.isFinishing()) return;
            if (activity.isDestroyed()) return;
        }
        tMsg.setText(msg);
        super.show();
//        if (dialog != null && dialog.isShowing()) dialog.dismiss();
//        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    public synchronized void dialogDismiss() {
        dialog.dismiss();
    }

    public static synchronized void clear() {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

}
