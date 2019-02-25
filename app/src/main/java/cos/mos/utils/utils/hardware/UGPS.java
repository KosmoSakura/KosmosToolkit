package cos.mos.utils.utils.hardware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import cos.mos.utils.utils.ui.UDialog;

/**
 * @Description: GPS工具类
 * @Author: Kosmos
 * @Date: 2018.11.27 18:32
 * @Email: KosmoSakura@gmail.com
 */
public class UGPS {

    /**
     * @return GPS是否打开
     */
    public static boolean isGPSOpne(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 跳转GPS设置界面
     */
    public static void openGPSSettings(Activity activity) {
        UDialog.getInstance(activity, false, false)
            .showNoticeWithOnebtn("Please open the location service",
                "To Open", (result, dia) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(intent, 5);
                    dia.dismiss();
                });
    }
}
