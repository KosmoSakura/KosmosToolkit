package cos.mos.library.Utils;

import android.content.Context;

import java.io.File;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.10.30 20:38
 * @Email: KosmoSakura@gmail.com
 */
public class UIO {
    /**
     * 获取缓存目录
     */
    public static String getExternalCacheDir(Context context) {
        File externalFilesDir = context.getExternalCacheDir();
        if (externalFilesDir != null) {
            return externalFilesDir.getPath();
        }
        return "";
    }


    /**
     * @return 为空文件夹
     */
    public static boolean isEmptyDir(File file) {
        return file.exists() && file.isDirectory() && file.listFiles().length <= 0;
    }

    /**
     * @return 为空文件
     */
    public static boolean isEmptyFile(File file) {
        return file.exists() && !file.isDirectory() && file.length() <= 0;
    }

    /**
     * @param name 文件名
     * @return 后缀名
     */
    public static String getSuffix(String name) {
        return name.substring(name.indexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? System.currentTimeMillis() + ".apk" : path.substring(separatorIndex + 1, path.length());
    }
}
