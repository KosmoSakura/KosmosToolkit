package cos.mos.toolkit.java;

import android.text.Editable;
import android.widget.TextView;

import java.util.List;

/**
 * @Description 字符校验类
 * @Author Kosmos
 * @Date 2016年8月29日 11:32
 * @Email KosmoSakura@gmail.com
 * */
public class UTextUsed {
    public static String isNull(TextView tv) {
        return isEmpty(tv) ? "" : tv.getText().toString();
    }

    public static String isNull(String str) {
        return isNull(str, "");
    }
    public static String isNull(String str, final String defaul) {
        return isEmpty(str) ? defaul : str;
    }

    public static String isNull(Editable str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

    public static String isNull(CharSequence str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

    public static long isNull(Long l) {
        return isNull(l, -1);
    }

    public static long isNull(Long l, final long defasult) {
        return l == null ? defasult : l;
    }

    public static float isNull(Float l) {
        return isNull(l, -1f);
    }

    public static float isNull(Float l, final float defasult) {
        return l == null ? defasult : l;
    }
    public static int isNull(Integer integer) {
        return isNull(integer, -1);
    }

    public static int isNull(Integer integer, final int defasult) {
        return integer == null ? defasult : integer;
    }

    public static boolean isBoolean(Boolean b) {
        return b == null ? false : b;
    }

    public static boolean isEmpty(TextView tv) {
        return tv == null || isEmpty(tv.getText());
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static <Z> boolean isEmpty(Z[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }

    public static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
}
