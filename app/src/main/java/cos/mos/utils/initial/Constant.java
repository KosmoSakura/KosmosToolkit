package cos.mos.utils.initial;

import android.os.Environment;

import java.io.File;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.constant.KConfig;


/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.14 17:28
 * @Email: KosmoSakura@gmail.com
 */
public class Constant extends KConfig {
    private static String DownloadPath;//下载路径
    public static String DB_NAME = "utils";

    public static String getDownloadPath() {
        if (UText.isEmpty(DownloadPath)) {
            DownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getBaseUrl() + File.separator;
        }
        return DownloadPath;
    }
}
