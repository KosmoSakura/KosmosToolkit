package cos.mos.toolkit.java;

import android.text.InputFilter;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 正则表达式
 * @Author: Kosmos
 * @Date: 2016年9月21日 17:01
 * @Email: KosmoSakura@gmail.com
 * →→is：是某种特定字符
 * →→checkXX:校验类
 * →→fun：功能类
 * @eg: 2018.9.12:转录
 * @eg: 2018.12.26:重构
 * @eg: 2019.2.28:Url判断
 * @eg: 2019.3.20:新增几个正则校验
 */
public class URegular {

    private static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };
    /**
     * @apiNote 禁止输入表情，Edt加上过滤器
     */
    public static InputFilter[] emojiFilters = {emojiFilter};

//--check校验类-------------------------------------------------------------------------------------------------------

    /**
     * @param urls 判断字符串是否为URL（https://blog.csdn.net/bronna/article/details/77529145）
     * @return true:是URL、false:不是URL
     */
    public static boolean checkUrl(String urls) {
        boolean isUrl;
        // 判断是否是网址的正则表达式
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
            + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";

        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        isUrl = mat.matches();
        return isUrl;
    }

    /**
     * @apiNote true→不包含符号
     */
    public static boolean checkSymbol(String str) {
        return str.matches("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
    }

    /**
     * @apiNote true→纯汉字
     */
    public static boolean checkChineseCharacter(String str) {
        return str.matches("^[\\u4e00-\\u9fa5]+$");
    }

    /**
     * @apiNote true→ (汉字)||(字母)
     */
    public static boolean checkChineseLetter(String str) {
        return str.matches("^[a-zA-Z\\u4e00-\\u9fa5]+$");
    }

    /**
     * @apiNote true→(字母)||(数字)
     */
    public static boolean checkLetterDigit(String str) {
        return str.matches("^[A-Za-z0-9]+$");
    }

    /**
     * @apiNote true→(汉字)||(字母)||(数字)
     */
    public static boolean checkChineseLetterDigit(String str) {
        return str.matches("^[a-z0-9A-Z\\u4e00-\\u9fa5]+$");
    }

    /**
     * @apiNote true→昵称可由：(中文)||(英文)||(数字)||("_")||("-")
     */
    public static boolean checkNickName(String nickName) {
        return nickName.matches("^[_\\-a-zA-Z0-9\\u4e00-\\u9fa5]+$");
    }

    /**
     * @param digit 一位或多位0-9之间的整数
     * @apiNote true→(正整数)||(负整数)
     */
    public static boolean checkDigit(String digit) {
        return digit.matches("-?[1-9]\\d+");
    }


    /**
     * @param date 日期，格式：1992-09-03，或1992.09.03
     * @apiNote true→是上述年月日
     */
    public static boolean checkDate(String date) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, date);
    }

//--check判断类-------------------------------------------------------------------------------------------------------

    /**
     * @apiNote true→是手机号码
     */
    public static boolean iskPhoneNum(String phone) {
        return phone.matches("(\\+\\d+)?1\\d{10}$");
    }

    /**
     * @apiNote true→是身份证号码
     */
    public static boolean isIdCard(String idcard) {
        return idcard.matches("[1-9]\\d{16}[a-zA-Z0-9]");
    }

    /**
     * @apiNote true→是邮箱
     */
    public static boolean isEmail(String email) {
        return email.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?");
    }

//--Fun工具类-------------------------------------------------------------------------------------------------------

    /**
     * @apiNote 隐藏手机号中间4位
     * @eg 185****9095
     */
    public static String funHidePhone(String input) {
        return input.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * @apiNote 隐藏身份证号中间N位
     * @eg 511***********5815
     */
    public static String funHideIDCard(String input) {
        return input.replaceAll("(\\d{3})\\d+(\\d{4})", "$1***********$2");
    }

    /**
     * @apiNote 隐藏银行卡号前几位
     * @eg **** **** **** **** 309
     */
    public static String funHideBankF(String input) {
        return input.replaceAll("([\\d]{4})(?=\\d)", "**** ");
    }

    /**
     * @apiNote 银行卡号每隔四位增加一个空格
     * @eg 6225 8801 3770 6868
     */
    public static String funBankCardChar(String input) {
        return input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
    }

    /**
     * @param input 6225880137706868
     * @param text  "-"
     * @apiNote 银行卡号每隔四位增加一个指定字符
     * @apiNote 银行卡号每隔四位增加一个指定字符
     * @eg 6225-8801-3770-6868
     */
    public static String funBankCardChar(String input, String text) {
        return input.replaceAll("([\\d]{4})(?=\\d)", "$1" + text);
    }

    /**
     * @param digit 1236.51634
     * @apiNote 每隔3位加一个逗号
     * 方式一:使用DecimalFormat
     * @eg 1, 236.516
     */
    public static String funformatDigit_3(double digit) {
        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setGroupingSize(3);
        return df1.format(digit);
    }

    /**
     * @param input 1236.51634
     * @apiNote 每隔3位加一个逗号
     * 方式二:使用正则表达式
     * @eg 1, 236.5, 1634
     */
    public static String funformatDigit_3(String input) {
        String regx = "(?<=\\d)(\\d{3})";
        return input.replaceAll(regx, ",$1");
    }

    /**
     * @param digit  5556.7468f
     * @param fotmat "#,##0.00"
     * @return 5, 556.75
     * @apiNote 按照指定格式转化数字
     */
    public static String formatDigitString(double digit, String fotmat) {
        return new DecimalFormat(fotmat).format(digit);
    }
}