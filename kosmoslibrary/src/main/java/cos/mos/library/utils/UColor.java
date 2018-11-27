package cos.mos.library.utils;


import android.graphics.Color;

/**
 * Description:颜色工具类
 * Author: Kosmos
 * Time: 2017/4/5 000513:17
 * Email:ZeroProject@gmail.com
 */
public class UColor {
    /**
     * 颜色反转
     */
    public static int ColorFilter(int color) {
        int r_ = 255 - ((color & 0xff0000) >> 16);
        int g_ = 255 - ((color & 0x00ff00) >> 8);
        int b_ = 255 - (color & 0x0000ff);
        return RgbToInt(r_, g_, b_);
    }

    /**
     * 将RGB颜色转化为int
     */
    public static int RgbToInt(int red, int green, int blue) {
        int color = Color.rgb(red, green, blue);
        return color;
    }

    /**
     * 将RGB颜色转化为int 带透明度
     */
    public static int RgbToInt(int alphe, int red, int green, int blue) {
        int color = Color.argb(alphe, red, green, blue);
        return color;
    }

    /**
     * int颜色转化为RGB
     */
    private static void IntToRgb(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
    }

    /**
     * Color对象转换成字符串
     *
     * @param color Color对象
     * @return 16进制颜色字符串
     */
    public static String HexFromColor(int color) {
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
        //0xFF0000FF
        return su.toString();
    }
}
