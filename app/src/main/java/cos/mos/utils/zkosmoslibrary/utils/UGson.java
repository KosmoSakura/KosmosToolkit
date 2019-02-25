package cos.mos.utils.zkosmoslibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年10月08日 16:30
 * @Email: KosmoSakura@foxmail.com
 */
public class UGson {
    private static Gson gson;

    static {
        gson = new GsonBuilder()
            .serializeNulls()//序列化null
            .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
            .disableInnerClassSerialization()// 禁此序列化内部类
//            .generateNonExecutableJson() //生成不可执行的Json（多了 )]}' 这4个字符）
            .disableHtmlEscaping() //禁止转义html标签
            .setPrettyPrinting()//格式化输出
            .create();
    }


    /**
     * @return 实体类转换的字符串
     */
    public static <T> String toJson(T bean) {
        return gson.toJson(bean);
    }

    /**
     * @return 返回一个实体类对象 JsonSyntaxException
     */
    public static <T> T toParse(String jsonString, Class<T> cls) {
        return gson.fromJson(jsonString, cls);
    }

    /**
     * @return 返回一个列表 JsonSyntaxException
     */
    public static <T> ArrayList<T> toParses(String jsonString, Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();

        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * @param list 待转换列表
     * @param cls  目标类型Class
     * @param <T>  目标泛型
     * @return 返回一个转换类型的列表
     */
    public static <T> ArrayList<T> toParseNew(ArrayList list, Class<T> cls) {
        ArrayList<T> dtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            String json = gson.toJson(object);
            T dto = gson.fromJson(json, cls);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
