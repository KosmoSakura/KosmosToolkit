package cos.mos.toolkit.java;

/**
 * @Description 单位换算工具类
 * @Author Kosmos
 * @Date 2018.10.31 14:02
 * @Email KosmoSakura@gmail.com
 * @Tip 2018.11.29:重构
 * @Tip 2019.2.22:分类
 * @Tip 2019.2.25:算法优化
 */
public class UUnit {
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
