package cos.mos.utils.anim.s_animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import cos.mos.utils.R;

/**
 * @Description: 补间动画示例
 * @Author: Kosmos
 * @Date: 2019.04.08 16:59
 * @Email: KosmoSakura@gmail.com
 */
public class AnimationSample {
    private View view;//动画的目标控件

    private void run(Animation animation) {
        animation.setDuration(500);
        //动画结束后状态
        animation.setFillAfter(true);//默认false,动画结束 保持最后一帧的样子
        animation.setFillBefore(true);//默认true,动画结束后保持第一帧的样子
        //重复次数
        animation.setRepeatCount(1);//重复1次=动画执行2次
        animation.setRepeatCount(-1);//无限重复
        //重复方式
        animation.setRepeatMode(Animation.REVERSE);//反向：来回一次
        //变化方式
        animation.setInterpolator(new LinearInterpolator());//线性变化
        view.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始回调
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束回调
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复回调
            }
        });
    }

    /**
     * 载入资源动画
     */
    public void incluld(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        run(animation);
    }

    /**
     * 补间动画：圆周运动动画效果(比如放大镜圈圈动画）
     */
    public void circle() {
        CircleAnimation circle = new CircleAnimation(20);
        run(circle);
    }


    /**
     * 平移动画
     * * Animation.ABSOLUTE:绝对坐标(单位像素)
     * * 假如100,就是相对于控件原点正方向偏移100个像素.
     * * -------------------------
     * * Animation.RELATIVE_TO_SELF:相对于自己
     * * 在该类型下值为float类型,比如0.5f,
     * * 相对于控件原点正方向偏移自身控件百分之五十长度.
     * * -------------------------
     * * Animation.RELATIVE_TO_PARENT:相对于父类
     * * 在该类型下值为float类型,比如0.5f,
     * * 就是相对于控件原点正方向偏移父控件百分之五十长度.
     */
    public void translate() {
        //TYPE=Animation.ABSOLUTE
        TranslateAnimation translate = new TranslateAnimation(10f, 100f, 0f, 0f);
        TranslateAnimation translate1 = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 10f, Animation.RELATIVE_TO_SELF, 100f,
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        run(translate);
    }

    /**
     * 旋转动画
     * * Animation.ABSOLUTE:旋转中心的绝对坐标(单位像素)
     * * 假如100,就是相对于控件原点正方向偏移100个像素.
     * * -------------------------
     * * Animation.RELATIVE_TO_SELF:旋转中心相对于自己
     * * 在该类型下值为float类型,比如0.5f,
     * * 相对于控件原点正方向偏移自身控件百分之五十长度.
     * * -------------------------
     * * Animation.RELATIVE_TO_PARENT:旋转中心相对于父类
     * * 在该类型下值为float类型,比如0.5f,
     * * 就是相对于控件原点正方向偏移父控件百分之五十长度.
     */
    public void rotate() {
        //旋转中心=(0.0f, 0.0f)
        RotateAnimation rotate = new RotateAnimation(0f, 360f);
        //旋转中心=手动传入，TYPE=Animation.ABSOLUTE
        RotateAnimation rotate1 = new RotateAnimation(0f, 360f, 1f, 1f);
        //旋转中心=相对于自己的百分比
        RotateAnimation rotate2 = new RotateAnimation(0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.95f);
        run(rotate);
    }

    /**
     * 缩放动画
     * * Animation.ABSOLUTE:拉伸中心的绝对坐标(单位像素)
     * * 假如100,就是相对于原点正方向偏移100个像素.
     * * -------------------------
     * * Animation.RELATIVE_TO_SELF:拉伸中心相对于自己
     * * 在该类型下值为float类型,比如0.5f,
     * * 相对于控件原点正方向偏移自身控件百分之五十长度.
     * * -------------------------
     * * Animation.RELATIVE_TO_PARENT:拉伸中心相对于父类
     * * 在该类型下值为float类型,比如0.5f,
     * * 就是相对于控件控件原点正方向偏移父控件百分之五十长度.
     */
    public void scale() {
        //拉伸中心=(0.0f, 0.0f),传入值为原控件对应参数的倍数
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f);
        //拉伸中心=手动传入,TYPE=Animation.ABSOLUTE
        ScaleAnimation scale1 = new ScaleAnimation(0f, 1f, 0f, 1f, 0, 0);
        //拉伸中心=相对于自己的百分比
        ScaleAnimation scale2 = new ScaleAnimation(0, 1f, 0, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        run(scale);
    }

    /**
     * 透明度动画
     */
    public void alpha() {
        AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.8f);
        run(alpha);
    }

    /**
     * 复合动画
     */
    public void set(AlphaAnimation alpha, ScaleAnimation scale) {
        // true:表明每一个动画对象都使用AnimationSet统一的变速器Interpolator
        // false:表明每一个动画都可以使用各自的变速器Interpolator
        AnimationSet set = new AnimationSet(true);
        //加载单个动画
        set.addAnimation(alpha);
        set.addAnimation(scale);
        run(set);
    }

    /**
     * 清除补间动画
     */
    public void clear() {
        view.clearAnimation();
    }
}
