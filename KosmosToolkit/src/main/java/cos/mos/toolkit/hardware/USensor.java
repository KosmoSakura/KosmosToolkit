package cos.mos.toolkit.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

import cos.mos.toolkit.init.KApp;


/**
 * @Description: 指南针辅助类：磁场放、方向等传感器工具
 * @Author: Kosmos
 * @Date: 2018.11.30 11:49
 * @Email: KosmoSakura@gmail.com
 * @eg 栗子
 * @eg 2019.5.8：优化传感器校验
 * 1.检查
 * USensor.instance().support()
 * 2.注册监听→onResume()
 * USensor.instance().setDegreeListener((value) -> degree = value);
 * 3.指南针动画
 * ValueAnimator valueAnimator = new ValueAnimator();
 * valueAnimator.addUpdateListener(animation -> {
 * float degree = (float) animation.getAnimatedValue();
 * vCompass.setRotation(degree);
 * });
 * <p>每秒拉取一次数据（之前的做法是回调角度值做差>灵敏度，这个缺点是动画不能走完，指针会抖）
 * rxDisposable(Observable.interval(1000, TimeUnit.MILLISECONDS)
 * .subscribeOn(Schedulers.newThread())
 * .observeOn(AndroidSchedulers.mainThread())
 * .subscribe(along -> {
 * valueAnimator.setFloatValues(olDegree, degree);
 * valueAnimator.setDuration(1000).start();
 * olDegree = degree;
 * }));
 * 4.释放→ onPause()
 * USensor.instance().clear();
 */
public class USensor implements SensorEventListener {
    private static USensor sensor;
    private SensorManager sorMgr;//传感器管理
    private Sensor magnetic; //地磁传感器
    private Sensor accelerometer; //加速度传感器
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private DegreeListener listener;
    private int support = 0;//一切正常=0，无传感器=-1，不支持加速度传感器=1，不支持地磁传感器=2

    private USensor() {
        if (sorMgr == null) {
            //传感器管理
            sorMgr = (SensorManager) KApp.instance().getSystemService(Context.SENSOR_SERVICE);
            if (sorMgr == null) {
                support = -1;//无传感器=-1
                return;
            }
            //加速度传感器
            accelerometer = sorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //地磁传感器
            magnetic = sorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (accelerometer == null) {
                support = 1;//不支持加速度传感器=1
            }
            if (magnetic == null) {
                support = 2;//不支持地磁传感器=2
            }
        }
    }

    public static USensor instance() {
        if (sensor == null) {
            synchronized (USensor.class) {
                if (sensor == null) {
                    sensor = new USensor();
                }
            }
        }
        return sensor;
    }

    /**
     * @return 支持方向地磁传感器
     * 一切正常=0，无传感器=-1，不支持加速度传感器=1，不支持地磁传感器=2
     */
    public int support() {
        if (sorMgr == null) {
            return -1;
        } else return support;
    }

    /**
     * @return 获取设备支持的传感器列表
     */
    public List<Sensor> getSensors() {
        return sorMgr.getSensorList(Sensor.TYPE_ALL);
    }

    public void setDegreeListener(DegreeListener listener) {
        this.listener = listener;
        if (sorMgr == null) {
            return;
        }
        //注册监听
        if (accelerometer != null) {
            sorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        if (magnetic != null) {
            sorMgr.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_GAME);
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
        if (sensor != null) {
            sensor = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }

        if (listener != null) {
            listener.onDegree(calculateOrientation());
        }
    }

    /**
     * @return 计算偏移量
     */
    private float calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        //转化为度数：π=3.14159265358979323846 → Math.toDegrees
        values[0] = values[0] * 180 / 3.1415f;
        return -values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface DegreeListener {
        void onDegree(float value);
    }
}
