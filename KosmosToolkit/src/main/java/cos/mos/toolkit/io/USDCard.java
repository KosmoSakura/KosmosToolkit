package cos.mos.toolkit.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cos.mos.toolkit.java.UText;

/**
 * @Description: SD卡工具
 * @Author: Kosmos
 * @Date: 2018.10.30 20:38
 * @Email: KosmoSakura@gmail.com
 */
public class USDCard {
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
     * @return SD卡总大小
     */
    public static long getSDSize() {
        return getBolockSize(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * @return SD卡剩余大小
     */
    public static long getSDAvailable() {
        return getBolockAvailable(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    /**
     * @param dir 路径
     * @return 返回路径总大小
     */
    public static long getBolockSize(String dir) {
        StatFs fs = new StatFs(dir);
        long totalBolocks;//总的block数量
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBolocks = fs.getBlockCount();
        } else {
            totalBolocks = fs.getBlockCountLong();
        }
        long blockSize = fs.getBlockSize(); //单个block的大小
        return totalBolocks * blockSize;//总空间
    }

    /**
     * @param dir 路径
     * @return 返回路径剩余空间大小
     */
    public static long getBolockAvailable(String dir) {
        StatFs fs = new StatFs(dir);
        long availableBolocks; //可用的blocks的数量
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBolocks = fs.getAvailableBlocks();
        } else {
            availableBolocks = fs.getAvailableBlocksLong();
        }
        long blockSize = fs.getBlockSize(); //单个block的大小
        return availableBolocks * blockSize;//剩余空间
    }
}
