package cos.mos.utils.ukotlin.okhttp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import cos.mos.toolkit.java.UText
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
 */
class UkHttp private constructor() {
    private var client: OkHttpClient = OkHttpClient()
    private val gson: Gson

    companion object {
        const val NetworkDislink = -1//网络不可用
        const val HttpError = -2//Http请求失败
        private var http: UkHttp? = null
        private var cls: Class<*>? = null//泛型

        fun instance(cls: Class<*>): UkHttp {
            if (http == null) {
                http = UkHttp()
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
        gson = GsonBuilder()
            .serializeNulls()//序列化null
            .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
            .disableInnerClassSerialization()// 禁此序列化内部类
            .disableHtmlEscaping() //禁止转义html标签
            .setPrettyPrinting()//格式化输出
            .create()
    }

    /**
     * @param url      api
     * @param params   json格式参数
     * @param listener 回调
     * @apiNote 异步Post
     */
    fun post(url: String, params: String, listener: KtHttpListener<*>) {
        if (!KtNet.instance().isNetConnected()) {
            listener.failure("网络不可用", NetworkDislink)
            return
        }
        connection(listener, Request.Builder()
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
    fun getAsyn(url: String, listener: KtHttpListener<*>) {
        if (!UNet.instance().isNetConnected) {
            listener.failure("网络不可用", NetworkDislink)
            return
        }
        connection(listener, Request.Builder()
            .url(url)
            .addHeader("key", "value")
            .build())
    }

    private fun connection(listener: KtHttpListener<*>, request: Request) {
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener.failure("请求失败", HttpError)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code() == 200) {
                        val body = response.body()
                        if (body != null) {
                            try {
                                convert(listener, body.string())
                            } catch (e: Exception) {
                                listener.failure("ResponseBody为空", 200)
                            }

                        } else {
                            listener.failure("ResponseBody为空", 200)
                        }
                    } else {
                        listener.failure("Code不等于200", response.code())
                    }
                }
            })
    }


    //todo 问题并没有解决
    @Throws(JSONException::class)
    private fun <T> convert(listener: KtHttpListener<T>, body: String) {
        val root = JSONObject(body)
        //外围字段
        val code = root.getInt("Code")
        val msg = root.getString("msg")
        val json = root.getString("data")
        //服务器成功返回码
        if (code == 0) {
            try {
//                val list: ArrayList<T> = gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
                listener.success(gson.fromJson(json, object : TypeToken<List<T>>() {}.type))
//                listener.success(KtGson.toParseList(json, TypeToken<List<T>>() {}.type))
            } catch (e: Exception) {
                try {
//                    val bean: T = gson.fromJson(json, object : TypeToken<T>() {}.type)
                    listener.success(gson.fromJson(json, object : TypeToken<T>() {}.type))
//                    listener.success(KtGson.toParseObj(json, cls))
                } catch (e1: Exception) {
                    listener.failure("解析失败", code)
                }
            }
        } else {
            listener.failure(msg, code)
        }
    }
}