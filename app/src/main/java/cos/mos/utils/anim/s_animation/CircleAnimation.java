package cos.mos.utils.anim.s_animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @Description: 补间动画：圆周运动动画效果(比如放大镜圈圈动画）
 * @Author: 作者地址：https://blog.csdn.net/niu0147/article/details/52292541
 * @Date: 2018.11.01 15:11
 * @apiNote 使用示例见AnimationSample.circle()
 */
public class CircleAnimation extends Animation {
    private int radius;

    /**
     * @param radius 圆弧半径
     */
    public CircleAnimation(int radius) {
        this.radius = radius;
    }

    /**
     * @param interpolatedTime 在插值函数运行后的归一化时间（0.0到1.0）*的值。
     * @param transformation   Transformation对象用当前的ransforms填充。
     * @apiNote getTransformation的助手。
     * 子类实现它以在给定插值的情况下应用它们的变换.
     * 此方法的实现应始终替换它们正在执行的指定转换或文档
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        //根据取值范围 确定圆周运动的角度范围。0-360
//        float d = 360 * interpolatedTime ;//+ 180;  //算法一
        float d = 360 * interpolatedTime + 180;       //算法二
        if (d > 360) { //算法二
            d = d - 360;
        }
        //根据取值范围 确定圆周运动的角度范围。0-360
        int[] ps = getNewLocation((int) d, radius, 0, 0);
        transformation.getMatrix().setTranslate(ps[0], ps[1] - radius);
    }

    /**
     * @param newAngle 当前临时角度
     * @param r        半径
     * @param width    控件的宽
     * @param height   控件的高
     */
    private int[] getNewLocation(int newAngle, int r, int width, int height) {
        int newAngle1;
        int newX = 0, newY = 0;
        // 0-90
        if (newAngle == 0) {
            newX = width;
            newY = height - r;
        } else if (newAngle == 90) {
            newX = width + r;
            newY = height;
        } else if (newAngle == 180) {
            newX = width;
            newY = height + r;
        } else if (newAngle == 270) {
            newX = width - r;
            newY = height;
        } else if (newAngle == 360) {
            newX = width;
            newY = height - r;
        } else if (newAngle > 360) {
            newX = width;
            newY = height - r;
        } else if (newAngle > 0 && newAngle < 90) {
            // 0-90  baidu：就拿sin30°为列：Math.sin(30*Math.PI/180)
            // 思路为PI相当于π，而此时的PI在角度值里相当于180°，
            // 所以Math.PI/180得到的结果就是1°，然后再乘以30就得到相应的30°
            newX = (int) (width + (r * Math.sin(newAngle * Math.PI / 180)));
            newY = (int) (height - (r * Math.cos(newAngle * Math.PI / 180)));
        } else if (newAngle > 90 && newAngle < 180) {// 90-180
            newAngle1 = 180 - newAngle;
            newX = (int) (width + (r * Math.sin(newAngle1 * Math.PI / 180)));
            newY = (int) (height + (r * Math.cos(newAngle1 * Math.PI / 180)));
        } else if (newAngle > 180 && newAngle < 270) {//180-270
            newAngle1 = 270 - newAngle;
            newX = (int) (width - (r * Math.cos(newAngle1 * Math.PI / 180)));
            newY = (int) (height + (r * Math.sin(newAngle1 * Math.PI / 180)));
        } else if (newAngle > 270 && newAngle < 360) {//270-360
            newAngle1 = 360 - newAngle;
            newX = (int) (width - (r * Math.sin(newAngle1 * Math.PI / 180)));
            newY = (int) (height - (r * Math.cos(newAngle1 * Math.PI / 180)));
        }
        return new int[]{newX, newY};
    }
}
