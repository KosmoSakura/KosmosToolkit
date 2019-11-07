package cos.mos.toolkit.java;

import android.text.Editable;
import android.widget.TextView;

import java.util.List;

/**
 * @Description 字符校验类
 * @Author Kosmos
 * @Date 2016年8月29日 11:32
 * @Email KosmoSakura@gmail.com
 * @Tip 2018.9.12:转录
 * @Tip 2018.9.22:重载方法
 * @Tip 2018.11.27:重构
 * @Tip 2019.3.7:优化文字处理的执行效率
 * @Tip 2019.4.8:优化重构变量
 * @Tip 2019.11.7:追加空处理类型
 * @apiNote 本类中的空值判断：长度为0都为false
 */
public class UTextFull {

    /**
     * @apiNote 递归：移除list中为null的元素
     */
    public static void nullClear(List list) {
        if (list.contains(null)) {
            list.remove(null);
            if (list.contains(null)) {
                nullClear(list);
            }
        }
    }

    //------------------------------------------------------------------------------------------------

    /**
     * @return 返回str长度，为空返回指定值
     */
    public static int getLength(String str, final int defaul) {
        return isEmpty(str) ? defaul : str.length();
    }

    /**
     * @return 返回str长度，为空返回0
     */
    public static int getLength(String str) {
        return getLength(str, 0);
    }

    /**
     * @return 文本控件内容长度，为空返回0
     */
    public static int getLength(TextView tv) {
        return isEmpty(tv) ? 0 : tv.length();
    }

    /**
     * @apiNote 获取数组长度(为空返回0)
     */
    public static int getLength(String[] arr) {
        return arr == null ? 0 : arr.length;
    }

    /**
     * @return 获取list长度(为空返回0)
     */
    public static int getLength(List list) {
        return list == null ? 0 : list.size();
    }

    /**
     * @return {@code true}: 相等<br>{@code false}: 不相等
     * @apiNote 判断两字符是否相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

//-------------------------------------------------------------------------------------------------------------------

    /**
     * @apiNote String为空返回指定字符
     */
    public static String isNull(String str, final String defaul) {
        return isEmpty(str) ? defaul : str;
    }

    /**
     * @apiNote String为空返回""
     */
    public static String isNull(String str) {
        return isNull(str, "");
    }

    public static String isNull(CharSequence str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

    public static String isNull(CharSequence str) {
        return isNull(str, "");
    }

    public static String isNull(Editable str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

    public static String isNull(Editable str) {
        return isNull(str, "");
    }

    /**
     * @apiNote TextView为空返回""
     */
    public static String isNull(TextView tv) {
        return isEmpty(tv) ? "" : tv.getText().toString();
    }
//-------------------------------------------------------------------------------------------------------------------

    /**
     * @return digit为空返回缺省数字
     */
    public static double isNull(Double digit, final double defasult) {
        return digit == null ? defasult : digit;
    }

    public static double isNull(Double digit) {
        return isNull(digit, 0.0D);
    }

    public static long isNull(Long digit, final long defasult) {
        return digit == null ? defasult : digit;
    }

    public static long isNull(Long digit) {
        return isNull(digit, 0L);
    }

    public static float isNull(Float digit) {
        return isNull(digit, 0f);
    }

    public static float isNull(Float digit, final float defasult) {
        return digit == null ? defasult : digit;
    }

    public static int isNull(Integer digit) {
        return isNull(digit, 0);
    }

    public static int isNull(Integer digit, final int defasult) {
        return digit == null ? defasult : digit;
    }

    public static short isNull(Short digit) {
        return isNull(digit, (short) 0);
    }

    public static short isNull(Short digit, final short defasult) {
        return digit == null ? defasult : digit;
    }

    public static byte isNull(Byte digit) {
        return isNull(digit, (byte) 0);
    }

    public static byte isNull(Byte digit, final byte defasult) {
        return digit == null ? defasult : digit;
    }


//-------------------------------------------------------------------------------------------------------------------

    /**
     * @param str 被校验的字符
     * @return 字符是否为空{@code true}: 空  {@code false}: 不为空
     * @Tip 字符串是否为空(只有空格也为空 ）
     * @Tip str.length () == 0:可以减少一次判断
     * @Tip null.toString().length =4
     * @Tip str.equalsIgnoreCase(null.toString ())=true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }

    public static boolean isEmpty(CharSequence sequence) {
        return isEmpty(sequence.toString());
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

    public static boolean isEmpty(Boolean b) {
        return b == null ? false : b;
    }

    //-------------------------------------------------------------------------------------------------------------------
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0 && str.trim().length() == 0 && !str.equalsIgnoreCase("null");
    }

    public static boolean isNotEmpty(CharSequence sequence) {
        return isNotEmpty(sequence.toString());
    }

    public static boolean isNotEmpty(TextView tv) {
        return tv != null && isNotEmpty(tv.getText());
    }

    public static boolean isNotEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static <Z> boolean isNotEmpty(Z[] arr) {
        return arr != null && arr.length > 0;
    }
}
