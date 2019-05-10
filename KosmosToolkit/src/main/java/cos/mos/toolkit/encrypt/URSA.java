package cos.mos.toolkit.encrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @Description: RSA加密解密
 * @Author: Kosmos
 * @Date: 2019.05.10 14:49
 * @Email: KosmoSakura@gmail.com
 */
public class URSA {
    private static URSA rsa;
    private KeyPair keyPair;

//    public static void main(String[] args) {
//        String publicKey = URSA.getInstance().getPublicKey();
//        String privateKey = URSA.getInstance().getPrivateKey();
//        System.out.println("公钥：" + publicKey);
//        System.out.println("私钥：" + privateKey);
//        String encrypt = URSA.getInstance().encrypt("RSA加密解密", publicKey);
//        System.out.println("加密：" + encrypt);
//        System.out.println("解密：" + URSA.getInstance().decrypt(encrypt, privateKey));
//    }

    private URSA() {
    }

    public static URSA getInstance() {
        if (rsa == null) {
            rsa = new URSA();
        }
        return rsa;
    }

    /**
     * @return 生成公钥
     */
    public String getPublicKey() {
        if (getKeyPair() == null) {
            return "";
        } else {
            return PtBase64.encode(getKeyPair().getPublic().getEncoded());
        }
    }

    /**
     * @return 生成私钥
     */
    public String getPrivateKey() {
        if (getKeyPair() == null) {
            return "";
        } else {
            return PtBase64.encode((getKeyPair().getPrivate().getEncoded()));
        }
    }

    /**
     * @param str       明文
     * @param publicKey 公钥
     * @return 密文
     * @apiNote RSA公钥加密
     */
    public String encrypt(String str, String publicKey) {
        Cipher cipher = null;
        try {
            //base64编码的公钥
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(PtBase64.decode(publicKey)));
            //RSA加密
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return PtBase64.encode(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param str        密文
     * @param privateKey 私钥
     * @return 明文
     * @apiNote RSA私钥解密
     */
    public String decrypt(String str, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = PtBase64.decode(str);
            //base64编码的私钥
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(PtBase64.decode(privateKey)));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @apiNote 随机生成密钥对
     */
    private KeyPair getKeyPair() {
        if (keyPair == null) {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen;
            try {
                keyPairGen = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            keyPair = keyPairGen.generateKeyPair();
        }
        return keyPair;
    }
}
