package cos.mos.utils.initial

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @Description:
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 16:57
 * @Email: KosmoSakura@gmail.com
 */
abstract class KActivity : AppCompatActivity() {
    protected lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(layout())
        init()
        logic()
    }

    protected abstract fun layout(): Int
    protected abstract fun init()
    protected abstract fun logic()
}