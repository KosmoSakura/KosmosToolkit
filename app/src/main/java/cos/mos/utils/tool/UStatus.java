package cos.mos.utils.tool;

import android.content.Context;
import android.os.PowerManager;

import cos.mos.library.init.KApp;


/**
 * @Description: 状态工具
 * @Author: Kosmos
 * @Date: 2019.02.01 13:34
 * @Email: KosmoSakura@gmail.com
 */
public class UStatus {
    private static PowerManager pm = (PowerManager) KApp.getInstance().getSystemService(Context.POWER_SERVICE);

    /**
     * @return true-亮屏，false-暗屏
     * @apiNote 是否处于亮屏状态
     */
    public static boolean isScreenOn() {
        return pm.isScreenOn();
    }
}
