package cos.mos.utils.anim.bessel;

/**
 * @Description: 曲线类型常量
 * @Author: Kosmos
 * @Date: 2019.03.14 17:52
 * @Email: KosmoSakura@gmail.com
 */
public interface PathCode {
    int MOVE = 0;//起始点操作
    int LINE = 1;//直线路径操作
//    int CIRCLE = 2;//圆周路径操作
    int SECOND_CURVE = 3;//二阶贝塞尔曲线操作
    int THIRD_CURVE = 4;//三阶贝塞尔曲线操作
}
