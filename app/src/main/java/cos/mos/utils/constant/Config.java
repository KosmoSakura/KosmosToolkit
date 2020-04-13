package cos.mos.utils.constant;

import android.os.Environment;

import java.io.File;

import cos.mos.toolkit.java.UText;


/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.14 17:28
 * @Email: KosmoSakura@gmail.com
 */
public class Config extends KConfig {
    private static String DownloadPath;//下载路径
    public static String DB_NAME = "utils";
    public static final String Version_Name = "1.1.0";
    public static final boolean IsDebug = true;
    public static final String DefaultTag = "Kosmos";//默认日志Tag
    public static final String BaseUrl = "https://test3.hougarden.com";

    public static String getDownloadPath() {
        if (UText.isEmpty(DownloadPath)) {
            DownloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getBaseUrl() + File.separator;
        }
        return DownloadPath;
    }
}
