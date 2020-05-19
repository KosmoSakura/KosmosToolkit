package cos.mos.utils.ui_tools.progress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import cos.mos.toolkit.R;
import cos.mos.toolkit.log.ULog;

/**
 * @Description 原生转圈进度条
 * @Author Kosmos
 * @Date 2018.12.11 10:43
 * @Email KosmoSakura@gmail.com
 * @tip 最新修改日期：2018年12月11日,基于原生ProgressBard的效果动画
 * */
public class UProgress {
    private volatile static UProgress instance;
    private MyDialog myDialog;

    private UProgress() {
    }

    public static UProgress instance() {
        if (instance == null) {
            synchronized (UProgress.class) {
                if (instance == null) {
                    instance = new UProgress();
                }
            }
        }
        return instance;
    }

    public void stopProgressDialog() {
        if (myDialog != null) {
            if (myDialog.isShowing()) {
                try {
                    myDialog.dismiss();
                } catch (Exception e) {
                    ULog.d(e.toString());
                }
            }
            myDialog = null;
        }
    }

    /**
     * @param text 修改加载文字
     */
    public void setProgressText(Context context, String text) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.setMsg(text);
        } else {
            startProgressDialog(context, text);
        }
    }

    public void startProgressDialog(Context context) {
        this.startProgressDialog(context, "loading...");
    }

    public void startProgressDialog(Context context, String content) {
        if (myDialog != null) {
            stopProgressDialog();
        }
        myDialog = new MyDialog(context);
        myDialog.setMsg(content);
        if (!((Activity) context).isFinishing()) {
            try {
                myDialog.show();
            } catch (Exception e) {
                ULog.d(e.toString());
            }
        }
    }

    private class MyDialog extends Dialog {
        private TextView tv;

        MyDialog(Context context) {
            super(context, R.style.SakuraDialog);
            setContentView(R.layout.dia_progress);
            setCancelable(false);
            tv = findViewById(R.id.tv);
        }

        void setMsg(String content) {
            tv.setText(content);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }
}
