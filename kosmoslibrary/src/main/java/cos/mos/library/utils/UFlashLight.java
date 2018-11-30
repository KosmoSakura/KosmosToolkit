package cos.mos.library.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

/**
 * @Description: 闪光灯工具
 * @Author: Kosmos
 * @Date: 2018.11.29 13:48
 * @Email: KosmoSakura@gmail.com
 */
public class UFlashLight {
    private static UFlashLight flashLight;
    private CameraManager manager;
    private Camera camera;
    private Context context;

    private UFlashLight(Context context) {
        this.context = context;
    }

    public static UFlashLight instance(Context context) {
        if (flashLight == null) {
            synchronized (UFlashLight.class) {
                if (flashLight == null) {
                    flashLight = new UFlashLight(context);
                }
            }
        }
        return flashLight;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private CameraManager getCMG() {
        if (manager == null) {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
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
                getCMG().setTorchMode("0", true);
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
                if (manager == null) {
                    return;
                }
                manager.setTorchMode("0", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (camera == null) {
                return;
            }
            camera.stopPreview();
            camera.release();
        }
    }
}
