package cos.mos.toolkit.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: 加密工具
 * @Author: Kosmos
 * @Date: 2019.04.08 15:24
 * @Email: KosmoSakura@gmail.com
 */
public class UEncryption {
    private UEncryption() {
        throw new UnsupportedOperationException("你不能搞我");
    }

    public static void main(String[] args) {
        System.out.println(md5("123456"));
    }


    /**
     * @param str 默认盐
     * @return MD5加盐加密
     */
    public static String md5(String str) {
        return md5(str, "&%5123***&&%%$$#@");
    }

    /**
     * @param str
     * @param salt 传入盐
     * @return MD5加盐加密
     */
    public static String md5(String str, String salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        digest.update((str + salt).getBytes(), 0, str.length());
        byte[] p_md5Data = digest.digest();
        StringBuilder m_szUniqueID = new StringBuilder();
        for (byte p_md5Datum : p_md5Data) {
            int b = (0xFF & p_md5Datum);
            if (b <= 0xF)
                m_szUniqueID.append("0");
            m_szUniqueID.append(Integer.toHexString(b));
        }
        m_szUniqueID = new StringBuilder(m_szUniqueID.toString().toUpperCase());
        return m_szUniqueID.toString();
    }
}
