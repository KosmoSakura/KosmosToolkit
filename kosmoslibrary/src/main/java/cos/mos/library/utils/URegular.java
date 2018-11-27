package cos.mos.library.utils;

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
 * @eg: 修改日期：2018年09月12日 16:19
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
     * 禁止输入表情
     */
    public static InputFilter[] emojiFilters = {emojiFilter};

    /**
     * 使用正则表达式检查手机号码
     */
    public static boolean checkPhoneNum(String phone) {
        return phone.matches(RegularExp.REGULAR_EXPRESSION_MOBILE);
    }

    /**
     * 使用正则表达式检查标点
     */
    public static boolean checkSign(String phone) {
        return phone.matches(RegularExp.REGULAR_EXPRESSION_SIGN);
    }

    /**
     * 校验纯汉字
     */
    public static boolean checkChineseCharacters(String name) {
        return name.matches("^[\\u4e00-\\u9fa5]+$");
    }

    public static void main(String[] args) {
        String card = "534534534534535";
//        System.out.println("      card: " + card);
//        System.out.println("check code: " + getBankCardCheckCode(card.substring(0, card.length() - 1)));
//        System.out.println("   card id: " + card + getBankCardCheckCode(card));
//        System.out.println(checkBankCard(card));
//        System.out.println(formBankCard(card));
//        formatFileSize();
//        System.out.println("1,纯数字:" + checkNumber("123456"));
//        System.out.println("1，纯字母:" + checkNumber("aaaaaavvs"));
//        System.out.println("1，纯符号:" + checkNumber("+-/**-+-/**-"));
//        System.out.println("2，字数:" + checkNumber("aaAav4596"));
//        System.out.println("2，字符:" + checkNumber("aaaav-/**-"));
//        System.out.println("2，符数:" + checkNumber("5-/**-596"));
//        System.out.println("3，字符数:" + checkNumber("aaaa*-596"));
    }

    /**
     * @return 必须包含 数字,字母,符号 3项组合
     */
    public static boolean checkPassword_3(String password) {
        return password.matches("(?:(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[,\\.#%'\\+\\*\\-:;^_`].*))[,\\.#%'\\+\\*\\-:;^_`0-9A-Za-z]{6,16}$");
    }

    /**
     * @return 只能包含字母（大小写）和数字
     */
    public static boolean checkPassword(String password) {
        return password.matches("^[A-Za-z0-9]+$");
    }

    /**
     * 6位纯数字
     */
    public static boolean checkNumber(String password) {
        return password.matches("[0-9]+");
    }


    /**
     * 验证身份证号码
     */
    public static boolean checkIdCard(String idcard) {
        return idcard.matches(RegularExp.REGULAR_EXPRESSION_ID_CARD);
    }

    /**
     * 验证邮箱
     */
    public static boolean checkEmail(String email) {
        return email.matches(RegularExp.REGULAR_EXPRESSION_CONTACT_EMAIL);
    }

    /**
     * 使用正则表达式检查用户名
     */
    public static boolean checkUsername(String name) {
        return name.matches(RegularExp.REGULAR_EXPRESSION_CONTACT);
    }

    /**
     * 隐藏手机号中间4位
     */
    public static String formPhoneNo(String input) {
        return input.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 隐藏身份证号中间N位
     */
    public static String formIDCardNo(String input) {
        return input.replaceAll("(\\d{3})\\d+(\\d{4})", "$1***********$2");
    }

    /**
     * 银行卡号每隔四位增加一个空格
     *
     * @param input : 银行卡号,例如"6225880137706868"
     */
    public static String formBankCard(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
        return result;
    }

    /**
     * 隐藏银行卡号前几位
     */
    public static String formBankCard2(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "**** ");
        return result;
    }

    /**
     * 隐藏银行卡号前几位
     */
    public static String formBankCard3(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
        return result;
    }

    /**
     * 格式化数字
     * 方式一:使用DecimalFormat
     */
    public static String formatDigitString(String string, int digitLength) {
//        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormat df1 = new DecimalFormat("#,##0.00");
        df1.setGroupingSize(digitLength);
        String result = null;
        try {
//            string = new DecimalFormat("0.00").format(Float.parseFloat(string));
            result = df1.format(Float.parseFloat(string));
            return result;
        } catch (Exception e) {
            return string;
        }
    }

    /**
     * 格式化数字
     * 方式一:使用DecimalFormat
     */
    public static void formatFileSize() {
        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setGroupingSize(3);
        String result = df1.format(1234567.45);
        System.out.println(result);
    }

    /**
     * 格式化数字
     * 方式二:使用正则表达式
     */
    public static String digit(String input) {
//        String input = "1234567.45634";
        String regx = "(?<=\\d)(\\d{4})";
//        System.out.println(input.replaceAll(regx, " $1"));
        return input.replaceAll(regx, " $1");
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
            || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    private class RegularExp {
        private static final String REGULAR_EXPRESSION_MOBILE = "(\\+\\d+)?1[345789]\\d{9}$";
        private static final String REGULAR_EXPRESSION_SIGN = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        private static final String REGULAR_EXPRESSION_ID_CARD = "[1-9]\\d{16}[a-zA-Z0-9]";
        private static final String REGULAR_EXPRESSION_REAL_NAME = "^[\\u4e00-\\u9fa5]+[·•●]{0,1}[\\u4e00-\\u9fa5]+$";
        private static final String REGULAR_EXPRESSION_CONTACT = "^[~\\-_`!\\/@#$%\\^\\+\\*&\\\\?\\|:\\.<>\\[\\]{}()';=\",]*$";
        private static final String REGULAR_EXPRESSION_CONTACT_EMAIL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        private static final String REGUILAR_EXPERSSION_AMOUNT = "^(([1-9]\\d*)(\\.\\d{1,2})?)$|(0\\.0?([1-9]\\d?))$";
        private static final String REGULAR_Emoji_Not = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    }
}