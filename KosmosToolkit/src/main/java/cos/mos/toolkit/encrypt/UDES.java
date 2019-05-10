package cos.mos.toolkit.encrypt;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @Description: DES加密解密
 * @Author: Kosmos
 * @Date: 2019.05.10 14:51
 * @Email: KosmoSakura@gmail.com
 */
public class UDES {
    private static final String KEY = "12345678";
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    /**
     * @param str 明文
     * @param pwd 加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @apiNote DES算法，加密
     */
    public static String encode(String str, String pwd) {
        if (str == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(pwd.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(KEY.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(str.getBytes());
            return PtHex.byte2hex(bytes);
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * @param str 密文
     * @param key 解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @apiNote DES算法，解密
     */
    public static String decode(String str, String key) {
        if (str == null)
            return "";
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(KEY.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(PtHex.hex2byte(str.getBytes())));
        } catch (Exception e) {
            return str;
        }
    }
}
