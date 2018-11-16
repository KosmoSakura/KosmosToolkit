package cos.mos.utils.init;

import android.os.Environment;

import java.io.File;

import cos.mos.library.Utils.UText;
import cos.mos.library.constant.KConfig;


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
            DownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getSPName() + File.separator;
        }
        return DownloadPath;
    }
}
