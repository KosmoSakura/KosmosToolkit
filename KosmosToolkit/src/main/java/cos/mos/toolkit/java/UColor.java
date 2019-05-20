package cos.mos.toolkit.java;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Size;

import cos.mos.toolkit.init.KApp;


/**
 * @Description: 颜色工具类
 * @Author: Kosmos
 * @Date: 2017年4月5日
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.3.13:基于Color类算法优化
 * @eg: 2019.5.8:argb颜色处理
 * @eg: 2019.5.13:获取颜色本地化
 */
public class UColor {
    private UColor() {
        throw new UnsupportedOperationException("你不能搞我");
    }

    //赤橙黄绿青蓝紫 七原色
    public static final int[] COLOR_7 = {0xff0000, 0xFF7D00, 0xFFFF00, 0x00FF00, 0x00FFFF,
        0x0000FF, 0xFF00FF, 0xFF0000};

    //渐变色70种
    public static final int[] COLOR_70 = {0xffff0000, 0xffff0c00, 0xffff1900, 0xffff2500,
        0xffff3200, 0xffff3e00, 0xffff4b00, 0xffff5700, 0xffff6400, 0xffff7000, 0xffff7d00,
        0xffff8a00, 0xffff9700, 0xffffa400, 0xffffb100, 0xffffbe00, 0xffffcb00, 0xffffd800,
        0xffffe500, 0xfffff200, 0xffffff00, 0xffe5ff00, 0xffccff00, 0xffb2ff00, 0xff99ff00,
        0xff7fff00, 0xff66ff00, 0xff4cff00, 0xff33ff00, 0xff19ff00, 0xff00ff00, 0xff00ff19,
        0xff00ff33, 0xff00ff4c, 0xff00ff66, 0xff00ff7f, 0xff00ff99, 0xff00ffb2, 0xff00ffcc,
        0xff00ffe5, 0xff00ffff, 0xff00e5ff, 0xff00ccff, 0xff00b2ff, 0xff0099ff, 0xff007fff,
        0xff0066ff, 0xff004cff, 0xff0033ff, 0xff0019ff, 0xff0000ff, 0xff1900ff, 0xff3300ff,
        0xff4c00ff, 0xff6600ff, 0xff7f00ff, 0xff9900ff, 0xffb200ff, 0xffcc00ff, 0xffe500ff,
        0xffff00ff, 0xffff00e5, 0xffff00cc, 0xffff00b2, 0xffff0099, 0xffff007f, 0xffff0066,
        0xffff004c, 0xffff0033, 0xffff0019};
    //渐变色140种
    public static final int[] COLOR_140 = {0xffff0000, 0xffff0600, 0xffff0c00, 0xffff1200,
        0xffff1900, 0xffff1f00, 0xffff2500, 0xffff2b00, 0xffff3200, 0xffff3800, 0xffff3e00,
        0xffff4400, 0xffff4b00, 0xffff5100, 0xffff5700, 0xffff5d00, 0xffff6400, 0xffff6a00,
        0xffff7000, 0xffff7600, 0xffff7d00, 0xffff8300, 0xffff8a00, 0xffff9000, 0xffff9700,
        0xffff9d00, 0xffffa400, 0xffffaa00, 0xffffb100, 0xffffb700, 0xffffbe00, 0xffffc400,
        0xffffcb00, 0xffffd100, 0xffffd800, 0xffffde00, 0xffffe500, 0xffffeb00, 0xfffff200,
        0xfffff800, 0xffffff00, 0xfff2ff00, 0xffe5ff00, 0xffd8ff00, 0xffccff00, 0xffbfff00,
        0xffb2ff00, 0xffa5ff00, 0xff99ff00, 0xff8cff00, 0xff7fff00, 0xff72ff00, 0xff66ff00,
        0xff59ff00, 0xff4cff00, 0xff3fff00, 0xff33ff00, 0xff26ff00, 0xff19ff00, 0xff0cff00,
        0xff00ff00, 0xff00ff0c, 0xff00ff19, 0xff00ff26, 0xff00ff33, 0xff00ff3f, 0xff00ff4c,
        0xff00ff59, 0xff00ff66, 0xff00ff72, 0xff00ff7f, 0xff00ff8c, 0xff00ff99, 0xff00ffa5,
        0xff00ffb2, 0xff00ffbf, 0xff00ffcc, 0xff00ffd8, 0xff00ffe5, 0xff00fff2, 0xff00ffff,
        0xff00f2ff, 0xff00e5ff, 0xff00d8ff, 0xff00ccff, 0xff00bfff, 0xff00b2ff, 0xff00a5ff,
        0xff0099ff, 0xff008cff, 0xff007fff, 0xff0072ff, 0xff0066ff, 0xff0059ff, 0xff004cff,
        0xff003fff, 0xff0033ff, 0xff0026ff, 0xff0019ff, 0xff000cff, 0xff0000ff, 0xff0c00ff,
        0xff1900ff, 0xff2600ff, 0xff3300ff, 0xff3f00ff, 0xff4c00ff, 0xff5900ff, 0xff6600ff,
        0xff7200ff, 0xff7f00ff, 0xff8c00ff, 0xff9900ff, 0xffa500ff, 0xffb200ff, 0xffbf00ff,
        0xffcc00ff, 0xffd800ff, 0xffe500ff, 0xfff200ff, 0xffff00ff, 0xffff00f2, 0xffff00e5,
        0xffff00d8, 0xffff00cc, 0xffff00bf, 0xffff00b2, 0xffff00a5, 0xffff0099, 0xffff008c,
        0xffff007f, 0xffff0072, 0xffff0066, 0xffff0059, 0xffff004c, 0xffff003f, 0xffff0033,
        0xffff0026, 0xffff0019, 0xffff000c};


