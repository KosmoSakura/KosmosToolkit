package cos.mos.utils.ui_tools.progress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;


/**
 * @Description 加载动画,基于ProgressDialog
 * @Author Kosmos
 * @Date 2020-4-13-013 21:54
 * @Email KosmoSakura@gmail.com
 */
public class DialogLoading {
    private Context context;
    private ProgressDialog dialog;

    public DialogLoading(Context context) {
        this.context = context;
    }

    public void showLoading(String content) {
        if (context == null) return;
        if (context instanceof Activity) {
            Activity activity = ((Activity) context);
            if (activity.isFinishing()) return;
            if (activity.isDestroyed()) return;
        }
        if (dialog == null) {
            dialog = ProgressDialog.show(context, null, content, false, true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        } else if (!dialog.isShowing()) {
            dialog = ProgressDialog.show(context, null, content, false, true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        }
    }

    public void notifyLoading(String content) {
        if (dialog != null && dialog.isShowing()) {
            dialog.setMessage(content);
            dialog.show();
        } else {
            showLoading(content);
        }
    }

    public void dismiss() {
        try {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShow() {
        return dialog != null && dialog.isShowing();
    }
}
