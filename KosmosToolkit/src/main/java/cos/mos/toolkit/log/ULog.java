package cos.mos.toolkit.log;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import cos.mos.toolkit.constant.Code;


/**
 * @Description 统一日志封装类
 * @Author Kosmos
 * @Date 2020.04.13 19:58
 * @Email KosmoSakura@gmail.com
 */
public class ULog {
    public static void init(@NonNull final Context context) {
        if (Code.IsDebug) {
            ULogBase.init(context)
                .setLogSwitch(true)//日志开关
                .setConsoleSwitch(true)//日志开关
                .setGlobalTag(Code.DefaultTag)
                .setLogHeadSwitch(true)
                .setLog2FileSwitch(false)
                .setDir("")
                .setFilePrefix(Code.DefaultTag)//日志默认Tag
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
        commonD(Code.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonV(String str) {
        commonV(Code.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonE(String str) {
        commonE(Code.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonW(String str) {
        commonW(Code.DefaultTag, str);
    }

    //输出普通的打印信息
    public static void commonI(String str) {
        commonI(Code.DefaultTag, str);
    }

    //输出堆栈信息
    public static void d(String str) {
        d(Code.DefaultTag, str);
    }

    //输出堆栈信息
    public static void v(String str) {
        v(Code.DefaultTag, str);
    }

    //输出堆栈信息
    public static void e(String str) {
        e(Code.DefaultTag, str);
    }

    //输出堆栈信息
    public static void w(String str) {
        w(Code.DefaultTag, str);
    }

    //输出堆栈信息
    public static void i(String str) {
        i(Code.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonD(String str) {
        jsonD(Code.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonV(String str) {
        jsonV(Code.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonE(String str) {
        jsonE(Code.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonW(String str) {
        jsonW(Code.DefaultTag, str);
    }

    //打印Json格式的日志
    public static void jsonI(String str) {
        jsonI(Code.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlD(String str) {
        xmlD(Code.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlV(String str) {
        xmlV(Code.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlE(String str) {
        xmlE(Code.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlW(String str) {
        xmlW(Code.DefaultTag, str);
    }

    //打印xml格式的日志
    public static void xmlI(String str) {
        xmlI(Code.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileD(String str) {
        fileD(Code.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileV(String str) {
        fileV(Code.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileE(String str) {
        fileE(Code.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileW(String str) {
        fileW(Code.DefaultTag, str);
    }

    //打印文件格式的日志
    public static void fileI(String str) {
        fileI(Code.DefaultTag, str);
    }

// 传入tag打印log ------------------------------------------------------------------------------------

    //输出普通的打印信息
    public static void commonD(String tag, String str) {
        if (Code.IsDebug) {
            Log.d(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonV(String tag, String str) {
        if (Code.IsDebug) {
            Log.v(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonE(String tag, String str) {
        if (Code.IsDebug) {
            Log.e(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonW(String tag, String str) {
        if (Code.IsDebug) {
            Log.w(tag, str);
        }
    }

    //输出普通的打印信息
    public static void commonI(String tag, String str) {
        if (Code.IsDebug) {
            Log.i(tag, str);
        }
    }

    //输出堆栈信息
    public static void d(String tag, String str) {
        if (Code.IsDebug) {
            ULogBase.dTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void v(String tag, String str) {
        if (Code.IsDebug) {
            ULogBase.vTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void e(String tag, String str) {
        if (Code.IsDebug) {
            ULogBase.eTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void w(String tag, String str) {
        if (Code.IsDebug) {
            ULogBase.wTag(tag, str);
        }
    }

    //输出堆栈信息
    public static void i(String tag, String str) {
        if (Code.IsDebug) {
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
        if (Code.IsDebug) {
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
        if (Code.IsDebug) {
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
        if (Code.IsDebug) {
            ULogBase.file(type, tag, str);
        }
    }
}
