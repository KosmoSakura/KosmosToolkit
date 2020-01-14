package cos.mos.utils.initial;

import org.litepal.LitePal;

import cos.mos.utils.from_blankj.ULogBj;
import cos.mos.toolkit.init.KApp;
import cos.mos.toolkit.io.USP;
import cos.mos.toolkit.media.audio.USampleSound;
import cos.mos.toolkit.ui.toast.ToastUtil;
import cos.mos.utils.constant.KConfig;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.04.12 13:09
 * @Email: KosmoSakura@gmail.com
 */
public class App extends KApp {
    @Override
    protected void initialize() {
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
        //数据库初始化
        LitePal.initialize(this);
        //音频池初始化
        USampleSound.instance();
    }
}
