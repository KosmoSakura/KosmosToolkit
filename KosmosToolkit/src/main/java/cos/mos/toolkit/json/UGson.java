package cos.mos.toolkit.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
            .registerTypeAdapter(Float.class, new FloatConverter())
            .registerTypeAdapter(Long.class, new LongConverter())
            .registerTypeAdapter(Integer.class, new IntConverter())
            .registerTypeAdapter(Boolean.class, new BoolConverter())
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
    public static <T> T toBean(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    /**
     * @return 返回一个列表 JsonSyntaxException
     */
    public static <T> ArrayList<T> toList(String json, Class<T> cls) {
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
     * @param <T>     目标泛型
     * @return 返回一个转换类型的列表
     */
    public static <T> ArrayList<T> toList(ArrayList list, Class<T> cls) {
        ArrayList<T> dtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            String json = gson.toJson(object);
            T dto = gson.fromJson(json, cls);
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 没封装出来orz
     */
    public static <T> void toList(String json) {
        List<T> list = gson.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }


    /**
     * @param obj 被解析的实例化对象如 Bean() ArrayList<Bean>()
     * @param <T> 解析任何类型的数据
     * @Tip 栗子
     * Integer xx = jsonToAny("json", 1);
     * ArrayList<Integer> ss = jsonToAny("json", new ArrayList<Integer>());
     */
    public static <T> T toAny(String json, T obj) {
        try {
            return gson.fromJson(json, getRealType(obj));
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    private static Type getRealType(Object obj) {
        Type[] ts = obj.getClass().getGenericInterfaces();
        for (Type type : ts) {
            if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
        }
        return null;
    }
}
