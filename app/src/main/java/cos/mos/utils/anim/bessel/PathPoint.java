package cos.mos.utils.anim.bessel;

/**
 * @Description 移动轨迹的坐标点
 * @Author Kosmos
 * @Date 2019.03.14 17:52
 * @Email KosmoSakura@gmail.com
 * 参考：https://www.jianshu.com/p/f64c3cd25f67
 */
public class PathPoint {
    public float endX, endY;//View移动到的最终位置
    float control1X, control1Y;//控制点1
    float control2X, control2Y;//控制点2
    int operation;//操作符

    /**
     * @param operation 操作符
     * @param endX      终点的坐标
     * @param endY      终点的坐标
     * @apiNote Line/Move都通过该构造函数来创建
     */
    PathPoint(int operation, float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
        this.operation = operation;
    }

    /**
     * @param control1X 控制点的坐标
     * @param control1Y 控制点的坐标
     * @param endX      终点的坐标
     * @param endY      终点的坐标
     * @apiNote 二阶贝塞尔曲线（控件当前位置为起始点）
     */
    PathPoint(float control1X, float control1Y, float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
        this.control1X = control1X;
        this.control1Y = control1Y;
        this.operation = PathCode.SECOND_CURVE;
    }

    /**
     * @param control1X 1号控制点的坐标
     * @param control1Y 1号控制点的坐标
     * @param control2X 2号控制点的坐标
     * @param control2Y 2号控制点的坐标
     * @param endX      终点的坐标
     * @param endY      终点的坐标
     * @apiNote 三阶贝塞尔曲线（控件当前位置为起始点）
     */
    PathPoint(float control1X, float control1Y, float control2X, float control2Y, float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
        this.control1X = control1X;
        this.control1Y = control1Y;
        this.control2X = control2X;
        this.control2Y = control2Y;
        this.operation = PathCode.THIRD_CURVE;
    }
}
