package cos.mos.toolkit.constant;

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
}
