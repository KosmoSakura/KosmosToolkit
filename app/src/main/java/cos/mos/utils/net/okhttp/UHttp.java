package cos.mos.utils.net.okhttp;

import android.os.Handler;
import android.os.Looper;

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
 * 三星等手机UnknownServiceException网络安全错误解决：
 * AndroidManifest.xml->Application ->添加属性
 * android:networkSecurityConfig="@xml/security_config"
 */
public class UHttp {
    private static UHttp http;
    private OkHttpClient client;
    private Class cls;//泛型
    private final Handler delivery = new Handler(Looper.getMainLooper());
    private HttpListener listener;
    private String describe;
    private int code;
    private Object response;

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
        this.listener = listener;
        if (!UNet.instance().isNetConnected()) {
            failure("网络不可用", NetState.NetworkDislink);
            return;
        }
        connection(new Request.Builder()
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
        this.listener = listener;
        if (!UNet.instance().isNetConnected()) {
            failure("网络不可用", NetState.NetworkDislink);
            return;
        }
        connection(new Request.Builder()
            .url(url)
            .addHeader("key", "value")
            .build());
    }

    /**
     * @apiNote 执行请求
     */
    private void connection(final Request request) {
        client.newCall(request)
            .enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    failure("请求失败", NetState.HttpError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    if (response.code() == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            try {
                                convert(body.string());
                            } catch (Exception e) {
                                failure("ResponseBody为空", 200);
                            }
                        } else {
                            failure("ResponseBody为空", 200);
                        }
                    } else {
                        failure("Code不等于200", response.code());
                    }
                }
            });
    }

    private void convert(final String body) throws JSONException {
        final JSONObject root = new JSONObject(body);
        //外围字段
        final int code = root.getInt("Code");
        final String msg = root.getString("msg");
        final String json = root.getString("data");
        //服务器成功返回码
        if (code == 0) {
            try {
                final ArrayList arrayList = UGson.toParseList(json, cls);
                success(arrayList);
            } catch (Exception e) {
                try {
                    Object object = UGson.toParseObj(json, cls);
                    success(object);
                } catch (Exception e1) {
                    failure("解析失败", code);
                }
            }
        } else {
            failure(msg, code);
        }
    }

    private void failure(final String describe, final int code) {
        this.describe = describe;
        this.code = code;
        delivery.post(runFail);
    }

    private void success(final Object response) {
        this.response = response;
        delivery.post(runSuccess);
    }

    private final Runnable runSuccess = new Runnable() {
        @Override
        public void run() {
            listener.success(response);
        }
    };
    private final Runnable runFail = new Runnable() {
        @Override
        public void run() {
            listener.failure(describe, code);
        }
    };
}
