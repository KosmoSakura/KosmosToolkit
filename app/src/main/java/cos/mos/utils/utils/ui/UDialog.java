package cos.mos.utils.utils.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cos.mos.utils.R;
import cos.mos.utils.utils.java.UText;


/**
 * @Description: 弹出统一对话框
 * @Author: Kosmos
 * @Date: 2018年07月06日 14:11
 * @Email: KosmoSakura@gmail.com
 * @eg 2018.9.2 基础弹窗
 * @eg 2018.9.12 函数封装
 * @eg 2019.3.21 解耦、构建封装
 */
public class UDialog extends Dialog {
    private String strTitle, strMsg, strHint, strConfirm, strCancle;
    private int iconRes;
    private boolean password;//密码模式
    private CancelClick cancelClick;

    public interface ConfirmClick {
        void onConfirmClick(String result, Dialog dia);
    }

    public interface CancelClick {
        void onCancelClick(Dialog dia);
    }

    private UDialog(Context context, boolean cancelable) {
        super(context, R.style.SakuraDialog);
        setContentView(R.layout.lay_dialog);
        setCancelable(cancelable);//是否可以通过返回键关闭
        setCanceledOnTouchOutside(cancelable);//是否可以点击外面关闭
        getWindow().setBackgroundDrawableResource(R.color.T_all);
        if (cancelable) {
            findViewById(R.id.dia_root).setOnClickListener(v -> clear());
        }
        iconRes = -1;
        strTitle = "";
        strMsg = "";
        strHint = "";
        strConfirm = "";
        strCancle = "";
        password = false;
        cancelClick = null;
    }

    /**
     * @apiNote 可以通过返回键、点击外面关闭
     */
    public static UDialog builder(Context context) {
        return new UDialog(context, true);
    }

    /**
     * @param msg 通知内容
     * @apiNote 可以通过返回键、点击外面关闭
     * 为了调用简单，集成通知内容
     */
    public static UDialog builder(Context context, String msg) {
        return new UDialog(context, true).msg(msg);
    }

    /**
     * @param cancelable 是否可以通过返回键、点击外面关闭
     */
    public static UDialog builder(Context context, boolean cancelable) {
        return new UDialog(context, cancelable);
    }

    /**
     * @param title 标题
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog title(String title) {
        this.strTitle = title;
        return this;
    }

    /**
     * @param msg 通知内容
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog msg(String msg) {
        this.strMsg = msg;
        return this;
    }

    /**
     * @param hint 输入框提示
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog input(String hint) {
        this.strHint = hint;
        return this;
    }

    /**
     * @param hint     输入框提示
     * @param password 是否是密码模式
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog input(String hint, boolean password) {
        this.strHint = hint;
        this.password = password;
        return this;
    }

    /**
     * @param icon 显示图标
     * @apiNote 不调用、或-1 则不显示该区域
     */
    public UDialog icon(@DrawableRes int icon) {
        this.iconRes = icon;
        return this;
    }

    /**
     * @apiNote 显示两个默认按钮
     * 两个按钮
     */
    public UDialog button() {
        this.strConfirm = "Confirm";
        this.strCancle = "Cancel";
        return this;
    }

    /**
     * @param confirm 取消按钮
     * @apiNote 不调用、或传入空 则不显示该区域
     * 一个按钮
     */
    public UDialog button(String confirm) {
        this.strConfirm = confirm;
        return this;
    }

    /**
     * @param confirm 确认按钮
     * @param cancle  取消按钮
     * @apiNote 不调用、或传入空 则不显示该区域
     * 两个按钮
     */
    public UDialog button(String confirm, String cancle) {
        this.strConfirm = confirm;
        this.strCancle = cancle;
        return this;
    }

    /**
     * @param cancelClick 取消按钮点击事件
     * @apiNote 不调用、或传入空 则自动处理
     * 一个按钮
     */
    public UDialog cancelClick(CancelClick cancelClick) {
        this.cancelClick = cancelClick;
        return this;
    }

    /**
     * @apiNote 开始构建对话框
     */
    public void build() {
        build(null);
    }

    /**
     * @param confirmClick 确认按钮点击事件
     * @apiNote 不调用、或传入空 则自动处理
     * 开始构建对话框
     */
    public void build(ConfirmClick confirmClick) {
        //标题
        TextView title = findViewById(R.id.dia_title);
        View lineTop = findViewById(R.id.dia_line_top);
        if (UText.isEmpty(strTitle)) {
            title.setVisibility(View.GONE);
            lineTop.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            lineTop.setVisibility(View.VISIBLE);
            title.setText(strTitle);
        }
        //图标
        ImageView icon = findViewById(R.id.dia_icon);
        if (iconRes == -1) {
            icon.setVisibility(View.GONE);
        } else {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(iconRes);
        }
        //通知内容
        TextView msg = findViewById(R.id.dia_msg);
        if (UText.isEmpty(strMsg)) {
            msg.setVisibility(View.GONE);
        } else {
            msg.setText(strMsg);
            msg.setVisibility(View.VISIBLE);
        }
        //输入框
        EditText edt = findViewById(R.id.dia_edt);
        if (UText.isEmpty(strHint)) {
            edt.setVisibility(View.GONE);
        } else {
            edt.setVisibility(View.VISIBLE);
            edt.setHint(strHint);
            edt.setText("");
            edt.setTransformationMethod(password ? PasswordTransformationMethod.getInstance()
                : HideReturnsTransformationMethod.getInstance());
        }
        //取消、确认按钮:至少要显示一个
        TextView cancel = findViewById(R.id.dia_cancel);
        TextView confirm = findViewById(R.id.dia_confirm);
        View lineBottom = findViewById(R.id.dia_line_bottom);
        if (!UText.isEmpty(strCancle) && !UText.isEmpty(strConfirm)) {
            cancel.setVisibility(View.VISIBLE);
            lineBottom.setVisibility(View.VISIBLE);
            cancel.setText(strCancle);
            confirm.setText(strConfirm);
            cancel.setOnClickListener(v -> {
                if (cancelClick == null) {
                    clear();
                } else {
                    cancelClick.onCancelClick(UDialog.this);
                }
            });
        } else {
            cancel.setVisibility(View.GONE);
            lineBottom.setVisibility(View.GONE);
            confirm.setText(UText.isNull(strConfirm, "Confirm"));
        }
        confirm.setOnClickListener(view -> {
            if (confirmClick == null) {
                clear();
            } else {
                confirmClick.onConfirmClick(UText.isNull(edt.getText().toString()), UDialog.this);
            }
        });
        show();
    }

    /**
     * @apiNote 隐藏并释放资源
     */
    private void clear() {
        dismiss();
    }
}
