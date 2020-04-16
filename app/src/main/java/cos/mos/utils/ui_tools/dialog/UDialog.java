package cos.mos.utils.ui_tools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.R;


/**
 * @Description 弹出统一对话框
 * @Author Kosmos
 * @Date 2018年07月06日 14:11
 * @Email KosmoSakura@gmail.com
 * @tip 2018.9.2 基础弹窗
 * @tip 2018.9.12 函数封装
 * @tip 2019.3.21 解耦、构建封装
 * @tip 2019.11.7 优化显示尺寸
 * @tip 2020.4.17 支持定制颜色(通过UHtml设置)
 */
public class UDialog extends Dialog {
    private CharSequence strTitle, strMsg, strHint, strConfirm, strCancle;
    private int iconRes, gravity = Gravity.CENTER;
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
        setContentView(R.layout.dia_dialog);
        setCancelable(cancelable);//是否可以通过返回键关闭
        setCanceledOnTouchOutside(cancelable);//是否可以点击外面关闭

//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadius(12);
//        drawable.setColor(ContextCompat.getColor(getContext(), R.color.T_all));
//        this.getWindow().setBackgroundDrawable(drawable);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lp);
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
    public static UDialog builder(Context context, CharSequence msg) {
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
    public UDialog title(CharSequence title) {
        this.strTitle = title;
        return this;
    }

    /**
     * @param msg 通知内容
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog msg(CharSequence msg) {
        this.strMsg = msg;
        return this;
    }

    /**
     * @param msg     通知内容
     * @param gravity 文字显示模式
     */
    public UDialog msg(CharSequence msg, int gravity) {
        this.strMsg = msg;
        this.gravity = gravity;
        return this;
    }

    /**
     * @param hint 输入框提示
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog input(CharSequence hint) {
        this.strHint = hint;
        return this;
    }

    /**
     * @param hint     输入框提示
     * @param password 是否是密码模式
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialog input(CharSequence hint, boolean password) {
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
    public UDialog button(CharSequence confirm) {
        this.strConfirm = confirm;
        return this;
    }

    /**
     * @param confirm 确认按钮
     * @param cancle  取消按钮
     * @apiNote 不调用、或传入空 则不显示该区域
     * 两个按钮
     */
    public UDialog button(CharSequence confirm, CharSequence cancle) {
        this.strConfirm = confirm;
        this.strCancle = cancle;
        return this;
    }

    public UDialog buttonCancle(CharSequence cancle) {
        this.strCancle = cancle;
        return this;
    }

    public UDialog buttonConfirm(CharSequence confirm) {
        this.strConfirm = confirm;
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
    public void build(final ConfirmClick confirmClick) {
        //标题
        TextView title = findViewById(R.id.dia_title);
        if (UText.isEmpty(strTitle)) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
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
            msg.setGravity(gravity);
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
        TextView cancel = findViewById(R.id.dia_cancel);
        TextView confirm = findViewById(R.id.dia_confirm);
        //取消、确认按钮:至少要显示一个
        if (UText.isNotEmpty(strCancle) && UText.isNotEmpty(strConfirm)) {
            cancel.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            cancel.setBackgroundResource(R.drawable.arc_full_white_r4_cor_lb);
            confirm.setBackgroundResource(R.drawable.arc_full_blue_r4_cor_rb);
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
            confirm.setText(UText.isEmpty(strConfirm) ? UText.isNull(strCancle, "确定") : strConfirm);
            confirm.setBackgroundResource(R.drawable.arc_full_blue_r4_cor_b);
        }
        confirm.setOnClickListener(v -> {
            if (confirmClick == null) {
                clear();
            } else {
                confirmClick.onConfirmClick(UText.isNull(edt.getText().toString(), ""), UDialog.this);
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
