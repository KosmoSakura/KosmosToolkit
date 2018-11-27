package cos.mos.library.init;


import androidx.multidex.MultiDexApplication;
import cos.mos.library.utils.ULog;
import cos.mos.library.utils.toast.UToast;
import cos.mos.library.constant.KConfig;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 10:42
 * @Email: KosmoSakura@gmail.com
 * @eg MultiDexApplication：解除Dex方法数量超过限制
 */
public abstract class KApp extends MultiDexApplication {
    private static KApp instances;

    public static KApp getInstance() {
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        initBase();
        initApp();
    }

    private void initBase() {
        //鸿洋牌吐司
        UToast.init(this);
        //SharedPreferences默认表名
        KConfig.setSpName(defaultSP());
        //Base url
        KConfig.setBaseUrl(baseUrl());
        //Blankj封装的Log
        ULog.init(this)
            .setLogSwitch(debugState())
            .setConsoleSwitch(debugState())
            .setGlobalTag(logTag())
            .setLogHeadSwitch(true)
            .setLog2FileSwitch(false)
            .setDir("")
            .setFilePrefix(logTag())//文件前缀
            .setBorderSwitch(true)
            .setSingleTagSwitch(true)
            .setConsoleFilter(ULog.V)
            .setFileFilter(ULog.V)
            .setStackDeep(1)// log 栈深度，默认为 1
            .setStackOffset(0);
    }

    /**
     * 初始化参数
     */
    protected abstract void initApp();

    /**
     * @return 日志开关
     */
    protected abstract boolean debugState();

    /**
     * @return 日志默认Tag
     */
    protected abstract String logTag();

    /**
     * @return SharedPreferences 默认名字
     */
    protected abstract String defaultSP();

    /**
     * @return SharedPreferences 默认名字
     */
    protected abstract String baseUrl();


}
