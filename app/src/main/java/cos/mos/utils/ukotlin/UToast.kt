package cos.mos.utils.ukotlin

import android.widget.Toast
import cos.mos.toolkit.init.KApp

/**
 * @Description:
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 13:21
 * @Email: KosmoSakura@gmail.com
 */
object UToast {
    @JvmStatic
    fun show(str: String) {
        Toast.makeText(KApp.instance(), str, Toast.LENGTH_SHORT).show()
    }
}
