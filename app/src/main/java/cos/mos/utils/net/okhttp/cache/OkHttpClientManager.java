package cos.mos.utils.net.okhttp.cache;

/**
 * Created by cl on 2016/8/11.
 */

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Cache cache;

    private static final String TAG = "OkHttpClientManager";

    //FrameWorkSetting.SAVE_SD_FLODER
    private OkHttpClientManager() {
        cache = new Cache(new File(Config.SAVE_SD_FLODER), 10240 * 1024);
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.cookieJar();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private void builds(boolean addSSL) {
        if (addSSL) {
            OkHttpsUtils.SSLParams sslParams = OkHttpsUtils.getSslSocketFactory(null, "dengdeng");
            mOkHttpClient = mOkHttpClient.newBuilder()
                .cache(cache)//缓存路径
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .cookieJar(CookiesManager.getInstance())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addNetworkInterceptor(cacheIntercepter)//自定义的网络拦截器
                .build();
        } else {
            mOkHttpClient = mOkHttpClient.newBuilder()
                .cache(cache)//缓存路径
//                .cookieJar(CookiesManager.getInstance())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addNetworkInterceptor(cacheIntercepter)//自定义的网络拦截器
                .build();
        }
        OkHttpUtils.initClient(mOkHttpClient);
    }

    public static OkHttpClientManager getInstance(boolean addSSL) {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        mInstance.builds(addSSL);
        return mInstance;
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
            .url(url)
            .addHeader("Identifier", Config.IMEID_MD5)
            .addHeader("TerminalCode", Config.TerminalCode)
            .addHeader("TerminalVersion", Config.TerminalVersion)
            .addHeader("Appid", Config.Appid)
            .addHeader("AppSecret", Config.AppSecret)
            .build();
        deliveryResult(callback, request);
    }


    /**
     * 异步的post请求
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     */
    private void _postAsyn(String url, final ResultCallback callback, String params) {
        Request request = buildPostStringRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    private void _postFile(String url, final FileResultCallback callback, File[] file, String form) throws IOException {
        final Request request = buildMultipartFormRequestFile(url, file, form);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null)
                    callback.onError(request, e, -1);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    if (response.code() == 200) {
                        try {
                            callback.onResponse(request, response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        callback.onError(request, new Exception(), response.code());
                    }
                }
            }
        });
    }

    private void _postFile(String url, final FileResultCallback callback, File[] file, String form, String uuid) throws IOException {
        final Request request = buildMultipartFormRequestFile(url, file, form, uuid);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callback != null)
                    callback.onError(request, e, -1);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback != null) {
                    if (response.code() == 200) {
                        try {
                            callback.onResponse(request, response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        callback.onError(request, new Exception(), response.code());
                    }
                }
            }
        });
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final File destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder()
            .url(url)
            .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, -1, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileSuffix(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if (callback.mType == String.class) {//如果下载文件成功，第一个参数为文件的绝对路径
                        sendSuccessResultCallback(request, file.getAbsolutePath(), callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, response.code(), callback);
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? System.currentTimeMillis() + "" : path.substring(separatorIndex + 1, path.length());
    }

    private String getFileSuffix(String path) {
        int separatorIndex = path.lastIndexOf(".");
        return (separatorIndex < 0) ? "ratosoft" + System.currentTimeMillis() + ".jpg" : "ratosoft" + System.currentTimeMillis() + path.substring(separatorIndex, path.length());
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    //*************对外公布的方法************

    /**
     * Get请求
     */
    public static void getAsyn(boolean addSSL, String url, ResultCallback callback) {
        getInstance(addSSL)._getAsyn(url, callback);
    }

    /**
     * 参数类型：Json
     */
    public static void postAsyn(boolean addSSL, String url, String params, final ResultCallback callback) {
        getInstance(addSSL)._postAsyn(url, callback, params);
    }

    /**
     * 参数类型：数组
     */
    public static void postAsyn(boolean addSSL, String url, final ResultCallback callback, Param... params) {
        getInstance(addSSL)._postAsyn(url, callback, params);
    }

    /**
     * 参数类型：Map
     */
    public static void postAsyn(boolean addSSL, String url, final ResultCallback callback, Map<String, String> params) {
        getInstance(addSSL)._postAsyn(url, callback, params);
    }

    /**
     * 参数类型：键值对
     */
    public static void postAsyn(boolean addSSL, String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance(addSSL)._postAsyn(url, callback, files, fileKeys, params);
    }

    /**
     * 参数类型：文件
     */
    public static void postAsyn(boolean addSSL, String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance(addSSL)._postAsyn(url, callback, file, fileKey);
    }

    /**
     * 参数类型：文件+数组
     */
    public static void postAsyn(boolean addSSL, String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance(addSSL)._postAsyn(url, callback, file, fileKey, params);
    }

    /**
     * 参数类型：文件
     */
    public static void postFile(boolean addSSL, String url, File[] file, String form, FileResultCallback callback) throws IOException {
        getInstance(addSSL)._postFile(url, callback, file, form);
    }

    public static void postFile(boolean addSSL, String url, File[] file, String form, String uuid, FileResultCallback callback) throws IOException {
        getInstance(addSSL)._postFile(url, callback, file, form, uuid);
    }

    /**
     * 下载
     */
    public static void downloadAsyn(boolean addSSL, String url, File destDir, ResultCallback callback) {
        getInstance(addSSL)._downloadAsyn(url, destDir, callback);
    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);// multipart/form-data

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                    fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
    }

    private Request buildMultipartFormRequestFile(String url, File[] file, String form) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("allowSuffix", "jpg,png,bmp,jpeg,zip,rar,doc,mp4")
            .addFormDataPart("module", form);
        for (File f : file) {
            String path = f.getAbsolutePath();
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"files\";filename=\"" + path + "\""),
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), f));
        }
        RequestBody requestBody = builder.build();

        return new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
    }

    private Request buildMultipartFormRequestFile(String url, File[] file, String form, String uuid) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("allowSuffix", "jpg,png,bmp,jpeg,zip,rar,doc,mp4")
                .addFormDataPart("module", form)
                .addFormDataPart("info", "19")//临时使用
                .addFormDataPart("uuid", uuid);
