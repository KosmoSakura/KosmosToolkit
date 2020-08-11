package cos.mos.utils.utils.gps

import android.Manifest
import android.annotation.SuppressLint
import android.location.GnssStatus
import android.location.GpsSatellite
import android.location.GpsStatus
import android.location.LocationManager
import android.os.Build
import android.support.annotation.RequiresPermission

/**
 * @Description Gps状态检测
 * @Author Kosmos
 * @Date 2020.08.11 11:35
 * @Email KosmoSakura@gmail.com
 * */

class UGpsState constructor(private val mgrLocation: LocationManager?) {
    var gpsCount = 0//有效卫星
    var gpsCountTotal = 0//搜索到的卫星总数

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun registeState() {
        if (mgrLocation == null) {
            return
        }
        mgrLocation.addGpsStatusListener(gpsLis)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            mgrLocation.registerGnssStatusCallback(gpsStatus)
//        } else {
//            mgrLocation.addGpsStatusListener(gpsLis)
//        }
    }

    @SuppressLint("MissingPermission")
    private val gpsLis = GpsStatus.Listener { event ->
        gpsCount = 0
        gpsCountTotal = 0
        when (event) {
            GpsStatus.GPS_EVENT_FIRST_FIX -> {
                //第一次定位
            }
            GpsStatus.GPS_EVENT_SATELLITE_STATUS -> {
                //卫星状态改变
                val gpsStauts: GpsStatus = mgrLocation!!.getGpsStatus(null) // 取当前状态
                val maxSatellites = gpsStauts.maxSatellites //获取卫星颗数的默认最大值
                val it: Iterator<GpsSatellite> = gpsStauts.satellites.iterator() //创建一个迭代器保存所有卫星
                while (it.hasNext() && gpsCountTotal <= maxSatellites) {
                    val s = it.next()
                    //可见卫星数量
                    if (s.usedInFix()) {
                        gpsCount++ //已定位卫星数量
                    }
                    gpsCountTotal++
                }
            }
            GpsStatus.GPS_EVENT_STARTED -> {
                //定位启动
            }
            GpsStatus.GPS_EVENT_STOPPED -> {
                //定位结束
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    private val gpsStatus: GnssStatus.Callback = object : GnssStatus.Callback() {
//        override fun onFirstFix(ttffMillis: Int) {
//            super.onFirstFix(ttffMillis)
//            //第一次定位
//        }
//
//        override fun onSatelliteStatusChanged(status: GnssStatus?) {
//            super.onSatelliteStatusChanged(status)
//            //卫星状态改变
//        }
//
//        override fun onStarted() {
//            super.onStarted()
//            //定位启动
//        }
//
//        override fun onStopped() {
//            super.onStopped()
//            //定位结束
//        }
//    }
}
