package cos.mos.utils.net.okhttp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cos.mos.toolkit.io.UFile;
import cos.mos.toolkit.java.UText;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Description: SSL请求
 * @Author: Kosmos
 * @Date: 2019.05.06 10:01
 * @Email: KosmoSakura@gmail.com
 */
public class UHttpSSL {
    private static UHttpSSL httpSSL;
    private OkHttpClient client;
    private Class cls;//泛型

    private UHttpSSL() {
        Cache cache = new Cache(new File("网络缓存路径"), 10240 * 1024);
        client = new OkHttpClient();
        client.cookieJar();
        //构建实例
        USSL.SSLParams sslParams = USSL.loadSSL(null);
        client = client.newBuilder()
            .cache(cache)//缓存路径
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .sslSocketFactory(sslParams.ssLSocketFactory, sslParams.trustManager)
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addNetworkInterceptor(cacheIntercepter)//自定义的网络拦截器
            .build();
    }

    public static UHttpSSL instance(Class classType) {
        if (httpSSL == null) {
            synchronized (UHttpSSL.class) {
                if (httpSSL == null) {
                    httpSSL = new UHttpSSL();
                }
            }
        }
        httpSSL.cls = classType;
        return httpSSL;
    }

    /**
     * @param url      api
     * @param params   json格式参数
     * @param listener 回调
     * @apiNote 异步Post
     */
    public void postAsyn(String url, String params, final HttpListener listener) {
        if (!UNet.instance().isNetConnected()) {
            listener.failure(null, null, "网络不可用", NetState.NetworkDislink);
            return;
        }
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
        if (!UNet.instance().isNetConnected()) {
            listener.failure(null, null, "网络不可用", NetState.NetworkDislink);
            return;
        }
        connection(listener, new Request.Builder()
            .url(url)
            .addHeader("key", "value")
            .build());
    }

    /**
     * @param url      请求地址
     * @param files    目标文件本地地址的file对象
     * @param form     对应的服务器表名
     * @param listener 回调
     * @apiNote 上传文件
     */
    public void fileUpload(String url, File[] files, final String form, final HttpListener listener) {
        if (!UNet.instance().isNetConnected()) {
            listener.failure(null, null, "网络不可用", NetState.NetworkDislink);
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("allowSuffix", "jpg,png,bmp,jpeg,zip,rar,doc,mp4")
            .addFormDataPart("module", form);
        for (File file : files) {
            builder.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"files\";filename=\"" + file.getAbsolutePath() + "\""),
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file));
        }
        connection(listener, new Request.Builder()
            .url(url)
            .post(builder.build())
            .build());
    }

    /**
     * @param url      下载地址
     * @param dir      下载路径
     * @param listener 回调
     * @apiNote 上传下载
     */
    public void fileDownload(String url, File dir, final HttpListener<String> listener) {
        if (!UNet.instance().isNetConnected()) {
            listener.failure(null, null, "网络不可用", NetState.NetworkDislink);
            return;
        }
        final Request request = new Request.Builder()
            .url(url)
            .build();
        client.newCall(request)
            .enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    listener.failure(call.request(), e, "请求失败", NetState.HttpError);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        InputStream is;
                        byte[] buf = new byte[2048];
                        int len;
                        FileOutputStream fos;
                        File file = new File(dir, UFile.getSuffix(url));
                        try {
                            is = body.byteStream();
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            is.close();
                            fos.close();
                            listener.success(file.getAbsolutePath());
                        } catch (Exception e) {
                            listener.failure(response.request(), e, "保存异常", response.code());
                        }
                    } else {
                        listener.failure(response.request(), null, "ResponseBody为空", response.code());
                    }
                }
            });
    }

    /**
     * @apiNote 执行请求
     */
    private void connection(final HttpListener listener, final Request request) {
        client.newCall(request)
            .enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    listener.failure(call.request(), e, "请求失败", NetState.HttpError);
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

    /**
     * 网络拦截器：设置缓存策略
     */
    private Interceptor cacheIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //无网的时使用缓存
            if (!UNet.instance().isNetConnected()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
            if (UNet.instance().isNetConnected()) {
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
//                    .addHeader("TerminalCode", "android_app")
//                    .addHeader("TerminalVersion", "v" + FrameWorkSetting.clientVersion)
//                    .addHeader("TerminalSim", FrameWorkSetting.phoneMODEL)
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return response.newBuilder()
//                    .addHeader("TerminalCode", "android_app")
//                    .addHeader("TerminalVersion", "v" + FrameWorkSetting.clientVersion)
//                    .addHeader("TerminalSim", FrameWorkSetting.phoneMODEL)
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
            }
        }
    };
}
