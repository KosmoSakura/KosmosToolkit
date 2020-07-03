package cos.mos.utils.anim.bessel;


import android.animation.TypeEvaluator;

/**
 * @Description 估值器 坐标点计算
 * @Author Kosmos
 * @Date 2019.03.14 17:52
 * @Email KosmoSakura@gmail.com
 * 参考：https://www.jianshu.com/p/f64c3cd25f67
 */
public class PathEvaluator implements TypeEvaluator<PathPoint> {

    /**
     * @param fraction   :数值变化值的比例
     * @param startValue : 起点
     * @param endValue    终点
     * @return 计算后的运动点
     */
    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
        float x, y;
        float oneMiunsT = 1 - fraction;
        //三阶贝塞尔曲线 Bessel
        if (endValue.operation == PathCode.THIRD_CURVE) {
            x = startValue.endX * oneMiunsT * oneMiunsT * oneMiunsT + 3 * endValue.control1X * fraction * oneMiunsT * oneMiunsT + 3 * endValue.control2X * fraction * fraction * oneMiunsT + endValue.endX * fraction * fraction * fraction;
            y = startValue.endY * oneMiunsT * oneMiunsT * oneMiunsT + 3 * endValue.control1Y * fraction * oneMiunsT * oneMiunsT + 3 * endValue.control2Y * fraction * fraction * oneMiunsT + endValue.endY * fraction * fraction * fraction;
        }//二阶贝塞尔曲线
        else if (endValue.operation == PathCode.SECOND_CURVE) {
            x = oneMiunsT * oneMiunsT * startValue.endX + 2 * fraction * oneMiunsT * endValue.control1X + fraction * fraction * endValue.endX;
            y = oneMiunsT * oneMiunsT * startValue.endY + 2 * fraction * oneMiunsT * endValue.control1Y + fraction * fraction * endValue.endY;
        }/* //圆周
        else if (endValue.operation == PathCode.CIRCLE) {
            x = (float) (startValue.endX + Math.sin(fraction) * (endValue.endX - startValue.endX));
            y = (float) (startValue.endY + Math.sin(fraction) * (endValue.endY - startValue.endY));
        }*///直线
        else if (endValue.operation == PathCode.LINE) {
            //x起始点+t*起始点和终点的距离
            x = startValue.endX + fraction * (endValue.endX - startValue.endX);
            y = startValue.endY + fraction * (endValue.endY - startValue.endY);
        } else {
            x = endValue.endX;
            y = endValue.endY;
        }
        return new PathPoint(PathCode.MOVE, x, y);
    }
}
