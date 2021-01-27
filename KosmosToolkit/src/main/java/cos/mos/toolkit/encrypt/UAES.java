package cos.mos.toolkit.encrypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description UAES
 * @Author Kosmos
 * @Date 2021-1-27-027 14:12
 * @Email KosmoSakura@gmail.com
 * */
public class UAES {
    private static final String ALGORITHM = "AES";//加密算法
    /**
     * @tip 偏移量
     * @tip AES 为16bytes, DES 为8bytes
     */
//    private static final IvParameterSpec zeroIv = new IvParameterSpec("1234567876543210".getBytes());
    /**
     * @tip 编码方式
     * @tip 选择：UTF-8 、GBK
     */
    private static final Charset CODE_TYPE = StandardCharsets.UTF_8;//编码方式
    /**
     * @tip 填充类型
     * @tip 选择：AES/ECB/PKCS5Padding 、AES/ECB/PKCS7Padding 、AES/ECB/NoPadding 、 AES/CBC/ZeroPadding
     * @tip PKCS5Padding：比PKCS7Padding效率高
     * @tip PKCS7Padding：可支持IOS加解密
     * @tip NoPadding：此类型 加密内容,密钥必须为16字节的倍数位,否则抛异常,需要字节补全再进行加密
     * @tip ZeroPadding：java不支持
     */
    private static final String AES_TYPE = "AES/ECB/PKCS5Padding";//填充类型

    //字符补全
    private static final String[] consult = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G"};


    /**
     * @param cleartext 明文
     * @return 密文：加密操作,返回密文字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等。
     * @tip SecretKeySpec(byte[], String)
     * 参数1:私钥字节数组
     * 参数2:加密方式 AES或者DES
     * @tip cipher.init(int, key, SecureRandom)
     * 1.int:加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;
     * 2.AES/CBC/ZeroPadding类型可以在第三个参数传递偏移量 zeroIv,AES/ECB/*没有偏移量
     * 3.第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)
     * @tip pwd 私钥:AES固定格式为128/192/256 bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes
     */
    public static String encrypt(String cleartext, String pwd) {
        try {
            SecretKeySpec key = new SecretKeySpec(pwd.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(CODE_TYPE));
//            return PtBase64Java8.encrypt(encryptedData);
            return PtBase64.encode(encryptedData);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param encrypted 密文
     * @return 明文
     * @tip cipher.init(int, key, SecureRandom)
     * 1.int:加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;
     * @tip pwd 私钥:AES固定格式为128/192/256 bits.即：16/24/32bytes。DES固定格式为128bits，即8bytes
     * 长度16位:1111222233334444
     * 长度24位:111122223333444455556666
     * 长度16位:11112222333344445555666677778888
     */
    public static String decrypt(String encrypted, String pwd) {
        try {
//            byte[] byteMi = PtBase64Java8.decode(encrypted);
            byte[] byteMi = PtBase64.decode(encrypted);
            SecretKeySpec key = new SecretKeySpec(pwd.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, CODE_TYPE);
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * @param cleartext 明文
     * @tip 填充类型NoPadding需要补全16位倍数
     * 当不足16*n字节是进行补全, 差一位时 补全16+1位
     * 补全字符,以 $ 开始,$后一位代表$后补全字符位数,之后全部以0进行补全
     */
    public static String completionCodeFor16Bytes(String cleartext) {
        int num = cleartext.getBytes(CODE_TYPE).length;
        int index = num % 16;
        StringBuilder sb = new StringBuilder(cleartext);
        if (index != 0) {
            if (16 - index == 1) {
                sb.append("$").append(consult[16 - 1]).append(addStr(16 - 1 - 1));
            } else {
                sb.append("$").append(consult[16 - index - 1]).append(addStr(16 - index - 1 - 1));
            }
        }
        return sb.toString();
    }

    //追加字符
    private static String addStr(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    //还原字符(进行字符判断)
    public static String resumeCodeOf16Bytes(String str) {
        int indexOf = str.lastIndexOf("$");
        if (indexOf == -1) {
            return str;
        }
        String trim = str.substring(indexOf + 1, indexOf + 2).trim();
        int num = 0;
        for (int i = 0; i < consult.length; i++) {
            if (trim.equals(consult[i])) {
                num = i;
            }
        }
        if (num == 0) {
            return str;
        }
        return str.substring(0, indexOf).trim();
    }


    public static void test(String content) {
        String AES_KEY = "111122223333444455556666";
        StringBuilder sb = new StringBuilder();
        sb.append("明文：").append(content);

        //NoPadding需要补全16位
        if (AES_TYPE.equals("AES/ECB/NoPadding")) {
            sb.append("\n\n");
            content = completionCodeFor16Bytes(content);
            sb.append("明文补全后：").append(content);
        }
        sb.append("\n\n");
        // 加密
        String encryptResult = encrypt(content, AES_KEY);
        sb.append("密文：").append(encryptResult);
        sb.append("\n\n");
        // 解密
        String decryptResult = decrypt(encryptResult, AES_KEY);
        //NoPadding需要补全16位
        if (AES_TYPE.equals("AES/ECB/NoPadding")) {
            sb.append("明文还原前：").append(decryptResult);
            sb.append("\n\n");
            decryptResult = resumeCodeOf16Bytes(decryptResult);
        }
        sb.append("明文：").append(decryptResult);
        System.out.println(sb.toString());
    }
}