package cos.mos.utils.net.okhttp_kotlin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cos.mos.toolkit.json.UGson
import java.util.*

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 15:43
 * @Email: KosmoSakura@gmail.com
 */
object KtGson {
    private val gson: Gson = UGson.getGson()


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
