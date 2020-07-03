package cos.mos.utils.anim.bessel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 不同类型移动轨迹的绘制方法
 * @Author Kosmos
 * @Date 2019.03.14 17:52
 * @Email KosmoSakura@gmail.com
 * 参考：https://www.jianshu.com/p/f64c3cd25f67
 */
public class AnimatorPath {
    /**
     * 一系列的轨迹记录动作
     */
    private List<PathPoint> points = new ArrayList<>();

    /**
     * @param x 终点的坐标
     * @param y 终点的坐标
     * @apiNote 移动位置到（控件当前位置为起始点）
     */
    public void moveTo(float x, float y) {
        points.add(new PathPoint(PathCode.MOVE, x, y));
    }

    /**
     * @param x 终点的坐标
     * @param y 终点的坐标
     * @apiNote 一阶贝塞尔-直线移动（控件当前位置为起始点）
     */
    public void lineTo(float x, float y) {
        points.add(new PathPoint(PathCode.LINE, x, y));
    }

    /**
     * @param x 终点的坐标
     * @param y 终点的坐标
     * @apiNote 圆周运动（控件当前位置为起始点）
     */
//    public void circleTo(float x, float y) {
//        points.add(new PathPoint(PathCode.CIRCLE, x, y));
//    }

    /**
     * @param c1X 控制点的坐标
     * @param c1Y 控制点的坐标
     * @param x   终点的坐标
     * @param y   终点的坐标
     * @apiNote 二阶贝塞尔曲线移动（控件当前位置为起始点）
     */
    public void secondBesselCurveTo(float c1X, float c1Y, float x, float y) {
        points.add(new PathPoint(c1X, c1Y, x, y));
    }

    /**
     * @param c1X 1号控制点的坐标
     * @param c1Y 1号控制点的坐标
     * @param c2X 2号控制点的坐标
     * @param c2Y 2号控制点的坐标
     * @param x   终点的坐标
     * @param y   终点的坐标
     * @apiNote 三阶贝塞尔曲线移动
     */
    public void thirdBesselCurveTo(float c1X, float c1Y, float c2X, float c2Y, float x, float y) {
        points.add(new PathPoint(c1X, c1Y, c2X, c2Y, x, y));
    }

    /**
     * @return 返回移动动作集合
     */
    public Collection<PathPoint> getPoints() {
        return points;
    }
}
