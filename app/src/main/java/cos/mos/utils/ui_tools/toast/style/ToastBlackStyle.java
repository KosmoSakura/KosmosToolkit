package cos.mos.utils.ui_tools.toast.style;

import android.view.Gravity;

import cos.mos.utils.ui_tools.toast.IToastStyle;


/**
 * author : HJQ
 * github : https://github.com/getActivity/ToastUtils
 * time   : 2018/09/01
 * desc   : 默认黑色样式实现
 */
public class ToastBlackStyle implements IToastStyle {
    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getZ() {
        return 30;
    }

    @Override
    public int getCornerRadius() {
        return 6;
    }

    @Override
    public int getBackgroundColor() {
        return 0X88000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return 14;
    }

    @Override
    public int getMaxLines() {
        return 3;
    }

    @Override
    public int getPaddingLeft() {
        return 24;
    }

    @Override
    public int getPaddingTop() {
        return 16;
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}