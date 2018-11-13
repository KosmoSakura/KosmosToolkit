package cos.mos.library.constant;

import cos.mos.library.Utils.UText;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 12:12
 * @Email: KosmoSakura@gmail.com
 */
public class KConfig {
    public static String Describe;//接口描述
    private static String SP_NAME;//SharedPreferences默认表名
    private static String BASE_URL;//base url

    public static void setSpName(String spName) {
        SP_NAME = spName;
    }

    public static String getSPName() {
        if (UText.isEmpty(SP_NAME)) {
            SP_NAME = "sakura";
        }
        return SP_NAME;
    }

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
