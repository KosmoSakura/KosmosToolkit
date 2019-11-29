package cos.mos.toolkit.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Description
 * @Author Kosmos
 * @Date 2019.11.29 11:10
 * @Email KosmoSakura@gmail.com
 * @Tip
 * */
object KtGson {
    private var gson: Gson? = null

    init {
        gson = GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .serializeNulls() //序列化null
            .setPrettyPrinting() //格式化输出
            .disableHtmlEscaping() //禁止转义html标签
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
            .disableInnerClassSerialization() // 禁此序列化内部类
            .registerTypeAdapter(Double::class.java, DoubleConverter())
            .registerTypeAdapter(Float::class.java, FloatConverter())
            .registerTypeAdapter(Long::class.java, LongConverter())
            .registerTypeAdapter(Int::class.java, IntConverter())
            .registerTypeAdapter(Boolean::class.java, BoolConverter())
            .registerTypeAdapter(String::class.java, StringConverter())
            //            .generateNonExecutableJson() //生成不可执行的Json（多了 )]}' 这4个字符）
            .create()
    }

    private fun <T> jsonToAny(json: String?, obj: T): T? {
        return try {
            gson?.fromJson(json, getRealType(obj!!))
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    private fun getRealType(obj: Any): Type? {
        val ts: Array<Type> = obj.javaClass.genericInterfaces
        for (type in ts) {
            if (ParameterizedType::class.java.isAssignableFrom(type.javaClass)) {
                return (type as ParameterizedType).actualTypeArguments[0]
            }
        }
        return null
    }
}