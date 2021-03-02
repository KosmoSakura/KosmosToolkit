package cos.mos.toolkit.init

import android.app.Application

abstract class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    protected abstract fun initialize()
}