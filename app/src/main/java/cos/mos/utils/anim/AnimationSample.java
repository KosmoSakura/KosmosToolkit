package cos.mos.utils.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * @Description: 补间动画示例
 * @Author: Kosmos
 * @Date: 2019.04.08 16:59
 * @Email: KosmoSakura@gmail.com
 */
public class AnimationSample {
    private View view;//动画的目标控件

    /**
     * 旋转动画
     */
    public void rotate() {
        //默认旋转中心=控件中心
        RotateAnimation rotate = new RotateAnimation(0, 360);
        //旋转中心=相对于自己的百分比
        RotateAnimation rotate1 = new RotateAnimation(0, 360,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.95f);

        rotate.setDuration(500);
        rotate.setFillAfter(true);//动画结束后保持最后一帧的样子
        rotate.setFillBefore(true);//动画结束后保持第一帧的样子，这两个一般不用同时出现
        rotate.setRepeatCount(1);//重复1次=动画执行2次
        rotate.setRepeatCount(-1);//无限重复
        //线性变化
        rotate.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotate);
    }

    /**
     * 平移动画
     */
    public void translate() {
        TranslateAnimation translate = new TranslateAnimation(10, 100, 0, 0);

        translate.setDuration(1000);
        translate.setRepeatCount(-1);
        translate.setInterpolator(new LinearInterpolator());
    }

    /**
     * 清除补间动画
     */
    public void clear() {
        view.clearAnimation();
    }
}
