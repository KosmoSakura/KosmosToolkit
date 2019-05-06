package cos.mos.utils.ukotlin.okhttp.sample

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cos.mos.utils.init.KApp
import cos.mos.utils.ukotlin.okhttp.KtHttpListener
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 20:16
 * @Email: KosmoSakura@gmail.com
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
class KtLoad private constructor() {
    private var client = OkHttpClient()
    private val gson: Gson

    companion object {
        const val NetworkDislink = -1//网络不可用
        const val HttpError = -2//Http请求失败
        private var http: KtLoad? = null
            get() {
                if (field == null) {
                    field = KtLoad()
                }
                return field
            }

        fun instance(): KtLoad = http!!
    }

    init {
        client = client.newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build()
        gson = GsonBuilder()
            .serializeNulls()//序列化null
            .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
            .disableInnerClassSerialization()// 禁此序列化内部类
            .disableHtmlEscaping() //禁止转义html标签
            .setPrettyPrinting()//格式化输出
            .create()
    }

    private var netMgr: ConnectivityManager? = null
    private fun mgr(): ConnectivityManager {
        if (netMgr == null) {
            netMgr = KApp.instance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
        return netMgr!!
    }

    private fun isNetConnected(): Boolean {
        val info = mgr().activeNetworkInfo
        return info != null && info.isConnected
    }

    fun  get(from: String, to: String, listener: KtHttpListener<RateBean>) {
        if (!isNetConnected()) {
            listener.failure("网络不可用", NetworkDislink)
            return
        }
        client.newCall(Request.Builder()
            .url("https://api.u9zw.com/exchange-rate/get-exchange-rate?from=$from&to=$to")
            .build())
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code() == 200) {
                        val body = response.body()
                        if (body != null) {
                            try {
                                convert(listener, body.string())
                            } catch (e: Exception) {
                                listener.failure("解析失败", 200)
                            }

                        } else {
                            listener.failure("ResponseBody为空", 200)
                        }
                    } else {
                        listener.failure("Code不等于200", response.code())
                    }
                }

                override fun onFailure(call: Call?, e: IOException) {
                    listener.failure("请求失败", HttpError)
                }
            })
    }

    @Throws(JSONException::class)
    private fun  convert(listener: KtHttpListener<RateBean>, body: String) {
        val root = JSONObject(body)
        val code = root.getInt("code")
        val json = root.getString("data")
        //服务器成功返回码
        if (code == 0) {
            listener.success(Gson().fromJson(json))
//            listener.success(gson.fromJson(json, RateBean::class.java))
        } else {
            listener.failure(root.getString("msg"), code)
        }
    }

    inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)
}