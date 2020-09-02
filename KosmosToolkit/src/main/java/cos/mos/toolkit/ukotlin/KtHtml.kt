package cos.mos.toolkit.ukotlin

import android.graphics.Paint
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView

/**
 * @Description Html处理
 * @Author Kosmos
 * @Date 2019.11.19 17:22
 * @Email KosmoSakura@gmail.com
 * @tip 2020.9.2 追加样式
 * */
object KtHtml {
    /**
     * 给字符添加下划线
     */
    @JvmStatic
    fun setTextUnderLine(tv: TextView) {
        tv.paint.flags = Paint.UNDERLINE_TEXT_FLAG//下划线
        tv.paint.isAntiAlias = true//抗锯齿
    }

    //下划线
    fun getTextUnderLine(str: String) = getHtml("<u>$str</u>")

    //文本加粗
    fun getTextBold(str: String) = getHtml("<b>$str</b>")

    //文本斜体
    fun getTextItalic(str: String) = getHtml("<i>$str</i>")

    //文本上标
    fun getTextSup(str: String) = getHtml("<sup><small>$str</small></sup>")

    //文本下标
    fun getTextSub(str: String) = getHtml("<sub><small>$str</small></sub>")

    /**
     * @param text1      字符1
     * @param textColor1 字符1颜色
     * @param text2      字符2
     * @param textColor2 字符2颜色
     * @param space      分隔符
     */
    fun getHtml(text1: String, textColor1: String, text2: String, textColor2: String, space: String): Spanned =
            getHtml("<font color= '$textColor1'>$text1</font> $space<font color= '$textColor2'><big>$text2</big></font><br/>")

    fun getHtml(str0: String, strBefor: String, strAfter: String, color: String): Spanned =
            getHtml("$strBefor<font color='$color'>$str0</font>$strAfter")

    /**
     * @Tip flags:
     * FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
     * FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
     */
    private fun getHtml(html: String) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Html.fromHtml(html)
    } else {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    }

    /**
     * @param str           待处理字符
     * @param color         将要单色设置的颜色
     * @param startLocation 使用该颜色的起始位置
     * @param endLocation   使用该颜色的终止位置
     * @param large         使用该颜色的字体与该字符串其他字体大小的倍数
     * @Tip 动态设置字符串的颜色和大小
     */
    fun getSpannable(str: String, color: Int, large: Float, startLocation: Int, endLocation: Int) = SpannableString(str).apply {
        setSpan(ForegroundColorSpan(color), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setSpan(RelativeSizeSpan(large), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}