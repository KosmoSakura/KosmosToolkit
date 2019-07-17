package cos.mos.toolkit.io;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void init(Context context, String spName) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sp.edit();
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

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return sp.getFloat(key, -1f);
    }

    public long getLong(String key, long value) {
        return sp.getLong(key, value);
    }

    public int getInt(String key, int value) {
        return sp.getInt(key, value);
    }

    public String getString(String key) {
        return getString(key, "");
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
}
