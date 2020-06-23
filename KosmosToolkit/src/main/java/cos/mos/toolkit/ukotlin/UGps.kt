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
import android.support.v4.app.ActivityCompat
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
class UGps(val activity: Activity?) : LocationListener {
    private lateinit var listener: GpsListener

    //位置管理器
    private val mgrLocation: LocationManager? by lazy {
        activity?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }
    private val waitingDialog: ProgressDialog by lazy {
        val waitingDialog = ProgressDialog(activity)
        waitingDialog.setTitle("GPS位置获取中")
        waitingDialog.setMessage("请稍后...")
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // 设置进度条的形式为圆形转动的进度条
        waitingDialog.isIndeterminate = true // 设置ProgressDialog 的进度条是否不明确
        waitingDialog.setCancelable(true) // 设置是否可以通过点击Back键取消
        waitingDialog.setCanceledOnTouchOutside(false) // 设置在点击Dialog外是否取消Dialog进度条
//        waitingDialog.setIcon(R.drawable.ic_navigation_location_sudoku_gps) // 设置提示的title的图标，默认是没有的
        waitingDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消") { _: DialogInterface?, _: Int ->
//            listener.failure(-2, "用户取消定位")
            clear()
        }
        waitingDialog.setOnCancelListener {
//            listener.failure(-2, "用户取消定位")
            clear()
        }
        waitingDialog
    }

    fun getLocation(listener: GpsListener) {
        if (activity == null) {
            KtToast.show("Gps检查异常")
            return
        }
        //检查权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            UDialog.builder(activity)
//                    .title(UHtml.getHtml("提示", Code.ColorRed))
//                    .msg(UHtml.getHtml("使用GPS,需授予本应用定位权限", Code.ColorBlueTheme))
//                    .button()
//                    .build { _, dia ->
//                        goToAppSetting()//跳转到当前应用的设置界面
//                        dia.dismiss()
//                    }
            return
        }
        //gps状态检查
        if (!isGpsOpen()) {
//            UDialog.builder(activity)
//                    .title(UHtml.getHtml("注意", Code.ColorRed))
//                    .msg(UHtml.getHtml("检测到GPS已关闭，请手动打开", Code.ColorBlueTheme))
//                    .button()
//                    .build { _, dia ->
//                        goToGpsSettings()//跳转到Gps开关界面
//                        dia.dismiss()
//                    }
            return
        }
        this.listener = listener
        waitingDialog.show()
        if (mgrLocation == null) {
            listener.failure(-1, "定位捕获异常")
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

    //清除，是否反馈
    fun clear() {
        mgrLocation?.apply {
            removeUpdates(this@UGps)
        }
        waitingDialog.dismiss()
    }

    // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
    override fun onLocationChanged(location: Location?) {
        if (location == null) {
            listener.failure(-1, "定位失败，请移至开阔地带重试！")
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
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
    }

    //跳转到当前应用的设置界面
    fun goToAppSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity?.packageName, null)
        activity?.startActivityForResult(intent, 123)
    }
}