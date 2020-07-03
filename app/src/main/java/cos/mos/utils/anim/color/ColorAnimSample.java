package cos.mos.utils.anim.color;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.widget.TextView;

/**
 * @Description 颜色变化的属性动画示例
 * @Author Kosmos
 * @Date 2019.04.02 15:06
 * @Email KosmoSakura@gmail.com
 */
public class ColorAnimSample {
    private TextView text;//目标控件

    /**
     * @apiNote 假如这里是程序入口
     */
    public void main() {
        /**
         * 反射名
         * backgroundColor：背景色
         * textColor：文本色
         * */
        ValueAnimator animator = ObjectAnimator
            .ofInt(text,//目标控件
                "textColor",//修改属性
                //黑色→红色→蓝色→绿色
                0x88333833, 0x88ca0007, 0x880333dc, 0x88089905);
        animator.setDuration(2000);
        // 颜色估计器 须要ArgbEvaluator或它的子类，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }
}
