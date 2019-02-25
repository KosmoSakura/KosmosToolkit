package cos.mos.utils.zkosmoslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import cos.mos.utils.zkosmoslibrary.init.KApp;

/**
 * @Description: 闪光灯工具
 * @Author: Kosmos
 * @Date: 2018.11.29 13:48
 * @Email: KosmoSakura@gmail.com
 * @eg 栗子
 * 1.检查
 * UFlashLight.hasFLASH()
 * 2.开灯
 * UFlashLight.instance().openFlash()
 * UFlashLight.screenLight(this, 1);
 * 3.关灯
 * UFlashLight.instance().closeFlash();
 * UFlashLight.screenLight(this, 0.5f);
 */
public class UFlashLight {
    private static UFlashLight flashLight;
    private CameraManager manager;
    private Camera camera;

    private UFlashLight() {

    }

    public static UFlashLight instance() {
        if (flashLight == null) {
            synchronized (UFlashLight.class) {
                if (flashLight == null) {
                    flashLight = new UFlashLight();
                }
            }
        }
        return flashLight;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private CameraManager getCMG() {
        if (manager == null) {
            manager = (CameraManager) KApp.getInstance().getSystemService(Context.CAMERA_SERVICE);
        }
        return manager;
    }

    private Camera getCamera() {
        if (camera == null) {
            camera = Camera.open();
        }
        return camera;
    }

    /**
     * @param alpha 屏幕亮度
     */
    public static void screenLight(Activity activity, float alpha) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        params.screenBrightness = alpha;
        localWindow.setAttributes(params);
    }

    /**
     * @return 是否有闪光灯
     */
    public static boolean hasFLASH() {
        return KApp.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * @param open true,开启闪光灯
     */
    public void light(boolean open) {
        if (open) {
            openFlash();
        } else {
            closeFlash();
        }
    }

    /**
     * 开启闪光灯
     */
    public void openFlash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
//                getCMG().setTorchMode("0", true);
                //获取当前手机所有摄像头设备ID
                String[] ids = getCMG().getCameraIdList();
                CameraCharacteristics c;
                for (String id : ids) {
                    c = getCMG().getCameraCharacteristics(id);
                    //查询该摄像头组件是否包含闪光灯
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (UText.isBoolean(c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) &&
                        lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        //打开或关闭手电筒
                        getCMG().setTorchMode(id, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Camera.Parameters parameters = getCamera().getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            getCamera().setParameters(parameters);
            getCamera().startPreview();
        }
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
//                getCMG().setTorchMode("0", false);
                //关闭当前手机所有摄像头设备ID
                for (String id : getCMG().getCameraIdList()) {
                    getCMG().setTorchMode(id, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getCamera().getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            getCamera().stopPreview();
            getCamera().release();
        }
    }
}
