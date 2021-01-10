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
    int OrgTransparent = 0; //原色:透明
    int OrgWhite = 0xFFFFFFFF; //原色:正白
    int OrgBlack = 0xFF000000; //原色：正黑
    int OrgBlue = Color.parseColor("#0000FF");//原色：蓝色
    int OrgRed = Color.parseColor("#FF0000");//原色：红色
    int OrgGreen = Color.parseColor("#00FF00");//原色：绿色
    int OrgBlueGreen = Color.parseColor("#00FFFF");//原色：蓝绿-浅蓝(天依蓝)
    int OrgRedGreen = Color.parseColor("#FFFF00");//原色：红绿-亮黄(柠檬黄)
    int OrgRedBlue = Color.parseColor("#FF00FF");//原色：红蓝-亮紫(嫣红)

    int ColorGray = Color.parseColor("#7d7d7d"); //常规颜色:灰
    int ColorGrayLight = Color.parseColor("#C3C3C3"); //常规颜色:灰
    int ColorGrayHint = Color.parseColor("#9a9a9a"); //常规颜色:灰

    int ColorGreen = Color.parseColor("#1f8751"); //通过颜色:绿
    int ColorGreenLight = Color.parseColor("#00ff00"); //通过颜色:亮绿

    int ColorRed = Color.parseColor("#FF0000"); //错误颜色：深红
    int ColorRedT = Color.parseColor("#1DFF0000"); //错误颜色：深红透明
    int ColorRedLight = Color.parseColor("#FF4A57"); //警示颜色：浅红

    int ColorBlue = Color.parseColor("#1E90FF"); //普通显示：蓝色
    int ColorBlueDark = Color.parseColor("#4B1AC1"); //普通显示：暗蓝
    int ColorBlueTheme = Color.parseColor("#204566"); //醒目颜色：主题蓝

    int ColorYellow = Color.parseColor("#ebea07"); //普通显示：黄
    int ColorYellowLight = Color.parseColor("#fff700"); //普通显示：亮黄
    int ColorYellowDark = Color.parseColor("#a8841d"); //普通显示：暗黄
    int ColorYellowVeryDark = Color.parseColor("#352605"); //普通显示：暗黄

    int ColorOrange = Color.parseColor("#F4511E"); //普通显示：橘色
    int ColorPurple = Color.parseColor("#ff14c0"); //普通显示：紫色
    int ColorBrown = Color.parseColor("#8a512d"); //普通显示：棕色 b
}
