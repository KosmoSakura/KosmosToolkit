package cos.mos.utils.utils.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description: 反射辅助工具
 * @Author: Kosmos
 * @Date: 2018.11.25 16:31
 * @Email: KosmoSakura@gmail.com
 * @eg: 最新修改日期：2018年11月25日 17:19
 */
public class UReflex {
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
