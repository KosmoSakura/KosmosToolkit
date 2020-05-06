package cos.mos.toolkit.java;

import java.util.Iterator;
import java.util.List;

/**
 * @Description 字符处理类
 * @Author Kosmos
 * @Date 2019.04.30 19:36
 * @Email KosmoSakura@gmail.com
 * @Tip 2020.5.6:新增几个函数
 */
public class UString {
    private static String[] EMPTY_STRING_ARRAY = new String[]{};

    /**
     * @param str "1967+2356-433*12/66"
     * @return [1967], [2356], [433], [12], [66]
     */
    public static String[] split(String str) {
        return str.split("[+\\-*/]");
    }

    /**
     * @return 返回：首字母大写字符串
     */
    public static String upperFirstLetter(String str) {
        if (UText.isEmpty(str) || !Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) - 32)) + str.substring(1);
        }
    }

    /**
     * @return 返回：首字母小写字符串
     */
    public static String lowerFirstLetter(String str) {
        if (UText.isEmpty(str) || !Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return String.valueOf((char) (str.charAt(0) + 32)) + str.substring(1);
        }
    }

    /**
     * @apiNote 递归：移除list中为null的元素
     */
    public static void clearNull(List list) {
        if (list.contains(null)) {
            list.remove(null);
            if (list.contains(null)) {
                clearNull(list);
            }
        }
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

    public static String join(CharSequence delimiter, Iterable tokens) {
        final Iterator<?> it = tokens.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }
        return sb.toString();
    }

    public static String[] split(String text, String expression) {
        if (text.length() == 0) {
            return EMPTY_STRING_ARRAY;
        } else {
            return text.split(expression, -1);
        }
    }
}
