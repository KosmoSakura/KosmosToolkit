package cos.mos.toolkit.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * @Description Gson解析
 * @Author Kosmos
 * @Date 2018年10月08日 16:30
 * @Email KosmoSakura@gmail.com
 * @Tip 2019.11.7-添加gson解析容错格式
 */
public class UGson {
    private static Gson gson;

    static {
        gson = new GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .serializeNulls()//序列化null
            .setPrettyPrinting()//格式化输出
            .disableHtmlEscaping() //禁止转义html标签
            .setDateFormat("yyyy-MM-dd HH:mm:ss")// 设置日期时间格式，另有2个重载方法 ,在序列化和反序化时均生效
            .disableInnerClassSerialization()// 禁此序列化内部类
            .registerTypeAdapter(Double.class, new DoubleConverter())
            .registerTypeAdapter(String.class, new StringConverter())
//            .generateNonExecutableJson() //生成不可执行的Json（多了 )]}' 这4个字符）
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
    public static <T> T toParseObj(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    /**
     * @return 返回一个列表 JsonSyntaxException
     */
    public static <T> ArrayList<T> toParseList(String json, Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * @param list<A> 待转泛型列表
     * @param cls     目标类型Class
     * @param <B>     目标泛型
     * @return 返回一个转换类型的列表
     */
    public static <B> ArrayList<B> toParseNew(ArrayList list, Class<B> cls) {
        ArrayList<B> dtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            String json = gson.toJson(object);
            B dto = gson.fromJson(json, cls);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
