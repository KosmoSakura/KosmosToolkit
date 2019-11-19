package cos.mos.toolkit.java;

/**
 * @Description 字符处理类
 * @Author Kosmos
 * @Date 2019.04.30 19:36
 * @Email KosmoSakura@gmail.com
 */
public class UString {
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
}
