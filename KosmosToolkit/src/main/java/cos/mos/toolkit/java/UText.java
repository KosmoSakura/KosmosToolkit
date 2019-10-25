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
 * @Tip 2019.10.25:优化判断逻辑执行效率
 * @apiNote 本类中的空值判断：长度为0都为false
 */
public class UText {
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
     * @apiNote Long为空返回-1
     */
    public static long isNull(Long l) {
        return isNull(l, -1);
    }

    /**
     * Long为空返回为空返回指定值
     *
     * @param l        长整型
     * @param defasult 缺省数字
     * @return 为空返回缺省数字
     */
    public static long isNull(Long l, final long defasult) {
        return l == null ? defasult : l;
    }

    /**
     * @apiNote Float为空返回-1
     */
    public static float isNull(Float l) {
        return isNull(l, -1f);
    }

    /**
     * @param l        浮点型
     * @param defasult 缺省数字
     * @return 为空返回缺省数字
     */
    public static float isNull(Float l, final float defasult) {
        return l == null ? defasult : l;
    }

    /**
     * @apiNote Integer为空返回-1
     */
    public static int isNull(Integer integer) {
        return isNull(integer, -1);
    }

    /**
     * @param integer  整型
     * @param defasult 缺省数字
     * @return 为空返回缺省数字
     */
    public static int isNull(Integer integer, final int defasult) {
        return integer == null ? defasult : integer;
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
