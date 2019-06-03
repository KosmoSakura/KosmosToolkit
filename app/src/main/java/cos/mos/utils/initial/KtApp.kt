package cos.mos.utils.initial

import android.app.Application

/**
 * @Description:
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.22 21:38
 * @Email: KosmoSakura@gmail.com
 */
class KtApp : Application() {
    //静态块:companion object,里面定义的静态变量跟静态方法
    companion object {
        private lateinit var instances: Application
        @JvmStatic
        fun instance(): Application {
            return instances
        }
    }

    override fun onCreate() {
        super.onCreate()
        instances = this
    }

}