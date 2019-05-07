package cos.mos.utils.net.okhttp;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 11:44
 * @Email: KosmoSakura@gmail.com
 */
public interface HttpListener<T> {
    void failure(String describe, int code);

    void success(T response);
}