//            .addFormDataPart("moduleTable", form);
//            .addFormDataPart("allowSuffix", "jpg,png,bmp,jpeg")
//            .addFormDataPart("allowMaxFileSize", "3096")
//            .addFormDataPart("moduleId", BaseApplication.sp.getInt("id", -1) + "")
//            .addFormDataPart("isUEditor", "false")
//            .addFormDataPart("moduleType", "0");
        for (File f : file) {
            String path = f.getAbsolutePath();
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"files\";filename=\"" + path + "\""),
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), f));
        }
        RequestBody requestBody = builder.build();

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, -1, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        final String string = response.body().string();
                        if (callback.mType == String.class) {
                            sendSuccessResultCallback(request, string, callback);
                        } else {
                            Object o = new Gson().fromJson(string, callback.mType);
                            sendSuccessResultCallback(request, o, callback);
                        }
                    } catch (IOException e) {
                        sendFailedStringCallback(response.request(), e, response.code(), callback);
                    } catch (com.google.gson.JsonParseException e) {//Json解析的错误
                        sendFailedStringCallback(response.request(), e, response.code(), callback);
                    }
                } else {
                    sendFailedStringCallback(response.request(), new IOException(), response.code(), callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final int code, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e, code);
            }
        });
    }

    private void sendSuccessResultCallback(final Request request, final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(request, object);
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
    }

    private Request buildPostStringRequest(String url, String params) {
        if (params == null) {
            params = "";
        }
        return new Request.Builder()
            .url(url)
            .addHeader("Identifier", Config.IMEID_MD5)
            .addHeader("TerminalCode", Config.TerminalCode)
            .addHeader("TerminalVersion", Config.TerminalVersion)
            .addHeader("Appid", Config.Appid)
            .addHeader("AppSecret", Config.AppSecret)
            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params))
            .build();
    }


    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e, int code);

        public abstract void onResponse(Request request, T response);
    }

    public static abstract class FileResultCallback<T> {
        public abstract void onError(Request request, Exception e, int code);

        public abstract void onResponse(Request request, String response);
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    //网络拦截器：设置缓存策略
    private Interceptor cacheIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //无网的时候强制使用缓存
            if (!NetworkUtil.isConnected()) {
                ToastBase.ShortMessage("网络不可用,开始加载本地缓存");
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            }

            Response response = chain.proceed(request);

            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            if (NetworkUtil.isConnected()) {
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
    //应用拦截器：主要用于设置公共参数，头信息，日志拦截等,有点类似Retrofit的Converter
    private Interceptor appIntercepter = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = processRequest(chain.request());
            Response response = processResponse(chain.proceed(request));
            return response;
        }

        //访问网络之前，处理Request(这里统一添加了Cookie)
        private Request processRequest(Request request) {
            return request
                .newBuilder()
                .addHeader("TerminalCode", "android_app")
                .addHeader("TerminalVersion", "v" + Config.clientVersion)
                .addHeader("TerminalSim", Config.phoneMODEL)
                .build();
        }

        //访问网络之后，处理Response(这里没有做特别处理)
        private Response processResponse(Response response) {
            return response;
        }
    };
}
