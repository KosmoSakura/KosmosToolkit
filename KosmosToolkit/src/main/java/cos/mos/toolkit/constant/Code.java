package cos.mos.toolkit.constant;

import android.graphics.Color;

/**
 * @Description 应用基本常量
 * @Author Kosmos
 * @Date 2020.05.19 17:36
 * @Email KosmoSakura@gmail.com
 */
public interface Code {
    boolean IsDebug = true;//是否为开发环境（正式环境要改为false）
    String DefaultTag = "Kosmos";//默认日志Tag
    String FILE_TYPE_apk = "application/vnd.android.package-archive";//文件类型-apk
    String FILE_TYPE_zip = "application/x-zip-compressed";//文件类型-zip
    String FILE_TYPE_doc = "application/msword";//文件类型-doc
    String FILE_TYPE_png = "image/png";//文件类型-png
    String FILE_TYPE_jpg = "image/jpg";//文件类型-jpg
    String FILE_TYPE_bmp = "image/bmp";//文件类型-bmp
    String FILE_TYPE_gif = "image/gif";//文件类型-gif
    String FILE_TYPE_html = "text/html";//文件类型-html
    String FILE_TYPE_mp3 = "audio/x-mpeg";//文件类型-mp3
    String FILE_TYPE_mp4 = "video/mp4";//文件类型-mp4

    //一些颜色-----------------------------------------------------------------------------------------
    int ColorGray = Color.parseColor("#7d7d7d"); //常规颜色:灰
    int ColorBlueTheme = Color.parseColor("#204566"); //醒目颜色：主题蓝
    int ColorGreen = Color.parseColor("#1f8751"); //通过颜色:绿
    int ColorRedLight = Color.parseColor("#FF4A57"); //警示颜色：浅红
    int ColorRed = Color.parseColor("#FF0000"); //错误颜色：深红
    int ColorBlack = Color.parseColor("#000000"); //普通显示：正黑
    int ColorBlue = Color.parseColor("#1E90FF"); //普通显示：蓝色
    int ColorOrange = Color.parseColor("#F4511E"); //普通显示：橘色
    int ColorPurple= Color.parseColor("#5E35B1"); //普通显示：紫色
}
