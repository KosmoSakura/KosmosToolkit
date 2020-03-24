package cos.mos.toolkit.ukotlin

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View

/**
 * @Description Kotlin拓展函数库
 * @Author Kosmos
 * @Date 2020.03.24 17:06
 * @Email KosmoSakura@gmail.com
 * */
object UParameter {
    //最后一次点击时间
    var <T : View> T.lastClickTime: Long
        set(value) = setTag(1766613352, value)//获取保存时间
        get() = getTag(1766613352) as? Long ?: 0//获取点击时间

    /**
     * @Description 防止重复点击
     * @Author Kosmos
     * @Date 2020/3/24 5:15 PM
     * @Email KosmoSakura@gmail.com
     * */
    inline fun <T : View> T.makeSingleClick(time: Long = 800, crossinline block: (T) -> Unit) {
        setOnClickListener {
            val currentClickTime = System.currentTimeMillis()
            if (currentClickTime - lastClickTime > time) {
                lastClickTime = currentClickTime
                block(this)
            }
        }
    }

    //保留2位小数
    fun Float.fotmatMax() = (this * 100f).toInt() / 100f

    fun String.makeUnderLine(): Spanned = getHtml("<u>$this</u>")
    fun String.makeBold(): Spanned = getHtml("<b>$this</b>")
    fun String.makeItalic(): Spanned = getHtml("<i>$this</i>")
    fun String.makeColor(color: String) = getHtml("<font color='$color'>$this</font>")

    private fun getHtml(html: String) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Html.fromHtml(html)
    } else {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    }
}