package cos.mos.utils.from_blankj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import cos.mos.utils.constant.Config;


/**
 * @Description 统一日志封装类
 * @Author Kosmos
 * @Date 2020.04.13 19:58
 * @Email KosmoSakura@gmail.com
 */
public class ULog {
    public static void init(@NonNull final Context context) {
        if (Config.IsDebug) {
            ULogBase.init(context)
                .setLogSwitch(true)//日志开关
                .setConsoleSwitch(true)//日志开关
                .setGlobalTag(Config.DefaultTag)
                .setLogHeadSwitch(true)
                .setLog2FileSwitch(false)
                .setDir("")
                .setFilePrefix(Config.DefaultTag)//日志默认Tag
                .setBorderSwitch(true)
                .setSingleTagSwitch(true)
                .setConsoleFilter(ULogBase.V)
                .setFileFilter(ULogBase.V)
                .setStackDeep(1)// log 栈深度，默认为 1
                .setStackOffset(0);
        }
    }

// 直接以默认tag打印log ------------------------------------------------------------------------------------

    //输出普通的打印信息
    public static void commonD(String str) {
        commonD(Config.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonV(String str) {
        commonV(Config.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonE(String str) {
        commonE(Config.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonW(String str) {
        commonW(Config.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonI(String str) {
        commonI(Config.DefaultTag, str);
    }

    //输出堆栈信息
    public static void d(String str) {
        if (Config.IsDebug) {
            d(Config.DefaultTag, str);
        }
    }

    //输出堆栈信息
    public static void v(String str) {
        if (Config.IsDebug) {
            v(Config.DefaultTag, str);
        }
    }

    //输出堆栈信息
    public static void e(String str) {
        if (Config.IsDebug) {
            e(Config.DefaultTag, str);
        }
    }

    //输出堆栈信息
    public static void w(String str) {
        if (Config.IsDebug) {
            w(Config.DefaultTag, str);
        }
    }

    //输出堆栈信息
    public static void i(String str) {
        if (Config.IsDebug) {
            i(Config.DefaultTag, str);
        }
    }

    //打印Json格式的日志
    public static void jsonD(String str) {
        jsonD(Config.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonV(String str) {
        jsonV(Config.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonE(String str) {
        jsonE(Config.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonW(String str) {
        jsonW(Config.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonI(String str) {
        jsonI(Config.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlD(String str) {
        xmlD(Config.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlV(String str) {
        xmlV(Config.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlE(String str) {
        xmlE(Config.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlW(String str) {
        xmlW(Config.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlI(String str) {
        xmlI(Config.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileD(String str) {
        fileD(Config.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileV(String str) {
        fileV(Config.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileE(String str) {
        fileE(Config.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileW(String str) {
        fileW(Config.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileI(String str) {
        fileI(Config.DefaultTag, str);
    }

// 传入tag打印log ------------------------------------------------------------------------------------

    //输出普通的打印信息
    public static void commonD(String tag, String str) {
        if (Config.IsDebug) {
            Log.d(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonV(String tag, String str) {
        if (Config.IsDebug) {
            Log.v(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonE(String tag, String str) {
        if (Config.IsDebug) {
            Log.e(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonW(String tag, String str) {
        if (Config.IsDebug) {
            Log.w(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonI(String tag, String str) {
        if (Config.IsDebug) {
            Log.i(tag, str);
        }
    }

    //输出堆栈信息
    public static void d(String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.dTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void v(String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.vTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void e(String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.eTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void w(String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.wTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void i(String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.iTag(tag, str);
        }
    }

    //打印Json格式的日志
    public static void jsonD(String tag, String str) {
        json(ULogBase.D, tag, str);
    }

    //打印Json格式的日志
    public static void jsonV(String tag, String str) {
        json(ULogBase.V, tag, str);
    }

    //打印Json格式的日志
    public static void jsonE(String tag, String str) {
        json(ULogBase.E, tag, str);
    }

    //打印Json格式的日志
    public static void jsonW(String tag, String str) {
        json(ULogBase.W, tag, str);
    }

    //打印Json格式的日志
    public static void jsonI(String tag, String str) {
        json(ULogBase.I, tag, str);
    }

    //打印xml格式的日志
    public static void xmlD(String tag, String str) {
        xml(ULogBase.D, tag, str);
    }

    //打印xml格式的日志
    public static void xmlV(String tag, String str) {
        xml(ULogBase.V, tag, str);
    }

    //打印xml格式的日志
    public static void xmlE(String tag, String str) {
        xml(ULogBase.E, tag, str);
    }

    //打印xml格式的日志
    public static void xmlW(String tag, String str) {
        xml(ULogBase.W, tag, str);
    }

    //打印xml格式的日志
    public static void xmlI(String tag, String str) {
        xml(ULogBase.I, tag, str);
    }

    //打印文件格式的日志
    public static void fileD(String tag, String str) {
        file(ULogBase.D, tag, str);
    }

    //打印文件格式的日志
    public static void fileV(String tag, String str) {
        file(ULogBase.V, tag, str);
    }

    //打印文件格式的日志
    public static void fileE(String tag, String str) {
        file(ULogBase.E, tag, str);
    }

    //打印文件格式的日志
    public static void fileW(String tag, String str) {
        file(ULogBase.W, tag, str);
    }

    //打印文件格式的日志
    public static void fileI(String tag, String str) {
        file(ULogBase.I, tag, str);
    }

// 不对外提供的方法 ------------------------------------------------------------------------------------

    /**
     * @param type 日志类型
     * @param tag  日志tag
     * @param str  日志内容
     * @Tip 打印Json格式的日志
     */
    private static void json(@ULogBase.TYPE int type, String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.json(type, tag, str);
        }
    }

    /**
     * @param type 日志类型
     * @param tag  日志tag
     * @param str  日志内容
     * @Tip 打印xml格式的日志
     */
    private static void xml(@ULogBase.TYPE int type, String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.xml(type, tag, str);
        }
    }

    /**
     * @param type 日志类型
     * @param tag  日志tag
     * @param str  日志内容
     * @Tip 打印文件日志
     */
    private static void file(@ULogBase.TYPE int type, String tag, String str) {
        if (Config.IsDebug) {
            ULogBase.file(type, tag, str);
        }
    }
}
