package cos.mos.toolkit.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description 反射辅助工具
 * @Author Kosmos
 * @Date 2018.11.25 16:31
 * @Email KosmoSakura@gmail.com
 * @Tip 最新修改日期：2018年11月25日 17:19
 * @Tip 返回目标的泛型 的真实类型:2019.10.24
 */
public class UReflex {
    /**
     * @param obj 目标对象
     * @return 返回目标的泛型 的真实类型
     */
    public static Type getRealType(Object obj) {
        Type[] ts = obj.getClass().getGenericInterfaces();
        for (Type type : ts) {
            //如果 参数化类型 可以转让
            if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
                //获取实际的类型参数 的第一个类型
                return ((ParameterizedType) type).getActualTypeArguments()[0];
            }
        }
        return null;
    }

    /**
     * @return 返回目标的泛型 的真实类型
     * 这种方法 还没有想到封装的办法
     */
    @Deprecated
    public <T> Class getRealTypeDeprecated(Object obj) {
        // 获取当前new的对象的泛型的父类类型
        ParameterizedType pt = (ParameterizedType) obj.getClass().getGenericSuperclass();
        // 获取第一个类型参数的真实类型
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    /**
     * @param obj  目标类的实例对象：WifiManager obj;
     * @param name 属性名字，eg：WIFI_AP_STATE_ENABLED
     * @return 公共属性的值(比如是String类型 ）
     */
    public static String getPrivateFirldStr(Object obj, String name) {
        Class<?> cc = obj.getClass();
        try {
            Method method = cc.getDeclaredMethod(name);//获取私有方法
            return (String) method.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param obj  目标类的实例对象：WifiManager obj;
     * @param name 属性名字，eg：WIFI_AP_STATE_ENABLED
     * @return 公共方法的值(比如是String类型 ）
     */
    public static String getPicMethod(Object obj, String name) {
        Class<?> cc = obj.getClass();
        try {
            Method method = cc.getMethod(name);//获取公共方法
            return (String) method.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param obj  目标类的实例对象：WifiManager obj;
     * @param name 属性名字，eg：WIFI_AP_STATE_ENABLED
     * @return 私有属性的值(比如是String类型 ）
     */
    public static String getPrtFirld(Object obj, String name) {
        Class<?> cc = obj.getClass();
        try {
            Field field = cc.getDeclaredField(name);//获取私有属性
            return (String) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param obj  目标类的实例对象：WifiManager obj;
     * @param name 属性名字，eg：WIFI_AP_STATE_ENABLED
     * @return 公共属性的值(比如是String类型 ）
     */
    public static String getPicFirld(Object obj, String name) {
        Class<?> cc = obj.getClass();
        try {
            Field field = cc.getField(name);//获取公共属性
            return (String) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param path eg:java.lang.Boolean
     * @return 某类的全部属性
     */
    public static Field[] getAllField(String path) {
        Class<?> cc = getClassBase(path);
        if (cc == null) {
            return null;
        } else {
            return cc.getDeclaredFields();
        }
    }

    /**
     * @param path eg:java.lang.Boolean
     * @return 某类的全部方法
     */
    public static Method[] getAllMethods(String path) {
        Class<?> cc = getClassBase(path);
        if (cc == null) {
            return null;
        } else {
            return cc.getMethods();
        }
    }

    /**
     * @return 某类的父类
     */
    public static Class<?> getSuperClass(String path) {
        Class<?> cc = getClassBase(path);
        //注意了，这里不会是数组，java中单继承
        if (cc == null) {
            return null;
        } else {
            return cc.getSuperclass();
        }
    }

    /**
     * @param path eg:java.lang.Boolean
     * @return 某类的Class
     * @apiNote 通过包名+类名获取到类对象
     */
    public static Class<?> getClassBase(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


}
