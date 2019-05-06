package cos.mos.utils.net.okhttp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cos.mos.toolkit.java.UText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Description: 一般请求
 * @Author: Kosmos
 * @Date: 2019.05.06 10:01
 * @Email: KosmoSakura@gmail.com
 */
public class UHttp {
    private static UHttp http;
    private OkHttpClient client;
    private Class cls;//泛型

    private UHttp() {
        client = new OkHttpClient();
        //构建实例
        client = client.newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build();
    }

    public static UHttp instance(Class classType) {
        if (http == null) {
            synchronized (UHttp.class) {
                if (http == null) {
                    http = new UHttp();
                }
            }
        }
        http.cls = classType;
        return http;
    }

    /**
     * @param url      api
     * @param params   json格式参数
     * @param listener 回调
     * @apiNote 异步Post
     */
    public void postAsyn(String url, String params, final HttpListener listener) {
        connection(listener, new Request.Builder()
            .url(url)
            .addHeader("key", "value")
            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), UText.isNull(params)))
            .build());
    }

    /**
     * @param url      api
     * @param listener 回调
     * @apiNote 异步Get
     */
    public void getAsyn(String url, final HttpListener listener) {
        connection(listener, new Request.Builder()
            .url(url)
            .addHeader("key", "value")
            .build());
    }

    /**
     * @apiNote 执行请求
     */
    private void connection(final HttpListener listener, final Request request) {
        client.newCall(request)
            .enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    listener.failure(call.request(), e, "请求失败", -1);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            try {
                                convert(listener, body.string());
                            } catch (Exception e) {
                                listener.failure(call.request(), null, "ResponseBody为空", 200);
                            }
                        } else {
                            listener.failure(call.request(), null, "ResponseBody为空", 200);
                        }
                    } else {
                        listener.failure(call.request(), null, "Code不等于200", response.code());
                    }
                }
            });
    }

    private void convert(final HttpListener listener, final String body) throws JSONException {
        final JSONObject root = new JSONObject(body);
        //外围字段
        final int code = root.getInt("Code");
        final String msg = root.getString("msg");
        final String json = root.getString("data");
        //服务器成功返回码
        if (code == 0) {
            try {
                final ArrayList arrayList = UGson.toParseList(json, cls);
                listener.success(arrayList);
            } catch (Exception e) {
                try {
                    Object object = UGson.toParseObj(json, cls);
                    listener.success(object);
                } catch (Exception e1) {
                    listener.failure(null, e1, "解析失败", code);
                }
            }
        } else {
            listener.failure(null, null, msg, code);
        }
    }
}
