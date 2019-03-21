package cos.mos.utils.init.k;


import android.app.Application;
import android.os.Environment;

import org.litepal.LitePal;

import java.io.File;

import cos.mos.utils.constant.KConfig;
import cos.mos.utils.utils.ULog;
import cos.mos.utils.utils.ULogBj;
import cos.mos.utils.utils.io.USP;
import cos.mos.utils.utils.ui.toast.ToastUtil;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 10:42
 * @Email: KosmoSakura@gmail.com
 * @eg MultiDexApplication：解除Dex方法数量超过限制
 */
public class KApp extends Application {
    private static KApp instances;
    private String rootPath;

    public static KApp instance() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        initBase();
        LitePal.initialize(this);
    }

    private void initBase() {
        //鸿洋牌吐司
        ToastUtil.init(this);
        //SharedPreferences默认表名
        USP.instance().init(this, "Kosmos");
        //Base url
        KConfig.setBaseUrl("---");
        //Blankj封装的Log
        ULogBj.init(this)
            .setLogSwitch(true)//日志开关
            .setConsoleSwitch(true)//日志开关
            .setGlobalTag("Kosmos")
            .setLogHeadSwitch(true)
            .setLog2FileSwitch(false)
            .setDir("")
            .setFilePrefix("Kosmos")//日志默认Tag
            .setBorderSwitch(true)
            .setSingleTagSwitch(true)
            .setConsoleFilter(ULogBj.V)
            .setFileFilter(ULogBj.V)
            .setStackDeep(1)// log 栈深度，默认为 1
            .setStackOffset(0);
    }

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
