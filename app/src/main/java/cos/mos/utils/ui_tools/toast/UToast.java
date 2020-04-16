package cos.mos.utils.ui_tools.toast;

import android.widget.Toast;

import cos.mos.toolkit.init.KApp;


/**
 * @Description
 * @Author Kosmos
 * @Date 2019.03.21 11:34
 * @Email KosmoSakura@gmail.com
 * */
public class UToast {
    public static void show(String str) {
        Toast.makeText(KApp.instance(), str, Toast.LENGTH_SHORT).show();
    }
}
