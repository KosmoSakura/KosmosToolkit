package cos.mos.toolkit.java;

import java.util.Locale;

/**
 * @Description 单位换算工具类
 * @Author Kosmos
 * @Date 2018.10.31 14:02
 * @Email KosmoSakura@gmail.com
 * @Tip 2018.11.29:重构
 * @Tip 2019.2.22:分类
 * @Tip 2019.2.25:算法优化
 */
public class UUnitFull {
    /**
     * @param units 单位：Hz
     * @return 格式化十进制单位
     */
    public static String formatUnits(float digit, String units) {
        float kiloByte = digit / 1000;
        if (kiloByte < 1) {
            return formatTwo(digit) + units;
        }
        float megaByte = kiloByte / 1000;
        if (megaByte < 1) {
            return formatTwo(kiloByte) + " K" + units;
        }
        float gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            return formatTwo(megaByte) + " M" + units;
        }
        float teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            return formatTwo(gigaByte) + " G" + units;
        }
        return formatTwo(teraBytes) + " T" + units;
    }

    /**
     * @param size 单位：字节
     * @apiNote 格式化字节单位
     */
    public static String formatBit(float size) {
        float kiloByte = size / 1024;
        if (kiloByte < 1) {
            return formatTwo(size) + "\tB";
        }
        float megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            return formatTwo(kiloByte) + "\tKB";
        }
        float gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            return formatTwo(megaByte) + "\tMB";
        }
        float teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            return formatTwo(gigaByte) + "\tGB";
        }
        return formatTwo(teraBytes) + "\tTB";
    }

    /**
     * @param millisecond 单位：毫秒
     * @apiNote 格式化字节单位
     */
    public static String formatTime(float millisecond) {
        float second = millisecond / 1000;
        if (second < 1) {
            return formatTwo(millisecond) + "ms";
        }
        float min = second / 60;
        if (min < 1) {
            return formatTwo(second) + "s";
        }
        float h = min / 60;
        if (h < 1) {
            return formatTwo(min) + "min";
        }
        float day = h / 24;
        if (day < 1) {
            return formatTwo(h) + "h";
        }
        float mon = day / 30;
        if (mon < 1) {
            return formatTwo(day) + "D";
        }
        float year = mon / 12;
        if (year < 1) {
            return formatTwo(mon) + "M";
        }
        return formatTwo(year) + "Y";
    }

    /**
     * @apiNote 保留两位小数
     * *方式一
     * DecimalFormat df1 = new DecimalFormat("0.00");
     * String str = df1.format(num);
     * System.out.println(str);  //13.15
     * *方式二:#.00 表示两位小数 #.0000四位小数
     * DecimalFormat df2 =new DecimalFormat("#.00");
     * String str2 =df2.format(num);
     * System.out.println(str2);  //13.15
     * *方式三
     * %.2f 其中：%→表示 小数点前任意位数   2→表示两位小数 格式后的结果为f→表示浮点型
     * String result = String.format("%.2f", num);
     * System.out.println(result);  //13.15
     * *方式四
     * BigDecimal bigDecimal = new BigDecimal(Double.toString(digit));
     * bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
     * setScale(1)→保留一位小数，默认四舍五入方式
     * setScale(1,BigDecimal.ROUND_DOWN)→直接删除多余的小数位，如2.35会变成2.3
     * setScale(1,BigDecimal.ROUND_UP)→进位处理，2.35变成2.4
     * setScale(1,BigDecimal.ROUND_HALF_UP)→四舍五入，2.35变成2.4
     * setScaler(1,BigDecimal.ROUND_HALF_DOWN)→四舍五入，2.35变成2.3，如果是5则向下舍
     * setScaler(1,BigDecimal.ROUND_CEILING)→接近正无穷大的舍入
     * setScaler(1,BigDecimal.ROUND_FLOOR)→接近负无穷大的舍入，数字>0和ROUND_UP作用一样，数字<0和ROUND_DOWN作用一样
     * setScaler(1,BigDecimal.ROUND_HALF_EVEN)→向最接近的数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
     */
    private static String fotmat(double digit) {
        return String.format(Locale.CHINA, "%.2f", digit);
    }

    public static String fotmat(float digit) {
        return String.format(Locale.CHINA, "%.2f", digit);
    }

    /**
     * @param digit 输入
     * @return 2位有效小数
     * 截断2位以上的小数(最优先使用
     */
    private static float formatTwo(float digit) {
        return (int) (digit * 100f) / 100f;
    }

    private static double formatTwo(double digit) {
        return (int) (digit * 100d) / 100d;
    }

    /**
     * @param version    要比较的版本号
     * @param versionOld 被比较的版本号
     * @return 比较版本号的大小, 前者大则返回一个正数, 后者大返回一个负数, 相等则返回0
     */
    public static int compareVersion(String version, String versionOld) {
        if (version == null || versionOld == null) {
            throw new NullPointerException("传入对象为空");
        }
        String[] versionArray1 = version.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = versionOld.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }


    /**
     * @param celsius 摄氏度
     * @return 华氏度
     */
    public static float cToF(float celsius) {
        return 9 * celsius / 5 + 32;
    }

    /**
     * @param fahrenheit 华氏度
     * @return 摄氏度
     */
    public static float fToC(float fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

}
