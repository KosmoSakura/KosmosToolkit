package cos.mos.utils.anim.s_animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @Description: 属性动画示例(ValueAnimator)
 * @Author: Kosmos
 * @Date: 2019.04.08 19:25
 * @Email: KosmoSakura@gmail.com
 */
public class ValueAnimSample {
    private ValueAnimator valueAnimator;
    private View view;//动画的目标控件

    public void sample() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(-60, 60);
        valueAnimator.setDuration(500);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(animation -> {
            float degree = (Float) animation.getAnimatedValue();//度
            view.setRotation(degree);
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                //动画取消回调
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束回调
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //动画重复回调
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始回调
            }

            @Override
            public void onAnimationPause(Animator animation) {
                //动画暂停回调
            }

            @Override
            public void onAnimationResume(Animator animation) {
                //动画恢复回调
            }
        });
        valueAnimator.start();
    }

    private void clear() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
    }
}
