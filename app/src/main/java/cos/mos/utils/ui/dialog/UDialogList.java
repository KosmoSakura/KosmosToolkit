package cos.mos.utils.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.R;


/**
 * @Description 弹出统一对话框
 * @Author Kosmos
 * @Date 2018年07月06日 14:11
 * @Email KosmoSakura@gmail.com
 * @Tip 2018.9.2 基础弹窗
 * @Tip 2018.9.12 函数封装
 * @Tip 2019.3.21 解耦、构建封装
 * @Tip 2019.11.8 封装列表对话框，内容除了列表和UDialog一样，因为太重，顾单独拉出来
 */
public class UDialogList extends Dialog {
    private String strTitle, strMsg, strHint, strConfirm, strCancle;
    private int resIcon;
    private int resConfirmBtn = R.drawable.line_alpha_top_gray;
    private int resCancelBtn = R.drawable.line_alpha_top_gray;
    private int colorConfirmBtn = Color.parseColor("#20A4F8");
    private int colorCancelBtn = Color.parseColor("#18202C");
    private int gravity = Gravity.CENTER;
    private boolean password;//密码模式
    private CancelClick cancelClick;
    private DialogAdapter adapter;
    private List<DialogBean> list;
    private BaseQuickAdapter.OnItemClickListener listener;

    public interface ConfirmClick {
        void onConfirmClick(String result, Dialog dia);
    }

    public interface CancelClick {
        void onCancelClick(Dialog dia);
    }

    private UDialogList(Context context, boolean cancelable) {
        super(context, R.style.SakuraDialog);
        setContentView(R.layout.dia_dialog);
        setCancelable(cancelable);//是否可以通过返回键关闭
        setCanceledOnTouchOutside(cancelable);//是否可以点击外面关闭
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(lp);
        resIcon = -1;
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
    public static UDialogList builder(Context context) {
        return new UDialogList(context, true);
    }

    /**
     * @param msg 通知内容
     * @apiNote 可以通过返回键、点击外面关闭
     * 为了调用简单，集成通知内容
     */
    public static UDialogList builder(Context context, String msg) {
        return new UDialogList(context, true).msg(msg);
    }

    /**
     * @param cancelable 是否可以通过返回键、点击外面关闭
     */
    public static UDialogList builder(Context context, boolean cancelable) {
        return new UDialogList(context, cancelable);
    }

    /**
     * @param title 标题
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialogList title(String title) {
        this.strTitle = title;
        return this;
    }

    /**
     * @param msg 通知内容
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialogList msg(String msg) {
        this.strMsg = msg;
        return this;
    }

    /**
     * @param msg     通知内容
     * @param gravity 文字显示模式
     */
    public UDialogList msg(String msg, int gravity) {
        this.strMsg = msg;
        this.gravity = gravity;
        return this;
    }

    /**
     * @param hint 输入框提示
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialogList input(String hint) {
        this.strHint = hint;
        return this;
    }

    /**
     * @param hint     输入框提示
     * @param password 是否是密码模式
     * @apiNote 不调用、或传入空 则不显示该区域
     */
    public UDialogList input(String hint, boolean password) {
        this.strHint = hint;
        this.password = password;
        return this;
    }

    /**
     * @param icon 显示图标
     * @apiNote 不调用、或-1 则不显示该区域
     */
    public UDialogList icon(@DrawableRes int icon) {
        this.resIcon = icon;
        return this;
    }

    /**
     * @apiNote 显示两个默认按钮
     * 两个按钮
     */
    public UDialogList button() {
        this.strConfirm = "Confirm";
        this.strCancle = "Cancel";
        return this;
    }

    /**
     * @param confirm 取消按钮
     * @apiNote 不调用、或传入空 则不显示该区域
     * 一个按钮
     */
    public UDialogList button(String confirm) {
        this.strConfirm = confirm;
        return this;
    }

    /**
     * @param confirm 确认按钮
     * @param cancle  取消按钮
     * @apiNote 不调用、或传入空 则不显示该区域
     * 两个按钮
     */
    public UDialogList button(String confirm, String cancle) {
        this.strConfirm = confirm;
        this.strCancle = cancle;
        return this;
    }

    public UDialogList buttonCancle(String cancle) {
        this.strCancle = cancle;
        return this;
    }

    /**
     * @param listener 初始化Dialog列表，如果没有调用该方法，列表则不会显示
     */
    public UDialogList initList(BaseQuickAdapter.OnItemClickListener listener) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.listener = listener;
        return this;
    }