    /**
     * @param color eg：-4253158
     * @return eg：-4253158
     * @apiNote 颜色更浅一点(R, G, B分别加40 ）
     */
    public static int lighter(@ColorInt int color) {
        int red = ((color & 0xff0000) >> 16) + 40;
        int green = ((color & 0x00ff00) >> 8) + 40;
        int blue = (color & 0x0000ff) + 40;
        if (red > 255) {
            red = 255;
        }
        if (green > 255) {
            green = 255;
        }
        if (blue > 255) {
            blue = 255;
        }
        return rgb(red, green, blue);
    }

    /**
     * @param color eg：-4253158
     * @return eg：-4253158
     * @apiNote 颜色更深一点(R, G, B分别减40 ）
     */
    public static int deeper(@ColorInt int color) {
        int red = ((color & 0xff0000) >> 16) - 40;
        int green = ((color & 0x00ff00) >> 8) - 40;
        int blue = (color & 0x0000ff) - 40;
        if (red < 0) {
            red = 0;
        }
        if (green < 0) {
            green = 0;
        }
        if (blue < 0) {
            blue = 0;
        }
        return rgb(red, green, blue);
    }

    /**
     * @param color eg：-4253158
     * @return int型颜色eg：-4253158
     * @apiNote 颜色反转
     */
    public static int filter(int color) {
        return rgb(255 - ((color & 0xff0000) >> 16), 255 - ((color & 0x00ff00) >> 8), 255 - (color & 0x0000ff));
    }

    /**
     * @return 随机生成一个带alpha通道的颜色
     */
    public static int random() {
        return random(true);
    }

    /**
     * @param supportAlpha 是否带alpha通道
     * @return 随机生成一个颜色
     */
    public static int random(final boolean supportAlpha) {
        int high = supportAlpha ? (int) (Math.random() * 0x100) << 24 : 0xFF000000;
        return high | (int) (Math.random() * 0x1000000);
    }

    /**
     * @param id 资源id
     * @return int型颜色
     */
    public static int getColor(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return KApp.instance().getColor(id);
        } else {
            return KApp.instance().getResources().getColor(id);
        }
    }

    public static int getColor(@ColorRes int id, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * @param color int型颜色eg：-4253158
     * @param alpha alpha (0-255)
     * @return 给int型颜色添加alpha通道
     */
    public static int addAlpha(@ColorInt final int color,
                               @IntRange(from = 0x0, to = 0xFF) int alpha) {
        return (color & 0x00ffffff) | ((int) (alpha * 255.0f + 0.5f) << 24);
    }

    /**
     * @param color int型颜色eg：-4253158
     * @param alpha alpha(0-1)
     * @return 给int型颜色添加alpha通道
     */
    public static int addAlpha(@ColorInt final int color,
                               @FloatRange(from = 0, to = 1) float alpha) {
        return (color & 0x00ffffff) | ((int) (alpha * 255.0f + 0.5f) << 24);
    }

    /**
     * @return eg：-4253158
     * @apiNote RGB颜色转化为int
     * ==Color.rgb(red, greed, blue);
     */
    public static int rgb(@IntRange(from = 0, to = 255) int red,
                          @IntRange(from = 0, to = 255) int green,
                          @IntRange(from = 0, to = 255) int blue) {
        return 0xff000000 | (red << 16) | (green << 8) | blue;
    }

    /**
     * @return eg：-4253158
     * @apiNote 将RGB颜色转化为int 带透明度
     */
    public static int rgb(@IntRange(from = 0, to = 255) int alpha,
                          @IntRange(from = 0, to = 255) int red,
                          @IntRange(from = 0, to = 255) int green,
                          @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @ColorInt
    public static int argb(@IntRange(from = 0, to = 255) int alpha,
                           @IntRange(from = 0, to = 255) int red,
                           @IntRange(from = 0, to = 255) int green,
                           @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * @param colorStr eg：#RRGGBB
     * @return eg：-4253158
     * @apiNote 字符串转int型颜色
     */
    public static int Str2Int(@Size(min = 1) String colorStr) {
        return Color.parseColor(colorStr);
    }

    /**
     * @param colorInt eg：-4253158
     * @return eg：#RRGGBB
     * @apiNote int型颜色转RGB字符串颜色
     */
    public static String IntToRgbStr(@ColorInt int colorInt) {
        colorInt = colorInt & 0x00ffffff;
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        color.insert(0, "#");
        return color.toString();
    }

    /**
     * @param colorInt eg：-4253158
     * @return eg：#7EE5E5E5
     * @apiNote int型颜色转ARGB字符串颜色
     */
    public static String IntToArgbStr(@ColorInt final int colorInt) {
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        while (color.length() < 8) {
            color.insert(0, "f");
        }
        color.insert(0, "#");
        return color.toString();
    }

    /**
     * @param color int型的颜色值（eg：-4253158
     * @apiNote int颜色转化为RGB
     */
    private static void IntToRgb(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
    }

    /**
     * @param color int型的颜色值 eg:-4253158
     * @return 16进制颜色字符串 eg:0xFFC2A631
     * @apiNote Color对象转换成16进制颜色字符串
     * 参考：Color.HSVToColor(hsv);
     */
    public static String IntToHexs(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(red);
        g = Integer.toHexString(green);
        b = Integer.toHexString(blue);
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0xFF");
        su.append(r);
        su.append(g);
        su.append(b);
        return su.toString();
    }
}
