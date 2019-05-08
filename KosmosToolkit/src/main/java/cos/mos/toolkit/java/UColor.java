package cos.mos.toolkit.java;


import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;

import cos.mos.toolkit.init.KApp;


/**
 * @Description: 颜色工具类
 * @Author: Kosmos
 * @Date: 2017年4月5日
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.3.13:基于Color类算法优化
 * @eg: 2019.5.8:argb颜色处理
 */
public class UColor {
    private UColor() {
        throw new UnsupportedOperationException("你不能搞我");
    }

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
        return ContextCompat.getColor(KApp.instance(), id);
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
