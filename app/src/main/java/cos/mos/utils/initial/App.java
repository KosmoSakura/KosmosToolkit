package cos.mos.utils.initial;

import org.litepal.LitePal;

import cos.mos.toolkit.init.KApp;
import cos.mos.toolkit.io.USP;
import cos.mos.toolkit.media.audio.USampleSound;
import cos.mos.utils.ui_tools.toast.ToastUtil;
import cos.mos.utils.constant.KConfig;
import cos.mos.utils.from_blankj.ULog;

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
        ULog.init(this);
        //数据库初始化
        LitePal.initialize(this);
        //音频池初始化
        USampleSound.instance();
    }
}
