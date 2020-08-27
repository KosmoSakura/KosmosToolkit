package cos.mos.toolkit.io

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cos.mos.toolkit.java.UText

/**
 * @Description IO工具
 * @Author Kosmos
 * @Date 2020.08.27 19:45
 * @Email KosmoSakura@gmail.com
 * */
object UIo {
    //SD卡中加载图片
    fun loadBmp(dirFile: String?): Bitmap? {
        if (UText.isEmpty(dirFile)) {
            return null
        }
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inSampleSize = 2
        return BitmapFactory.decodeFile(dirFile, options)
    }
}