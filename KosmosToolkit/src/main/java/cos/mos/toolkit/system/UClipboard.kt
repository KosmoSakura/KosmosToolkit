package cos.mos.toolkit.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * @Description 剪切板
 * @Author Kosmos
 * @Date 2020-8-11-011 21:43
 * @Email KosmoSakura@gmail.com
 * */
object UClipboard {
    private var cm: ClipboardManager? = null
    fun copyToClipboard(context: Context?, copy: String?, notice: String) {
        //获取剪贴板管理器：
        if (cm == null) {
            cm = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        }
        // 创建普通字符型ClipData
        ClipData.newPlainText("Label", copy)?.apply {
            cm?.setPrimaryClip(this)// 将ClipData内容放到系统剪贴板里。
        }
//        UToast.show(context, "${notice}:已复制到剪切板", Toast.LENGTH_SHORT)
    }

}