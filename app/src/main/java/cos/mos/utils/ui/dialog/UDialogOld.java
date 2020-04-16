package cos.mos.utils.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cos.mos.toolkit.R;
import cos.mos.toolkit.java.UText;

/**
 * @Description 弹出统一对话框
 * @Author Kosmos
 * @Date 2018年07月06日 14:11
 * @Email KosmoSakura@gmail.com
 * @Tip 2018.9.2 基础弹窗
 * @Tip 2018.9.12 函数封装
 */
@Deprecated
public class UDialogOld {
    private Dialog dialog;
    private EditText edt;
    private ImageView icon;
    private TextView msg, title, cancel, sure;

    public interface SureClick {
        void OnSureClick(String result, Dialog dia);
    }

    public interface CancelClick {
        void OnCancelClick(Dialog dia);
    }

    private SureClick sureClick;
    private CancelClick cancelClick;

    /**
     * @param Cancelable 是否可以通过返回键关闭
     * @param outside    是否可以点击外面关闭
     */
    public static UDialogOld getInstance(Context context, boolean Cancelable, boolean outside) {
        return new UDialogOld(context, Cancelable, outside);
    }

    private UDialogOld(Context context, boolean Cancelable, boolean outside) {
        View view = LayoutInflater.from(context).inflate(R.layout.dia_dialog, null);
        dialog = new Dialog(context, R.style.SakuraDialog);
        dialog.setCancelable(Cancelable);//是否可以通过返回键关闭
        dialog.setCanceledOnTouchOutside(outside);//是否可以点击外面关闭
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(R.color.t_all);

        title = view.findViewById(R.id.dia_title);
        icon = view.findViewById(R.id.dia_icon);
        msg = view.findViewById(R.id.dia_msg);
        edt = view.findViewById(R.id.dia_edt);
        cancel = view.findViewById(R.id.dia_cancel);
        sure = view.findViewById(R.id.dia_confirm);
        View root = view.findViewById(R.id.dia_root);
        root.setOnClickListener(v -> {
            if (outside) {
                clear();
            }
        });
    }

    /**
     * @param msg 通知内容
     * @apiNote 只有一个确认按钮，点击直接消失
     */
    public void showNoticeWithOnebtn(String msg) {
        show(null, msg, null, null, null, -1);
    }

    /**
     * @param msg     通知内容
     * @param strSure 确认按钮字样
     * @apiNote 只有一个确认按钮，点击直接消失
     */
    public void showNoticeWithOnebtn(String msg, String strSure) {
        show(null, msg, null, strSure, null, -1);
    }

    /**
     * @param msg     通知内容
     * @param strSure 确认按钮字样
     * @param res     图片
     * @apiNote 显示带图片的通知对话框, 有1个按钮，点击直接消失
     */
    public void showNoticeWithOnebtn(String msg, String strSure, int res) {
        show(null, msg, null, strSure, null, res);
    }

    /**
     * @param msg       通知内容
     * @param sureClick 确认按钮点击回调
     * @apiNote 只有一个确认按钮，点击回调
     */
    public void showNoticeWithOnebtn(String msg, SureClick sureClick) {
        this.sureClick = sureClick;
        show(null, msg, null, null, null, -1);
    }

    /**
     * @param msg       通知内容
     * @param strSure   确认按钮字样
     * @param sureClick 确认按钮点击回调
     * @apiNote 只有一个确认按钮，点击回调
     */
    public void showNoticeWithOnebtn(String msg, String strSure, SureClick sureClick) {
        this.sureClick = sureClick;
        show(null, msg, null, strSure, null, -1);
    }

    /**
     * @param title     标题
     * @param msg       通知内容
     * @param sureClick 确认按钮点击回调
     * @apiNote 有2个按钮，带标题，2个回调
     */
    public void showTitleSelectWithTwobtn(String title, String msg, SureClick sureClick, CancelClick cancelClick) {
        this.sureClick = sureClick;
        this.cancelClick = cancelClick;
        show(title, msg, null, "Confirm", "Cancel", -1);
    }

