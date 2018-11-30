package cos.mos.library.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * @Description: 指南针辅助类：磁场放、方向等传感器工具
 * @Author: Kosmos
 * @Date: 2018.11.30 11:49
 * @Email: KosmoSakura@gmail.com
 */
public class USensor implements SensorEventListener {
    private static USensor sensor;
    private SensorManager sorMgr;//传感器管理
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private DegreeListener listener;
    private float olDegree;
    private static final int sensitivity = 6;//变化灵敏度，数值越小，变化月灵敏

    private USensor(Context context) {
        if (sorMgr == null) {
            //传感器管理
            sorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            //加速度传感器
            Sensor accelerometer = sorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //地磁传感器
            Sensor magnetic = sorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            //注册监听
            sorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            sorMgr.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public static USensor instance(Context context) {
        if (sensor == null) {
            synchronized (USensor.class) {
                if (sensor == null) {
                    sensor = new USensor(context);
                }
            }
        }
        return sensor;
    }

    /**
     * @return 获取设备支持的传感器列表
     */
    public List<Sensor> getSensors() {
        return sorMgr.getSensorList(Sensor.TYPE_ALL);
    }

    public void setDegreeListener(DegreeListener listener) {
        this.listener = listener;
    }

    /**
     * 资源清理，注销监听
     */
    public void clear() {
        sorMgr.unregisterListener(this);
        sorMgr = null;
        sensor = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }

        float degree = calculateOrientation();
        if (Math.abs(degree - olDegree) > sensitivity) {
            if (listener != null) {
                ULog.commonD(" --> " + degree);
                listener.onDegree(olDegree, degree);
            }
        }
        olDegree = degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    /**
     * @return 计算偏移量
     */
    private float calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        return -values[0];
    }

    public interface DegreeListener {
        void onDegree(float olDegree, float degree);
    }

}
