package cos.mos.utils.anim.s_animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * @Description 属性动画示例(ObjectAnimator)
 * @Author Kosmos
 * @Date 2019.04.08 17:09
 * @Email KosmoSakura@gmail.com
 */
public class ObjAnimSample {
    private View view;//动画的目标控件

    private void run(Animator animator) {
        animator.setDuration(500);
        //变化率
        animator.setInterpolator(new LinearInterpolator());//线性变化
        animator.setInterpolator(new AccelerateInterpolator());//慢-快（加速变化）
        animator.setInterpolator(new DecelerateInterpolator());//快-慢（减速变化）
        animator.setInterpolator(new AccelerateDecelerateInterpolator());//慢-快-慢（先加速，再减速）
        animator.setInterpolator(new CycleInterpolator(5f));//动画循环播放给定的次数（正弦曲线变化）
        animator.setInterpolator(new AnticipateInterpolator());//开始的时候向后然后向前甩
        animator.setInterpolator(new AnticipateOvershootInterpolator());//开始的时候向后然后向前甩一定值后返回最后的值
        animator.setInterpolator(new BounceInterpolator());//动画结束的时候弹起
        animator.setInterpolator(new OvershootInterpolator());//向前甩一定值后再回到原来位置
        animator.start();
    }

    /**
     * ObjectAnimator
     */
    public void obj() {
        /**
         * 平移
         * translationX、translationY、
         * TranslationX、TranslationY
         * */
        ObjectAnimator translationX = ObjectAnimator
            .ofFloat(view, "translationX", 0, 300);
        //重复次数
        translationX.setRepeatCount(1);//重复1次=动画执行2次
        translationX.setRepeatCount(-1);//无限重复
        //重复方式
        translationX.setRepeatMode(ValueAnimator.REVERSE);//反向：来回一次

        /**
         * 透明度
         * alpha、Alpha
         * */
        ObjectAnimator alpha = ObjectAnimator
            .ofFloat(view, "alpha", 0f, 1f, 0);
        alpha.setRepeatCount(-1);
        /**
         * 旋转
         * rotation，
         * rotationX、rotationY、
         * RotationX、RotationY
         * */
        ObjectAnimator rotation = ObjectAnimator
            .ofFloat(view, "rotation", 0f, 360f, 0);
        alpha.setRepeatCount(-1);
        /**
         * 缩放：原控件大小的倍率
         * scaleX、scaleY
         * ScaleX、ScaleY
         * */
        ObjectAnimator scale = ObjectAnimator
            .ofFloat(view, "scaleX", 0f, 2.2f, 0);
        alpha.setRepeatCount(-1);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(translationX, alpha);
        animSet.play(scale).after(alpha).with(translationX);
        run(animSet);
    }

    private void clear(ObjectAnimator animator) {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }
}
