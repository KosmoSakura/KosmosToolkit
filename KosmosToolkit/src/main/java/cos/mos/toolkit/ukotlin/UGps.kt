package cos.mos.toolkit.ukotlin

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import cos.mos.toolkit.listener.GpsListener

/**
 * @Description GPS工具类
 * @Author Kosmos
 * @Date 2018.11.27 18:32
 * @Email KosmoSakura@gmail.com
 * 需要权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * @tip 2020.06.23 完善定位
 * */

//class UGps(private val act: Activity?) : LocationListener {
class UGps private constructor(private val act: Activity?) : LocationListener {
    companion object {
        @Volatile
        private var gps: UGps? = null

        @JvmStatic
        fun instance(actNow: Activity?): UGps {
            if (gps == null || UText.isActNull(gps!!.act)) {
                gps = UGps(actNow)
            }
            return gps!!
        }
    }

    private var waitingDialog: ProgressDialog? = null
    private lateinit var listener: GpsListener
    private var showDia: Boolean = true
    private var hasShow: Boolean = false

    //位置管理器
    private val mgrLocation: LocationManager? by lazy {
        act?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    fun getLocation(listener: GpsListener) {
        getLocation(true, listener)
    }

    //获取经纬度-没有对话框等视图
    fun getLocation(showDia: Boolean, listener: GpsListener) {
        if (UText.isActNull(act)) return
        //检查权限
        if (ActivityCompat.checkSelfPermission(act!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            UDialog.builder(act)
                    .title(UHtml.getHtml("提示", Code.ColorRed))
                    .msg(UHtml.getHtml("使用GPS,需授予本应用定位权限", Code.ColorBlueTheme))
                    .button()
                    .build { _, dia ->
                        goToAppSetting()//跳转到当前应用的设置界面
                        dia.dismiss()
                    }
            return
        }
        //gps状态检查
        if (!isGpsOpen()) {
            val ss = UDialog.builder(act)
                    .title(UHtml.getHtml("注意", Code.ColorRed))
                    .msg(UHtml.getHtml("检测到GPS已关闭，请手动打开", Code.ColorBlueTheme))
                    .button()
            ss.build { _, dia ->
                goToGpsSettings()//跳转到Gps开关界面
                dia.dismiss()
            }
            ss.setOnDismissListener { ULog.commonD("关闭") }
            return
        }
        this.showDia = showDia
        this.listener = listener
        if (showDia) {
            showWaitingDialog()
        }
        if (mgrLocation == null) {
            if (showDia) {
                handleFailure(-1, "定位捕获异常")
            }
            clear()
        } else {
            val criteria = Criteria()//定位参数
            criteria.accuracy = Criteria.ACCURACY_COARSE//定位精度:粗略:Criteria.ACCURACY_COARSE，精细:Criteria.ACCURACY_FINE
            criteria.isSpeedRequired = false// 设置是否要求速度
            criteria.isCostAllowed = false//是否允许运营商收费
            criteria.isBearingRequired = false//是否需要方位信息
            criteria.isAltitudeRequired = false//是否需要海拔信息
            criteria.powerRequirement = Criteria.POWER_LOW//对电源的需求
//            val provider = mgrLocation!!.getBestProvider(criteria, true)
//            if (UText.isEmpty(provider)) {
//                //获取最后已知位置
////                val location = mgrLocation!!.getLastKnownLocation(provider!!)
//            } else {
//                clear(-1, "Gps状态异常")
//            }
            //minTime：位置信息更新周期（单位：毫秒）
            //minDistance：位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
            mgrLocation!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0.001f, this)
        }
    }


    private fun showWaitingDialog() {
        if (UText.isActNull(act)) return
        if (waitingDialog == null) {
            waitingDialog = ProgressDialog(act)
            waitingDialog!!.setTitle("GPS位置获取中")
            waitingDialog!!.setMessage("请稍后...")
            waitingDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER) // 设置进度条的形式为圆形转动的进度条
            waitingDialog!!.isIndeterminate = true // 设置ProgressDialog 的进度条是否不明确
            waitingDialog!!.setCancelable(true) // 设置是否可以通过点击Back键取消
            waitingDialog!!.setCanceledOnTouchOutside(false) // 设置在点击Dialog外是否取消Dialog进度条
            waitingDialog!!.setIcon(R.drawable.ic_navigation_location_sudoku_gps) // 设置提示的title的图标，默认是没有的
            waitingDialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, "取消") { _: DialogInterface?, _: Int ->
                handleFailure(-2, "用户取消定位")
                clear()
            }
            waitingDialog!!.setOnCancelListener {
                handleFailure(-2, "用户取消定位")
                clear()
            }
        }
        waitingDialog!!.show()
    }

    /**
     * @param status 状态码 -2:用户取消定位, -1:工具类中错误，其他：定位错误
     * @param msg 描述信息
     * @tip 处理获取失败的情况
     */
    private fun handleFailure(status: Int, msg: String) {
        if (showDia && !hasShow) {
            if (UText.isActNull(act)) return
            if (status != -2) {
                hasShow = true
                val dia = UDialog.builder(act)
                        .title(UHtml.getHtml("错误", Code.ColorRed))
                        .msg(UHtml.getHtml(msg, Code.ColorBlueTheme))
                        .button()
                dia.build()
                dia.setOnDismissListener { hasShow = false }
            }
        }
    }

    //清除，是否反馈
    private fun clear() {
        mgrLocation?.apply {
            removeUpdates(this@UGps)
        }
        if (act == null || act.isFinishing || act.isDestroyed) return
        waitingDialog?.dismiss()
    }

    // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
    override fun onLocationChanged(location: Location?) {
        if (location == null) {
            handleFailure(-1, "定位失败，请移至开阔地带重试！")
        } else {
            listener.success(location)
        }
        clear()
    }

    /**
     * @param provider String
     * @param status Int
     * @param extras provider可选包
     * @tip provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
     */
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        when (status) {
//            LocationProvider.AVAILABLE -> clear(status, "当前GPS状态为可见状态")
//            LocationProvider.OUT_OF_SERVICE -> clear(status, "当前GPS状态为服务区外状态")
//            LocationProvider.TEMPORARILY_UNAVAILABLE -> clear(status, "当前GPS状态为暂停服务状态")
//        }
    }

    //provider被enable时触发此函数，比如GPS被打开
    override fun onProviderEnabled(provider: String?) {}

    //provider被disable时触发此函数，比如GPS被关闭
    override fun onProviderDisabled(provider: String?) {}
//---辅助工具，可单独使用-------------------------------------------------------------------------------------------------------------------------

    //判断Gps是否已经打开：GPS或者AGPS开启一个就认为是开启的
    fun isGpsOpen(): Boolean {
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = mgrLocation?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = mgrLocation?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
        return gps || network
    }

    // 跳转到Gps开关界面
    fun goToGpsSettings() {
        act?.apply {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    //跳转到当前应用的设置界面
    fun goToAppSetting() {
        act?.apply {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }
}