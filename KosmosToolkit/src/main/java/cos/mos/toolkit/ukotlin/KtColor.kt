package cos.mos.toolkit.ukotlin

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.IntRange
import android.support.annotation.Size
import cos.mos.toolkit.init.KApp

/**
 * @Description:颜色工具类
 * @Author: Kosmos
 * @Date: 2019.04.24 15:18
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.5.13:获取颜色本地化
 */
@Deprecated("还是不用了，日后只在Uxxx.java中更新")
object KtColor {
    /**
     * @param color eg：-4253158
     * @return eg：-4253158
     * @apiNote 颜色更浅一点(R, G, B分别加40 ）
     */
    @JvmStatic
    fun ColorLighter(@ColorInt color: Int): Int {
        var red = (color and 0xff0000 shr 16) + 40
        var green = (color and 0x00ff00 shr 8) + 40
        var blue = (color and 0x0000ff) + 40
        if (red > 255) {
            red = 255
        }
        if (green > 255) {
            green = 255
        }
        if (blue > 255) {
            blue = 255
        }
        return RgbToInt(red, green, blue)
    }


    /**
     * @param color eg：-4253158
     * @return eg：-4253158
     * @apiNote 颜色更深一点(R, G, B分别减40 ）
     */
    @JvmStatic
    fun ColorDeeper(@ColorInt color: Int): Int {
        var red = (color and 0xff0000 shr 16) - 40
        var green = (color and 0x00ff00 shr 8) - 40
        var blue = (color and 0x0000ff) - 40
        if (red < 0) {
            red = 0
        }
        if (green < 0) {
            green = 0
        }
        if (blue < 0) {
            blue = 0
        }
        return RgbToInt(red, green, blue)
    }

    /**
     * @param color eg：-4253158
     * @return int型颜色eg：-4253158
     * @apiNote 颜色反转
     */
    @JvmStatic
    fun ColorFilter(color: Int): Int = RgbToInt(255 - (color and 0xff0000 shr 16), 255 - (color and 0x00ff00 shr 8), 255 - (color and 0x0000ff))

    /**
     * @return 随机生成一个带alpha通道的颜色
     */
    @JvmStatic
    fun getColorRandom(): Int = getColorRandom(true)

    /**
     * @param supportAlpha 是否带alpha通道
     * @return 随机生成一个颜色
     */
    @JvmStatic
    fun getColorRandom(supportAlpha: Boolean): Int {
        val high = if (supportAlpha) (Math.random() * 0x100).toInt() shl 24 else -0x1000000
        return high or (Math.random() * 0x1000000).toInt()
    }

    /**
     * @param id 资源id
     * @return int型颜色
     */
    @JvmStatic
    fun getColor(@ColorRes id: Int, context: Context? = null): Int {
        return if (context == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KApp.instance().getColor(id)
            } else {
                KApp.instance().resources.getColor(id)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getColor(id)
            } else {
                context.resources.getColor(id)
            }
        }
    }

    /**
     * @param color int型颜色eg：-4253158
     * @param alpha alpha (0-255)
     * @return 带alpha通道的int型颜色
     */
    @JvmStatic
    fun getColorAlpha(@ColorInt color: Int, @IntRange(from = 0x0, to = 0xFF) alpha: Int): Int {
        return color and 0x00ffffff or ((alpha * 255.0f + 0.5f).toInt() shl 24)
    }

    /**
     * @return eg：-4253158
     * @apiNote RGB颜色转化为int
     */
    @JvmStatic
    fun RgbToInt(@IntRange(from = 0, to = 255) red: Int, @IntRange(from = 0, to = 255) green: Int,
                 @IntRange(from = 0, to = 255) blue: Int): Int {
        return -0x1000000 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * @return eg：-4253158
     * @apiNote 将RGB颜色转化为int 带透明度
     */
    @JvmStatic
    fun RgbToInt(@IntRange(from = 0, to = 255) alpha: Int, @IntRange(from = 0, to = 255) red: Int,
                 @IntRange(from = 0, to = 255) green: Int, @IntRange(from = 0, to = 255) blue: Int): Int {
        return alpha shl 24 or (red shl 16) or (green shl 8) or blue
    }

    /**
     * @param colorStr eg：#RRGGBB
     * @return eg：-4253158
     * @apiNote 字符串转int型颜色
     */
    @JvmStatic
    fun Str2Int(@Size(min = 1) colorStr: String): Int = Color.parseColor(colorStr)

    /**
     * @param colorInt eg：-4253158
     * @return eg：#RRGGBB
     * @apiNote int型颜色转RGB字符串颜色
     */
    @JvmStatic
    fun IntToRgbStr(@ColorInt colorInt: Int): String {
        val color = StringBuilder(Integer.toHexString(colorInt and 0x00ffffff))
        while (color.length < 6) {
            color.insert(0, "0")
        }
        color.insert(0, "#")
        return color.toString()
    }


    /**
     * @param colorInt eg：-4253158
     * @return eg：#7EE5E5E5
     * @apiNote int型颜色转ARGB字符串颜色
     */
    @JvmStatic
    fun IntToArgbStr(@ColorInt colorInt: Int): String {
        val color = StringBuilder(Integer.toHexString(colorInt))
        while (color.length < 6) {
            color.insert(0, "0")
        }
        while (color.length < 8) {
            color.insert(0, "f")
        }
        color.insert(0, "#")
        return color.toString()
    }

    /**
     * @param color int型的颜色值（eg：-4253158
     * @apiNote int颜色转化为RGB
     */
    private fun IntToRgb(color: Int) {
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff
    }

    /**
     * @param color int型的颜色值 eg:-4253158
     * @return 16进制颜色字符串 eg:0xFFC2A631
     * @apiNote Color对象转换成16进制颜色字符串
     * 参考：Color.HSVToColor(hsv);
     */
    @JvmStatic
    fun IntToHexs(color: Int): String {
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff
        var r: String
        var g: String
        var b: String
        val su = StringBuilder()
        r = Integer.toHexString(red)
        g = Integer.toHexString(green)
        b = Integer.toHexString(blue)
        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b
        r = r.toUpperCase()
        g = g.toUpperCase()
        b = b.toUpperCase()
        su.append("0xFF")
        su.append(r)
        su.append(g)
        su.append(b)
        return su.toString()
    }
}