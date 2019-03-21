package cos.mos.utils.utils.io;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @Description: SharedPreferences工具
 * @Author: Kosmos
 * @Date: 2018年08月03日 14:08
 * @Email: KosmoSakura@gmail.com
 * @eg: 修改日期：2018年10月8日
 * @eg: 修改日期：2018年12月22日
 */
public class USP {
    private volatile static USP instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private USP() {
    }

    public static USP instance() {
        if (instance == null) {
            synchronized (USP.class) {
                if (instance == null) {
                    instance = new USP();
                }
            }
        }
        return instance;
    }

    /**
     * 在Application里面初始化
     */
    public void init(Context context, String spName) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public long getLong(String key, long value) {
        return sp.getLong(key, value);
    }

    public int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public String getString(String key, String value) {
        return sp.getString(key, value);
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean value) {
        return sp.getBoolean(key, value);
    }

    /**
     * 存入Object，自动判断类型
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
//        boolean commit = editor.commit();
        editor.apply();
    }

    /**
     * 取出Object，，自动判断类型
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return sp.getString(key, null);
        }
    }

    /**
     * 删除某一key对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清空SharedPreferences
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }
}
