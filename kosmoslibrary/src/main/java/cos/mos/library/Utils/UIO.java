package cos.mos.library.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.10.30 20:38
 * @Email: KosmoSakura@gmail.com
 */
public class UIO {
    private static String SD_PATH;

    private static String getSdPath() {
        if (UText.isEmpty(SD_PATH)) {
            SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "QR_Code";
        }
        File fileDr = new File(SD_PATH);
        if (!fileDr.exists()) {
            fileDr.mkdir();
        }
        return SD_PATH;
    }

    /**
     * @param bmp 保存bmp到sd卡
     */
    public static void saveBitmap(Bitmap bmp) {
        File fileDr = new File(getSdPath(), System.currentTimeMillis() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(fileDr);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
