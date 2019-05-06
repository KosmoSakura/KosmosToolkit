package cos.mos.utils.net.okhttp;

import okhttp3.Request;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 11:44
 * @Email: KosmoSakura@gmail.com
 */
public interface HttpListener<T> {
    void failure(Request request, Exception e, String describe, int code);

    void success(T response);
}
