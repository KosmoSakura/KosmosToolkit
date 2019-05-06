package cos.mos.utils.net.okhttp.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 09:56
 * @Email: KosmoSakura@gmail.com
 */
public class CookiesManager implements CookieJar {
    private static PersistentCookieStore cookieStore = PersistentCookieStore.getInstance();
    private static CookiesManager mInstance;

    private CookiesManager() {
    }

    public static CookiesManager getInstance() {
        if (mInstance == null) {
            synchronized (CookiesManager.class) {
                if (mInstance == null) {
                    mInstance = new CookiesManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                if (!item.value().equalsIgnoreCase("deleteMe")) {
                    cookieStore.add(url, item);
                }
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
//        Log.e("kosmos", "cookiesS:"+cookies.toString());
        return cookies;
    }
}
