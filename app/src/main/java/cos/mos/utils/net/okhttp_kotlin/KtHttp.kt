package cos.mos.utils.net.okhttp_kotlin

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cos.mos.toolkit.java.UText
import cos.mos.toolkit.json.UGson
import cos.mos.utils.net.okhttp.UNet
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 16:08
 * @Email: KosmoSakura@gmail.com
 * 三星等手机UnknownServiceException网络安全错误解决：
 * AndroidManifest.xml->Application ->添加属性
 * android:networkSecurityConfig="@xml/security_config"
 */
class KtHttp private constructor() {
    private var client: OkHttpClient = OkHttpClient()
    private val gson: Gson
    private val delivery = Handler(Looper.getMainLooper())
    private lateinit var listener: KtHttpListener
    private var describe: String = ""
    private var code: Int = 0
    private var response: Any? = null

    companion object {
        const val NetworkDislink = -1//网络不可用
        const val HttpError = -2//Http请求失败
        private var http: KtHttp? = null
        private var cls: Class<*>? = null//泛型

        fun instance(cls: Class<*>): KtHttp {
            if (http == null) {
                http = KtHttp()
            }
            this.cls = cls
            return http!!
        }
    }

    init {
        //构建实例
        client = client.newBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .build()
        gson = UGson.getGson()
    }

    /**
     * @param url      api
     * @param params   json格式参数
     * @param listener 回调
     * @apiNote 异步Post
     */
    fun post(url: String, params: String, listener: KtHttpListener) {
        this.listener = listener
        if (!KtNet.instance().isNetConnected()) {
            failure("网络不可用", NetworkDislink)
            return
        }
        connection(Request.Builder()
                .url(url)
                .addHeader("key", "value")
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), UText.isNull(params)))
                .build())
    }

    /**
     * @param url      api
     * @param listener 回调
     * @apiNote 异步Get
     */
    fun getAsyn(url: String, listener: KtHttpListener) {
        this.listener = listener
        if (!UNet.instance().isNetConnected) {
            failure("网络不可用", NetworkDislink)
            return
        }
        connection(Request.Builder()
                .url(url)
                .addHeader("key", "value")
                .build())
    }

    private fun connection(request: Request) {
        client.newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        failure("请求失败", HttpError)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.code() == 200) {
                            val body = response.body()
                            if (body != null) {
                                try {
//                                convert("")
//                                convert(body.string())
                                } catch (e: Exception) {
                                    failure("ResponseBody为空", 200)
                                }

                            } else {
                                failure("ResponseBody为空", 200)
                            }
                        } else {
                            failure("Code不等于200", response.code())
                        }
                    }
                })
    }


    //todo 泛型擦除问题并没有解决
    @Throws(JSONException::class)
    private fun <T> convert(body: String) {
        val root = JSONObject(body)
        //外围字段
        val code = root.getInt("Code")
        val msg = root.getString("msg")
        val json = root.getString("data")
        //服务器成功返回码
        if (code == 0) {
            try {
//                val list: ArrayList<T> = gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
                success(gson.fromJson(json, object : TypeToken<List<T>>() {}.type))
//              success(KtGson.toParseList(json, TypeToken<List<T>>() {}.type))
            } catch (e: Exception) {
                try {
//                    val bean: T = gson.fromJson(json, object : TypeToken<T>() {}.type)
                    success(gson.fromJson(json, object : TypeToken<T>() {}.type))
//                  success(KtGson.toParseObj(json, cls))
                } catch (e1: Exception) {
                    failure("解析失败", code)
                }
            }
        } else {
            failure(msg, code)
        }
    }

    private fun failure(describe: String, code: Int) {
        this.describe = describe
        this.code = code
        delivery.post(runFail)
    }

    private fun success(response: Any) {
        this.response = response
        delivery.post(runSuccess)
    }

    private val runSuccess = Runnable { listener.success(response) }
    private val runFail = Runnable { listener.failure(describe, code) }
}