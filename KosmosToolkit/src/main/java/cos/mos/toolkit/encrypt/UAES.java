package cos.mos.toolkit.encrypt;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: AES加密解密
 * @Author: Kosmos
 * @Date: 2019.05.10 14:49
 * @Email: KosmoSakura@gmail.com
 */
public class UAES {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

//    public static void main(String[] args) {
//        String encrypt = encrypt("AES加密解密", "asv1234");
//        System.out.println("加密：" + encrypt);
//        System.out.println("解密："+decrypt(encrypt,"asv1234"));
//    }

    /**
     * @param str 明文
     * @param pwd 密钥
     * @return 密文
     * @apiNote 加密
     */
    public static String encrypt(String str, String pwd) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);//密码器
            byte[] byteContent = str.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(pwd));//密码器初始化为加密模式
            byte[] result = cipher.doFinal(byteContent);// 加密
            return PtBase64.encode(result);//Base64转码返回
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * @param str 密文
     * @param pwd 密钥
     * @return 明文
     * @apiNote 解密
     */
    public static String decrypt(String str, String pwd) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(pwd));
            //执行操作
            byte[] result = cipher.doFinal(PtBase64.decode(str));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * @return 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