    /**
     * 刷新列表
     */
    public void notify(List<DialogBean> data) {
        this.list.clear();
        if (data != null) {
            this.list.addAll(data);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 确认取消按钮背景，一般不用调用，使用默认的就好
     */
    public UDialogList buttonBackground(int resConfirm, int resCancel) {
        this.resConfirmBtn = resConfirm;
        this.resCancelBtn = resCancel;
        return this;
    }

    /**
     * @param confirm      按钮文字
     * @param resConfirm   按钮背景
     * @param colorConfirm 按钮文字颜色
     * @Tip 确认按钮样式, 一般不用调用，使用默认的就好
     */
    public UDialogList buttonStyleConfirm(String confirm, int colorConfirm, int resConfirm) {
        this.strConfirm = confirm;
        this.colorConfirmBtn = colorConfirm;
        this.resConfirmBtn = resConfirm;
        return this;
    }

    /**
     * @param cancel      按钮文字
     * @param resCancel   按钮背景
     * @param colorCancel 按钮文字颜色
     * @Tip 返回按钮样式, 一般不用调用，使用默认的就好
     */
    public UDialogList buttonStyleCancel(String cancel, int colorCancel, int resCancel) {
        this.strCancle = cancel;
        this.colorCancelBtn = colorCancel;
        this.resCancelBtn = resCancel;
        return this;
    }

    public UDialogList buttonConfirm(String confirm) {
        this.strConfirm = confirm;
        return this;
    }

    /**
     * @param cancelClick 取消按钮点击事件
     * @apiNote 不调用、或传入空 则自动处理
     * 一个按钮
     */
    public UDialogList cancelClick(CancelClick cancelClick) {
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
        if (resIcon == -1) {
            icon.setVisibility(View.GONE);
        } else {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(resIcon);
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
        //列表
        RecyclerView rcv = findViewById(R.id.dia_list);
        if (UText.isEmpty(list)) {
            rcv.setVisibility(View.GONE);
        } else {
            adapter = new DialogAdapter(list);
            rcv.setVisibility(View.VISIBLE);
            rcv.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv.setAdapter(adapter);
            if (listener != null) {
                adapter.setOnItemClickListener(listener);
            }
        }

        TextView cancel = findViewById(R.id.dia_cancel);
        TextView confirm = findViewById(R.id.dia_confirm);

        //取消、确认按钮:至少要显示一个
        if (UText.isEmpty(strCancle) && UText.isEmpty(strConfirm)) {
            cancel.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
            confirm.setTextColor(colorConfirmBtn);
            confirm.setBackgroundResource(resConfirmBtn);
            confirm.setText(UText.isNull(strConfirm, "Confirm"));
            confirm.setOnClickListener(v -> clear());
        } else {
            if (UText.isEmpty(strCancle)) {
                cancel.setVisibility(View.GONE);
            } else {
                cancel.setVisibility(View.VISIBLE);
                cancel.setText(strCancle);
                cancel.setTextColor(colorCancelBtn);
                cancel.setBackgroundResource(resCancelBtn);
                cancel.setOnClickListener(v -> {
                    if (cancelClick == null) {
                        clear();
                    } else {
                        cancelClick.onCancelClick(UDialogList.this);
                    }
                });
            }
            if (UText.isEmpty(strConfirm)) {
                confirm.setVisibility(View.GONE);
            } else {
                confirm.setVisibility(View.VISIBLE);
                confirm.setText(strConfirm);
                confirm.setTextColor(colorConfirmBtn);
                confirm.setBackgroundResource(resConfirmBtn);
                confirm.setOnClickListener(v -> {
                    if (confirmClick == null) {
                        clear();
                    } else {
                        confirmClick.onConfirmClick(UText.isNull(edt.getText().toString(), ""), UDialogList.this);
                    }
                });
            }
        }
        show();
    }

    /**
     * @apiNote 隐藏并释放资源
     */
    private void clear() {
        dismiss();
    }
}
