package cos.mos.utils.retrofit;


import cos.mos.utils.constant.KConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HostWrapper {
    private static volatile HostWrapper sInstance;
    private static Retrofit mRetrofit;

    private HostWrapper() {
        //初始化okhttp
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //添加拦截器
        client.addInterceptor(new HeaderInterceptor());
        // 初始化Retrofit
        mRetrofit = new Retrofit.Builder()
            .baseUrl(KConfig.getBaseUrl())
            //json转化为对象
            .addConverterFactory(GsonConverterFactory.create())
            //引入RxJava网络请求rxjava转换
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build();
    }

    public static HostWrapper with() {
        if (sInstance == null) {
            synchronized (HostWrapper.class) {
                if (sInstance == null) {
                    sInstance = new HostWrapper();
                }
            }
        }
        return sInstance;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}