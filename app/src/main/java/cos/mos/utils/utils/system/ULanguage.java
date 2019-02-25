package cos.mos.utils.utils.system;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

import cos.mos.utils.ui.MainActivity;
import cos.mos.utils.utils.media.USP;

/**
 * @Description: 语言切换工具
 * @Author: Kosmos
 * @Date: 2019.01.10 13:52
 * @Email: KosmoSakura@gmail.com
 * @eg: 举个栗子
 * 一、切换语言示例
 * ULanguage.instance().setDefaultLanguage(ULanguage.English);
 * recreate();
 * 二、Application内部
 * 1.onCreate
 * ULanguage.instance().doChoose(this);
 * 2.attachBaseContext
 * super.attachBaseContext(ULanguage.instance().getContext(base));
 * 3.onConfigurationChanged
 * super.onConfigurationChanged(newConfig);
 * ULanguage.instance().doChoose(this);
 * 三、BaseActivity的attachBaseContext函数
 * super.attachBaseContext(ULanguage.instance().getContext(newBase));
 * 完成
 */
public class ULanguage {
    private static final String LocaLanguage = "loca_language";
    public static final int Chinese = 1;//简体中文
    public static final int English = 2;//英语
    public static final int German = 3;//德语
    public static final int French = 4;//法语
    public static final int Polish = 5;//波兰语
    public static final int Italian = 6;//意大利语
    public static final int Spanish = 7;//西班牙语
    public static final int Portuguese = 8;//葡萄牙语
    public static final int Ukrainian = 9;//乌克兰语
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
     * @apiNote 选择语言
     */
    public void doChoose(Context context) {
        switch (USP.instance().getInt(LocaLanguage, English)) {
            case Chinese://简体中文:
                locale = new Locale("zh");
                break;
            case English://英语
                locale = new Locale("en");
                break;
            case German://德语
                locale = new Locale("de");
                break;
            case French://法语
                locale = new Locale("fr");
                break;
            case Polish://波兰语
                locale = new Locale("pl");
                break;
            case Italian://意大利语
                locale = new Locale("it");
                break;
            case Spanish://西班牙语
                locale = new Locale("es");
                break;
            case Portuguese://葡萄牙语
                locale = new Locale("pt");
                break;
            case Ukrainian://乌克兰语
                locale = new Locale("uk");
                break;
        }
        changeAppLanguage(locale, context);
    }

    /**
     * @param locale Locale.ENGLISH
     * @apiNote 杀死App，会显示默认语言
     */
    private void changeAppLanguage(Locale locale, Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
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

    public void setDefaultLanguage(int language) {
        USP.instance().putInt(LocaLanguage, language);
    }

    /**
     * @apiNote 重新启动Activity
     * <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />
     */
    public void reBoot(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static String getLanguageText() {
        switch (USP.instance().getInt(LocaLanguage, English)) {
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
                return "English";
        }
    }
}
