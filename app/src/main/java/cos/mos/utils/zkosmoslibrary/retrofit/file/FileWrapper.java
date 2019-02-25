package cos.mos.utils.zkosmoslibrary.retrofit.file;


import cos.mos.utils.zkosmoslibrary.constant.KConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 下载
 */
public class FileWrapper {
    private static volatile FileWrapper sInstance;
    private Retrofit mRetrofit;

    public FileWrapper(final FileProgressCallback callback) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(chain -> {
            okhttp3.Response response = chain.proceed(chain.request());
            //将ResponseBody转换成我们需要的FileResponseBody
            return response.newBuilder().body(new FileResponseBody(response.body(), callback)).build();
        });
        mRetrofit = new Retrofit.Builder()
            .baseUrl(KConfig.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder.build())
            .build();
    }

    /**
     * 懒汉式
     */
    public static FileWrapper getInstance(FileProgressCallback callback) {
        if (sInstance == null) {
            synchronized (FileWrapper.class) {
                if (sInstance == null) {
                    sInstance = new FileWrapper(callback);
                }
            }
        }
        return sInstance;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}