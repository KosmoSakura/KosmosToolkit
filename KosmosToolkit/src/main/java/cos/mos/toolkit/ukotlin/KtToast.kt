package cos.mos.toolkit.ukotlin

import android.widget.Toast
import cos.mos.toolkit.init.KApp

/**
 * @Description:
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 13:21
 * @Email: KosmoSakura@gmail.com
 */
@Deprecated("还是不用了，日后只在Uxxx.java中更新")
object KtToast {
    @JvmStatic
    fun show(str: String) {
        Toast.makeText(KApp.instance(), str, Toast.LENGTH_SHORT).show()
    }
}
