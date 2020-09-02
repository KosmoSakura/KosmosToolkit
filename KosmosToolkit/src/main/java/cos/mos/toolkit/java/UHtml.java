package cos.mos.toolkit.java;

import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

/**
 * @Description
 * @Author Kosmos
 * @Date 2019.11.19 17:24
 * @Email KosmoSakura@gmail.com
 * @tip 2020.9.2 追加样式
 */
public class UHtml {
    /**
     * @Tip 给字符添加下划线
     */
    public static void getTextUnderLine(TextView t) {
        t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        t.getPaint().setAntiAlias(true);//抗锯齿
    }

    //下划线
    public static Spanned getTextUnderLine(String str) {
        return getHtml("<u>" + str + "</u>");
    }

    //文本加粗
    public static Spanned getTextBold(String str) {
        return getHtml("<b>" + str + "</b>");
    }

    //文本斜体
    public static Spanned getTextItalic(String str) {
        return getHtml("<i>" + str + "</i>");
    }

    //文本上标
    public static Spanned getTextSup(String str) {
        return getHtml("<sup><small>" + str + "</small></sup>");
    }

    //文本下标
    public static Spanned getTextSub(String str) {
        return getHtml("<sub><small>" + str + "</small></sub>");
    }

    /**
     * @param text      字符
     * @param textColor 字符颜色
     */
    public static Spanned getHtml(String text, String textColor) {
        return getHtml("<font color= '" + textColor + "'>" + text + "</font> ");
    }

    public static Spanned getHtml(String text, int textColor) {
        return getHtml("<font color= '" + textColor + "'>" + text + "</font> ");
    }

    /**
     * @param text1      字符1
     * @param textColor1 字符1颜色
     * @param text2      字符2
     * @param textColor2 字符2颜色
     * @param space      分隔符
     */
    public static Spanned getHtml(String text1, String textColor1, String text2, String textColor2, String space) {
        return getHtml("<font color= '" + textColor1 + "'>" + text1 + "</font> " + space + "<font color= '" + textColor2 + "'>" + "<big>" + text2 + "</big></font><br/>");
    }

    public static Spanned getHtml(String text1, int textColor1, String text2, int textColor2, String space) {
        return getHtml("<font color= '" + textColor1 + "'>" + text1 + "</font> " + space + "<font color= '" + textColor2 + "'>" + "<big>" + text2 + "</big></font><br/>");
    }

    /**
     * @Tip flags:
     * FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
     * FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
     */
    private static Spanned getHtml(String html) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(html);
        } else {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        }
    }

    /**
     * @param str           待处理字符
     * @param color         将要单色设置的颜色
     * @param startLocation 使用该颜色的起始位置
     * @param endLocation   使用该颜色的终止位置
     * @param large         使用该颜色的字体与该字符串其他字体大小的倍数
     * @Tip 动态设置字符串的颜色和大小
     */
    public static SpannableString getSpannable(String str, int color, float large, int startLocation, int endLocation) {
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new ForegroundColorSpan(color), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new RelativeSizeSpan(large), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }
}
