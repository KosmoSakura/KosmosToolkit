package cos.mos.toolkit.ukotlin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import cos.mos.toolkit.json.UGson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Description 可用于简单的json解析
 * @Author Kosmos
 * @Date 2020.07.28 18:14
 * @Email KosmoSakura@gmail.com
 * @Tip 2019.11.7-添加gson解析容错格式
 * @Tip 2020.7.28 优化gson列表解析
 * */
object UGson {
    private val gson: Gson by lazy {
        GsonBuilder().setLenient()//宽容的解析，默认情况下，Gson是严格的，只接受指定的JSON
                .serializeNulls()//序列化null
//                .enableComplexMapKeySerialization()//启用复杂映射键序列化
//                .setPrettyPrinting()//将Gson配置为输出Json，以便在页面中进行漂亮的打印。这个选项有影响Json序列化。
//                .disableHtmlEscaping() //禁止转义html标签:默认情况下，Gson会转义HTML字符，如&lt等。使用此选项进行配置按原样传递HTML字符
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
//                .disableInnerClassSerialization()// 禁此序列化内部类
//                .registerTypeAdapter(Boolean::class.java, BoolConverter())
//                .registerTypeAdapter(String::class.java, StringConverter())
//                .excludeFieldsWithoutExposeAnnotation()// 不转换没有@Expose注释的字段
//                .generateNonExecutableJson() //生成不可执行的Json（多了 )]}' 这4个字符）：通过在生成的JSON前面加上一些前缀，使输出JSON在Javascript中不可执行
                .create()
    }

    /**
     * @return 实体类\列表 转换的字符串
     */
    fun <T> toJson(bean: T): String? = gson.toJson(bean)
    fun <T> toJson(bean: T, type: Type): String? = gson.toJson(bean, type)

    /**
     * @return 返回一个实体类对象 JsonSyntaxException
     */
    fun <T> fromJson(json: String?, cls: Class<T>?) = gson.fromJson(json, cls)
    fun <T> fromJsonList(json: String?): List<T>? = gson.fromJson(json, object : TypeToken<List<T>>() {}.type)

//---------------------------------------------------------------------------------------------
//下面的代码仅测试用
//---------------------------------------------------------------------------------------------

    /**
     * @param obj 被解析的实例化对象如 Bean() ArrayList<Bean>()
     * @param <T> 解析任何类型的数据
     * @Tip 栗子
     * Integer xx = jsonToAny("json", 1);
     * ArrayList<Integer> ss = jsonToAny("json", new ArrayList<Integer>());
     * */
    private fun <T> toAny(json: String?, obj: T): T? {
        return try {
            gson.fromJson(json, getRealType(obj!!))
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