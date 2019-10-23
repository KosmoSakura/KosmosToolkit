package cos.mos.toolkit.java;

import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

/**
 * @Description 字符处理类
 * @Author Kosmos
 * @Date 2019.04.30 19:36
 * @Email KosmoSakura@gmail.com
 */
public class UString {
    /**
     * @param str "1967+2356-433*12/66"
     * @return [1967], [2356], [433], [12], [66]
     */
    public static String[] split(String str) {
        return str.split("[+\\-*/]");
    }

    /**
     * @return 返回：首字母大写字符串
     */
    public static String upperFirstLetter(String str) {
        if (UText.isEmpty(str) || !Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) - 32)) + str.substring(1);
        }
    }

    /**
     * @return 返回：首字母小写字符串
     */
    public static String lowerFirstLetter(String str) {
        if (UText.isEmpty(str) || !Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) + 32)) + str.substring(1);
        }
    }

    /**
     * @return 给字符添加下划线
     */
    public static void setTextUnderLine(TextView t) {
        t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        t.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * @return 下划线
     */
    public static Spanned getTextUnderLine(String str) {
        return Html.fromHtml("<u>" + str + "</u>");
    }

    /**
     * @return 文本加粗
     */
    public static Spanned getTextBold(String str) {
        return Html.fromHtml("<b>" + str + "</b>");
    }

    /**
     * @return 文本斜体
     */
    public static Spanned getTextItalic(String str) {
        return Html.fromHtml("<i>" + str + "</i>");
    }

    private void setHtml(TextView textView, String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }

    /**
     * @param text1      字符1
     * @param textColor1 字符1颜色
     * @param text2      字符2
     * @param textColor2 字符2颜色
     * @param space      分隔符
     */
    private Spanned insertHtml(String text1, String textColor1, String text2, String textColor2, String space) {
        return Html.fromHtml("<font color= '" + textColor1 + "'>" + text1 + "</font> " + space +
            "<font color= '" + textColor2 + "'>" + "<big>" + text2 + "</big></font><br/>");
    }

    /**
     * @param t             TextView
     * @param color         将要单色设置的颜色
     * @param startLocation 使用该颜色的起始位置
     * @param endLocation   使用该颜色的终止位置
     * @param large         使用该颜色的字体与该字符串其他字体大小的倍数
     * @apiNote 动态设置字符串的颜色和大小
     */
    public static void getSpannableString(TextView t, int color, int startLocation, int endLocation, float large) {
        String str = t.getText().toString();
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new ForegroundColorSpan(color), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new RelativeSizeSpan(large), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setText(styledText);
    }
}
