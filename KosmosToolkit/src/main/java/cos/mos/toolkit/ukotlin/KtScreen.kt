package cos.mos.toolkit.ukotlin

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import cos.mos.toolkit.init.KApp

/**
 * @Description: 屏幕信息
 * @Author: Kosmos
 * @Date: 2019.04.22 21:30
 * @Email: KosmoSakura@gmail.com
 */
@Deprecated("还是不用了，日后只在Uxxx.java中更新,截至2019.04.22，两个内容还是相同的")
object KtScreen {
    private val metric = KApp.instance().resources.displayMetrics
    private val scale = metric.density

    /**
     * 目标控件的绝对坐标 位置
     * @param view 目标控件
     * */
    @JvmStatic
    fun getAbs(view: View): IntArray {
        val location = IntArray(2)
        view.getLocationInWindow(location) //获取在当前窗口内的绝对坐标，含toolBar
        view.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标，含statusBar
        return location
    }

    /**
     * @param activity Activity引用
     * @param color int型色值
     * @return 设置顶部状态栏、底部导航栏颜色(只能在Activity内部调用)
     * */
    @JvmStatic
    fun setBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val window = activity.window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
                window.navigationBarColor = color
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * @return 获取DisplayMetrics对象
     * */
    @JvmStatic
    fun getDisPlayMetrics(): DisplayMetrics = metric

    /**
     * @param pxValue 像素单位
     * @return dp单位
     * */
    @JvmStatic
    fun px2dp(pxValue: Float): Float = pxValue / scale + 0.5f

    /**
     * @param dpValue dp单位
     * @return  像素单位
     * */
    @JvmStatic
    fun dp2Px(dpValue: Float): Float = dpValue * scale + 0.5f

    /**
     *@return 屏幕宽度（像素）
     * */
    @JvmStatic
    fun getScreenWidth(): Int = metric.widthPixels

    /**
     *@return 屏幕高度（像素）
     * */
    @JvmStatic
    fun getScreenHeight(): Int = metric.heightPixels

    /**
     * @return 屏幕密度(0.75 / 1.0 / 1.5)
     */
    @JvmStatic
    fun getDensity(): Float = metric.density

    /**
     * @return 屏幕密度DPI(120 / 160 / 240)
     */
    @JvmStatic
    fun getDensityDpi(): Int = metric.densityDpi
}