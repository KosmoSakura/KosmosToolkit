package cos.mos.toolkit.init;


import android.app.Application;
import android.os.Environment;

import java.io.File;

import cos.mos.toolkit.log.ULog;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 10:42
 * @Email: KosmoSakura@gmail.com
 * @eg MultiDexApplication：解除Dex方法数量超过限制
 */
public abstract class KApp extends Application {
    private static KApp instances;
    private String rootPath;

    public static KApp instance() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        initialize();
    }

    protected abstract void initialize();


    public String getRootPath() {
        return rootPath;
    }

    public void initFiles() {
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            rootPath = Environment.getExternalStorageDirectory() + File.separator + "Sakura";
        }
        File flie = new File(rootPath);
        //目录不存在就自动创建
        if (!flie.exists()) {
            boolean mkdirs = flie.mkdirs();
            ULog.commonD("目录不存在，创建：" + mkdirs + "=" + rootPath);
        }
    }
}
