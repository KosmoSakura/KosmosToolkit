package cos.mos.toolkit.system;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

import cos.mos.toolkit.io.USP;

/**
 * @Description 语言切换
 * @Author Kosmos
 * @Date 2019.01.10 13:52
 * @Email KosmoSakura@gmail.com
 * @tip 举个栗子
 * 一、切换语言示例
 * <p>ULanguage.getLanguageFlag()//获取当前设置的语言(用于一般UI展示)
 * <p>ULanguage.instance().setDefaultLanguage(ULanguage.Chinese);//切换语言
 * <p>ULanguage.instance().reBoot(this)//重启到第一个页面
 * <p>
 * 二、Application内部
 * 1.onCreate()
 * <p> ULanguage.instance().changeAppLanguage(this);//启动时：刷新默认语言
 * 2.attachBaseContext()
 * <p>super.attachBaseContext(ULanguage.instance().getContext(base));//初始化上下文，比onCreate先调用
 * 3.onConfigurationChanged()
 * <p>super.onConfigurationChanged(newConfig);
 * <p>ULanguage.instance().changeAppLanguage(this);//多进程Application启动回调（super之后调用）
 * <p>
 * 三、BaseActivity的attachBaseContext函数
 * 1.onCreate()
 * <p> ULanguage.instance().changeAppLanguage(this)
 * 2.attachBaseContext()
 * <p>super.attachBaseContext(ULanguage.instance().getContext(newBase));
 * 完成
 */
public class ULanguage {
    private static final String LocaLanguage = "loca_language";
    public static final String Chinese = "zh";//简体中文
    public static final String English = "en";//英语
    public static final String German = "de";//德语
    public static final String French = "fr";//法语
    public static final String Polish = "pl";//波兰语
    public static final String Italian = "it";//意大利语
    public static final String Spanish = "es";//西班牙语
    public static final String Portuguese = "pt";//葡萄牙语
    public static final String Ukrainian = "uk";//乌克兰语
    private static ULanguage uLanguage;
    private Locale locale;

    public static ULanguage instance() {
        if (uLanguage == null) {
            synchronized (ULanguage.class) {
                if (uLanguage == null) {
                    uLanguage = new ULanguage();
                }
            }
        }
        uLanguage.locale = Locale.getDefault();
        return uLanguage;
    }

    private ULanguage() {
    }

    /**
     * @apiNote 选择语言:杀死App，会显示默认语言
     */
    private void changeAppLanguage(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        locale = new Locale(getLanguageFlag());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    public Context getContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Configuration config = context.getResources().getConfiguration();
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(config);
        } else {
            return context;
        }
    }

    public void setDefaultLanguage(String language) {
        USP.instance().putString(LocaLanguage, language);
        locale = new Locale(language);
    }

    /**
     * @param aClass eg: MainActivity.class(传入app的第一个页面)
     * @apiNote 重新启动Activity
     * <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
     */
    public void reBoot(Context context, Class aClass) {
        Intent intent = new Intent(context, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }

    public static String getLanguageFlag() {
        return USP.instance().getString(LocaLanguage, Chinese);
    }

    public static String getLanguageText() {
        switch (USP.instance().getString(LocaLanguage, Chinese)) {
            case Chinese://简体中文
                return "简体中文";
            case English://英语
                return "English";
            case German://德语
                return "Deutsch";
            case French://法语
                return "Le français";
            case Polish://波兰语
                return "Polski";
            case Italian://意大利语
                return "lingua italiana";
            case Spanish://西班牙语
                return "Español";
            case Portuguese://葡萄牙语
                return "Português";
            case Ukrainian://乌克兰语
                return "Ukrainian";
            default:
                return "简体中文";
        }
    }
}
