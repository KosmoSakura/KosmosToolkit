package cos.mos.utils.constant;

import cos.mos.toolkit.constant.Code;
import cos.mos.toolkit.java.UText;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 12:12
 * @Email: KosmoSakura@gmail.com
 */
public class KConfig implements Code {
    public static String Describe;//接口描述
    private static String BASE_URL;//base url


    public static String getBaseUrl() {
        if (UText.isEmpty(BASE_URL)) {
            BASE_URL = "";
        }
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }
}
