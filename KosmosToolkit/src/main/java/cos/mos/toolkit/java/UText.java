package cos.mos.toolkit.java;

import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import java.util.List;

/**
 * @Description: 字符工具类
 * @Author: Kosmos
 * @Date: 2016年8月29日 11:32
 * @Email: KosmoSakura@gmail.com
 * @eg: 2018.9.12:转录
 * @eg: 2018.9.22:重载方法
 * @eg: 2018.11.27:重构
 * @eg: 2019.3.7:优化文字处理的执行效率
 * @eg: 2019.4.8:优化重构变量
 * @apiNote 本类中的空值判断：长度为0都为false
 */
public class UText {

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

    /**
     * @apiNote TextView为空返回""
     */
    public static String isNull(TextView tv) {
        return isEmpty(tv) ? "" : tv.getText().toString();
    }

    /**
     * @apiNote String为空返回""
     */
    public static String isNull(String str) {
        return isNull(str, "");
    }

    /**
     * @apiNote String为空返回指定字符
     */
    public static String isNull(String str, final String defaul) {
        return isEmpty(str) ? defaul : str;
    }

    public static String isNull(Editable str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

    public static String isNull(CharSequence str, final String defaul) {
        return isEmpty(str) ? defaul : str.toString();
    }

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

    /**
     * Boolean为空返回false
     */
    public static boolean isBoolean(Boolean b) {
        return b == null ? false : b;
    }

    /**
     * @apiNote 文本控件内容是否为空
     */
    public static boolean isEmpty(TextView tv) {
        return tv == null || isEmpty(tv.getText());
    }

    /**
     * @apiNote 集合是否为空
     */
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * @apiNote 数组是否为空
     */
    public static <Z> boolean isEmpty(Z[] arr) {
        return arr == null || arr.length == 0;
    }

    /**
     * @apiNote 字符串是否为空(只有空格也为空 ）
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }

    /**
     * @param sequence 被校验的字符
     * @return 字符是否为空{@code true}: 空  {@code false}: 不为空
     * @apiNote (字符为null或长度为0均判定为空)
     */
    public static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
    //------------------------------------------------------------------------------------------------

    /**
     * @apiNote 获取数组长度(为空返回0)
     */
    public static int getSize(String[] arr) {
        return arr == null ? 0 : arr.length;
    }

    /**
     * @return 获取list长度(为空返回0)
     */
    public static int getSize(List list) {
        return list == null ? 0 : list.size();
    }

    /**
     * @return 返回str长度，为空返回0
     */
    public static int getLength(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * @return 返回str长度，为空返回指定值
     */
    public static int getLength(String str, final int defaul) {
        return str == null ? defaul : str.length();
    }

    /**
     * @return 文本控件内容长度，为空返回0
     */
    public static int getLength(TextView tv) {
        return isEmpty(tv) ? 0 : tv.length();
    }

    /**
     * @return 返回：首字母大写字符串
     */
    public static String upperFirstLetter(String str) {
        if (isEmpty(str) || !Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) - 32)) + str.substring(1);
        }
    }

    /**
     * @return 返回：首字母小写字符串
     */
    public static String lowerFirstLetter(String str) {
        if (isEmpty(str) || !Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) + 32)) + str.substring(1);
        }
    }

    //------------------------------------------------------------------------------------------------

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

    /**
     * @return 给字符添加下划线
     */
    public static void getTextUnderLine(TextView t) {
        t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        t.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * @return 下划线
     */
    public static Spanned getTextUnderLine(String str) {
        return Html.fromHtml("<u>" + str + "</u>");
    }

    /**
     * @return 文本加粗
     */
    public static Spanned getTextBold(String str) {
        return Html.fromHtml("<b>" + str + "</b>");
    }

    /**
     * @return 文本斜体
     */
    public static Spanned getTextItalic(String str) {
        return Html.fromHtml("<i>" + str + "</i>");
    }

    /**
     * @param t             TextView
     * @param color         将要单色设置的颜色
     * @param startLocation 使用该颜色的起始位置
     * @param endLocation   使用该颜色的终止位置
     * @param large         使用该颜色的字体与该字符串其他字体大小的倍数
     * @apiNote 动态设置字符串的颜色和大小
     */
    public static void getSpannableString(TextView t, int color, int startLocation, int endLocation, float large) {
        String str = t.getText().toString();
        SpannableString styledText = new SpannableString(str);
        styledText.setSpan(new ForegroundColorSpan(color), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledText.setSpan(new RelativeSizeSpan(large), startLocation, endLocation, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setText(styledText);
    }
}
