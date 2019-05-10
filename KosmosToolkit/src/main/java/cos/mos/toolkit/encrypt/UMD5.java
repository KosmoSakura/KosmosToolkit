package cos.mos.toolkit.encrypt;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: MD5加密
 * @Author: Kosmos
 * @Date: 2019.04.08 15:24
 * @Email: KosmoSakura@gmail.com
 */
public class UMD5 {
    private UMD5() {
        throw new UnsupportedOperationException("你不能搞我");
    }

    /**
     * @param str 明文
     * @return MD5加盐加密（默认盐）
     */
    public static String encrypt(String str) {
        return encrypt(str, "&%5123***&&%%$$#@");
    }

    /**
     * @param str  明文
     * @param salt 传入盐
     * @return MD5加盐加密
     */
    public static String encrypt(String str, String salt) {
        MessageDigest md;//指定算法名称的信息摘要
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        //完成摘要计算=>使用指定的字节数组对摘要进行最后更新
        md.update((str + salt).getBytes(), 0, str.length());
        byte[] results = md.digest();
        StringBuilder resultString = new StringBuilder();
        for (byte result : results) {
            int b = (0xFF & result);
            if (b <= 0xF)
                resultString.append("0");
            resultString.append(Integer.toHexString(b));
        }
        resultString = new StringBuilder(resultString.toString().toUpperCase());
        return resultString.toString();
    }
}
