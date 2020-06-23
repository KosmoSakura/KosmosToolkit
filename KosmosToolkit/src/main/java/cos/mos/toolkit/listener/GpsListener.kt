package cos.mos.toolkit.listener

import android.location.Location

/**
 * @Description Gps定位监听
 * @Author Kosmos
 * @Date 2020.06.23 18:07
 * @Email KosmoSakura@gmail.com
 * */
interface GpsListener {
    /**
     * @param location 位置信息
     */
    fun success(location: Location)

    /**
     * @param status 状态码
     * @param msg    描述信息:-2:用户取消定位, -1:工具类中错误，其他：定位错误
     */
    fun failure(status: Int, msg: String)
}