    /**
     * @param msg 通知内容
     * @apiNote 有两个按钮，点击直接消失
     */
    public void showNoticeWithTwobtn(String msg) {
        show(null, msg, null, "确认", "取消", -1);
    }

    /**
     * @param msg       通知内容
     * @param strCancel 取消按钮字样
     * @param strSure   确认按钮字样
     * @apiNote 有两个按钮，点击直接消失
     */
    public void showNoticeWithTwobtn(String msg, String strCancel, String strSure) {
        show(null, msg, null, strSure, strCancel, -1);
    }

    /**
     * @param msg       通知内容
     * @param sureClick 确认按钮点击回调
     * @apiNote 有2个按钮 确认按钮回调
     */
    public void showNoticeWithTwobtn(String msg, SureClick sureClick) {
        this.sureClick = sureClick;
        show(null, msg, null, "确认", "取消", -1);
    }


    /**
     * @param msg       通知内容
     * @param strCancel 取消按钮字样
     * @param strSure   确认按钮字样
     * @param sureClick 确认按钮点击回调
     * @apiNote 有2个按钮，确认按钮点击回调
     */
    public void showNoticeWithTwobtn(String msg, String strCancel, String strSure, SureClick sureClick) {
        show(null, msg, null, strSure, strCancel, -1);
    }

    /**
     * @param msg         通知内容
     * @param sureClick   确认按钮点击回调
     * @param cancelClick 取消按钮点击回调
     * @apiNote 有2个按钮 两个按钮都有回调
     */
    public void showSelectWithTwobtn(String msg, SureClick sureClick, CancelClick cancelClick) {
        this.sureClick = sureClick;
        this.cancelClick = cancelClick;
        show(null, msg, null, "确定", "取消", -1);
    }

    /**
     * @param msg       通知内容
     * @param strSure   确认按钮字样
     * @param strCancel 取消按钮字样
     * @apiNote 有2个按钮 两个按钮都有回调
     */
    public void showSelectWithTwobtn(String msg, String strSure, String strCancel, SureClick sureClick, CancelClick cancelClick) {
        this.sureClick = sureClick;
        this.cancelClick = cancelClick;
        show(null, msg, null, strSure, strCancel, -1);
    }


    /**
     * @param strTitle 对话框标题
     * @param hint     输入框提示语
     * @apiNote 显示带有输入框的对话框
     */
    public void showInput(String strTitle, String hint, SureClick sureClick) {
        this.sureClick = sureClick;
        show(strTitle, null, hint, null, null, -1);
    }

    private void show(String strTitle, String strMsg, String strHint, String strSure, String strCancel, int res) {
        if (UText.isEmpty(strTitle)) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(strTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (res > 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(res);
        } else {
            icon.setVisibility(View.GONE);
        }

        if (UText.isEmpty(strMsg)) {
            msg.setVisibility(View.GONE);
        } else {
            msg.setText(strMsg);
            msg.setVisibility(View.VISIBLE);
        }

        if (UText.isEmpty(strHint)) {
            edt.setVisibility(View.GONE);
        } else {
            edt.setHint(strHint);
            edt.setText("");
            edt.setVisibility(View.VISIBLE);
        }

        if (UText.isEmpty(strCancel)) {
            cancel.setVisibility(View.GONE);
            sure.setText(UText.isNull(strSure, "确定"));
        } else {
            cancel.setVisibility(View.VISIBLE);
            cancel.setText(UText.isNull(strCancel, "取消"));
            sure.setText(UText.isNull(strSure, "确定"));
        }

        cancel.setOnClickListener(view -> {
            if (cancelClick == null) {
                clear();
            } else {
                cancelClick.OnCancelClick(dialog);
            }
        });
        sure.setOnClickListener(view -> {
            if (sureClick == null) {
                clear();
            } else {
                sureClick.OnSureClick(UText.isNull(edt.getText().toString()), dialog);
            }
        });
        dialog.show();
    }

    private void clear() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
