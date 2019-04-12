package cos.mos.toolkit.hardware;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @Description: 重力监听
 * @Author: Kosmos
 * @Date: 2019.01.28 11:56
 * @Email: KosmoSakura@gmail.com
 * 1.摇晃手机
 * 2.屏幕旋转
 */
public class UGravity implements SensorEventListener {
    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 2000;
    private static final int UPTATE_INTERVAL_TIME = 200;// 两次检测的时间间隔
    private SensorManager sorMgr;// 传感器管理器
    private OnShakeListener onShakeListener;//摇晃监听器
    private OrientationListener orientationListener;////旋转监听器
    private long lastUpdateTime;//上次检测时间
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;

    // 构造器
    public UGravity(Context context) {
        // 获得传感器管理器
        sorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // 注册
        if (sorMgr != null) {
            sorMgr.registerListener(this,
                sorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    // 摇晃监听接口
    public interface OnShakeListener {
        void onShake();
    }

    // 旋转监听接口
    public interface OrientationListener {
        void orientation(int orientation);
    }

    // 设置重力感应监听器
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    public void setOrientationListener(OrientationListener orientationListener) {
        this.orientationListener = orientationListener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /*
     * 重力感应器感应获得变化数据
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        //返回的不是重力传感信号
        if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
            return;
        }
        // 检测时间必须大于间隔时间
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;//时间间隔
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;
        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //摇晃监听
        if (onShakeListener != null) {
            // 获得x,y,z的变化值
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            // 将现在的坐标变成last坐标
            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
            // 达到速度阀值，发出提示
            if (speed >= SPEED_SHRESHOLD) {
                onShakeListener.onShake();
            }
        }
        //旋转监听
        if (orientationListener != null) {
            //理论上临界值应为4.5，但是实际效果有所偏差，值为6时效果最佳
            if (x < 6 && x > -6 && y > 6) {
                //竖屏:x∈（-4.5,4.5),y∈(4.5,+∞)
                orientationListener.orientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (x < 6 && x > -6 && y < -6) {
                //竖屏反转:x∈（-4.5,4.5）,y∈(-∞,-4.5)
                orientationListener.orientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            } else if (y > -6 && y < 6 && x > 6) {
                //横屏:x∈(4.5,+∞),y∈(-4.5,4.5)
                orientationListener.orientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                //横屏反转:x∈(-∞,-4.5),y∈(-4.5,4.5)
                orientationListener.orientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
//            //-6**6  y>6
//            if (x < 4.5 && x >= -4.5 && y > 4.5) {
//                newOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;//竖屏
//            } else if (x >= 4.5 && y < 4.5 && y >= 4.5) {
//                //x>6 y -4.5**5
//                newOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横屏
//            } else if (x <= -4.5 && y < 4.5 && y >= -4.5) {
//                //x,-6  Y -3**5
//                newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;//横屏反转
//            } else {
//                //x -5**5  y<-6
//                newOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;//竖屏反转
//            }
        }
    }

    /**
     * 资源清理，注销监听
     */
    public void clear() {
        if (sorMgr != null) {
            sorMgr.unregisterListener(this);
            sorMgr = null;
        }
        if (onShakeListener != null) {
            onShakeListener = null;
        }
        if (orientationListener != null) {
            orientationListener = null;
        }
    }
}
