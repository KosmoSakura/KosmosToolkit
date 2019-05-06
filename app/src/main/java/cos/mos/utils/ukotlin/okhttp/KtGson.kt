package cos.mos.utils.ukotlin.okhttp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 15:43
 * @Email: KosmoSakura@gmail.com
 */
object KtGson {
    private val gson: Gson = GsonBuilder()
        .serializeNulls()//序列化null
        .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
        .disableInnerClassSerialization()// 禁此序列化内部类
        .disableHtmlEscaping() //禁止转义html标签
        .setPrettyPrinting()//格式化输出
        .create()


    /**
     * @return 实体类转换的字符串
     */
    fun <T> toJson(bean: T): String {
        return gson.toJson(bean)
    }



    inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)
    /**
     * @return 返回一个实体类对象 JsonSyntaxException
     */
    fun <T> toParseObj(json: String, cls: Class<T>): T {
        return gson.fromJson(json, cls)
    }

    /**
     * @return 返回一个列表 JsonSyntaxException
     */
    fun <T> toParseList(json: String, cls: Class<T>): ArrayList<T> {
//        val list = ArrayList<T>()
//        val array = JsonParser().parse(json).asJsonArray
//        for (elem in array) {
//            list.add(gson.fromJson(elem, cls))
//        }
        return gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
    }


    /**
     * @param list<A> 待转泛型列表
     * @param cls  目标类型Class
     * @param <B>  目标泛型
     * @return 返回一个转换类型的列表
     */
    fun <B> toParseNew(list: ArrayList<*>, cls: Class<B>): ArrayList<B> {
        val dtoList = ArrayList<B>()
        for (i in list.indices) {
            val `object` = list[i]
            val json = gson.toJson(`object`)
            val dto = gson.fromJson(json, cls)
            dtoList.add(dto)
        }
        return dtoList
    }
}
