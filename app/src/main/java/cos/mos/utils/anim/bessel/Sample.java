package cos.mos.utils.anim.bessel;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @Description: 演示栗子
 * @Author: Kosmos
 * @Date: 2019.03.15 10:13
 * @Email: KosmoSakura@gmail.com
 */
public class Sample {
    private AnimatorPath path;//轨迹动画路径
    private View vAnim;//动画 的目标控件
    private View cv;//这是一个参考控件，用于计算动画轨迹

    /**
     * @apiNote 假如这里是程序入口
     */
    public void main() {
        initPath();
        animRun();
    }

    /**
     * @apiNote 计算动画路径
     */
    private void initPath() {
        //设置动画路径
        if (path == null) {
            path = new AnimatorPath();
        }
        int r = (cv.getRight() - cv.getLeft()) / 2;//圆半径


        //这是一个'∞'形状的轨迹
        path.moveTo(0, 0);
        path.thirdBesselCurveTo(-r * 0.7f, -r * 1.7f, r * 2.7f, r * 2.7f, r * 2, 0);
        path.thirdBesselCurveTo(r * 2.7f, -r * 1.7f, -r * 0.7f, r * 1.7f, 0, 0);
    }

    /**
     * @apiNote 启动动画
     */
    private void animRun() {
        ObjectAnimator anim = ObjectAnimator.ofObject(this, "sakura",
            new PathEvaluator(), path.getPoints().toArray());
        anim.setInterpolator(new LinearInterpolator());//线性变化
//        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(700);
        anim.setRepeatCount(-1);
        anim.start();
    }

    /**
     * ObjectAnimator.ofObject()中的"sakura"参数通过反射机制调用setSakura()
     * 当在当前类中定义了该方法,就会自动通过反射的机制来调用该方法!
     */
    public void setSakura(PathPoint newLoc) {
        vAnim.setTranslationX(newLoc.endX);
        vAnim.setTranslationY(newLoc.endY);
    }
}
