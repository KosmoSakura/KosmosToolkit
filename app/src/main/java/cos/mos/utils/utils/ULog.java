package cos.mos.utils.utils;

import android.util.Log;

/**
 * @Description: 打日志
 * @Author: Kosmos
 * @Date: 2019.02.14 16:20
 * @Email: KosmoSakura@gmail.com
 */
public class ULog {
    public static void commonD(String str) {
        Log.d("Kosmos", str);
    }

    public static void commonV(String str) {
        Log.v("Kosmos", str);
    }

    public static void commonE(String str) {
        Log.e("Kosmos", str);
    }

    public static void commonW(String str) {
        Log.w("Kosmos", str);
    }

    public static void commonI(String str) {
        Log.i("Kosmos", str);
    }
}
