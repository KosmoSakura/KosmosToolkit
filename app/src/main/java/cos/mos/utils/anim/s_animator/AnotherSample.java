package cos.mos.utils.anim.s_animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.v4.view.ViewCompat;
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

import cos.mos.utils.R;

/**
 * @Description <p>
 * @Author Kosmos
 * @Date 2019.04.08 19:41
 * @Email KosmoSakura@gmail.com
 */
public class AnotherSample {
    private View view;//动画的目标控件

    private void run(Animator animator) {
        animator.setDuration(500);
        //变化方式
        animator.setInterpolator(new LinearInterpolator());//线性变化
        animator.setInterpolator(new AccelerateDecelerateInterpolator());//慢-快-慢
        animator.setInterpolator(new AccelerateInterpolator());//慢-快
        animator.setInterpolator(new DecelerateInterpolator());//快-慢
        animator.setInterpolator(new AnticipateInterpolator());//开始的时候向后然后向前甩
        animator.setInterpolator(new AnticipateOvershootInterpolator());//开始的时候向后然后向前甩一定值后返回最后的值
        animator.setInterpolator(new BounceInterpolator());//动画结束的时候弹起
        animator.setInterpolator(new CycleInterpolator(5f));//动画循环播放特定的次数，速率改变沿着正弦曲线
        animator.setInterpolator(new OvershootInterpolator());//向前甩一定值后再回到原来位置
        animator.start();
    }

    /**
     * 载入资源动画
     */
    public void incluld(Context context) {
        Animator anim = AnimatorInflater.loadAnimator(context, R.animator.obj_translation);
//        view.setPivotX(0);
//        view.setPivotY(0);
        //显示的调用invalidate
//        view.invalidate();
        anim.setTarget(view);
        run(anim);
    }

    /**
     * API 12以后，View增加了一个新的方法animate()，
     * 他用来返回一个ViewPropertyAnimation()的实例，
     * 并且可以对他进行一系列的链式操作
     */
    public void withLink() {
        view.animate()
            .alpha(1)
            .translationX(100)
            .y(30)
            .setDuration(300)
            .withStartAction(new Runnable() {
                @Override
                public void run() {

                }
            })
            .withEndAction(new Runnable() {
                @Override
                public void run() {

                }
            })
            .start();
    }

    /**
     * ViewCompat封装的
     */
    public void withSystem() {
        ViewCompat.animate(view)
            .rotationBy(360)
            .scaleX(100f)
            .scaleY(200f)
            .translationX(200f)
            .translationY(100f)
            .alpha(0.5f)
            .setDuration(500)
            .start();
    }
}
