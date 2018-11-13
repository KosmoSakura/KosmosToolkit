package cos.mos.library.retrofit;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cos.mos.library.Utils.ULog;
import cos.mos.library.constant.KConfig;
import cos.mos.library.retrofit.connector.HttpMsgListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Description: 网络错误统一处理
 * @Author: Kosmos 接口 connector
 * @Date: 2018年10月17日 21:31
 * @Email: KosmoSakura@gmail.com
 * @apiNote 还不完善。查漏补缺
 */
public class UHttp {
    public static <T> void start(Observable<T> observable, String describe, HttpMsgListener<T> listener) {
        KConfig.Describe = describe;
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<T>() {
                @Override
                public void onSubscribe(Disposable d) {
                    listener.onSubscribe(d);
                }

                @Override
                public void onNext(T dto) {
                    if (dto == null) {
                        listener.onError("网络异常，请稍后再试");
                    } else {
                        listener.onSuccess(dto);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError("数据解析异常，请稍后再试");//这里描述还需要确认，或者细分
                    listener.onFinish();
                    ULog.commonE(describe + "-->" + e.toString());
                }

                @Override
                public void onComplete() {
                    listener.onFinish();
                }
            });
    }

    public void downloadLatestFeature(Call<ResponseBody> resultCall, String describe, HttpMsgListener<String> listener) {
        KConfig.Describe = describe;
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null || response.body() == null) {
                    listener.onError("Image source error");
                    listener.onFinish();
                    return;
                }
                boolean result = writeResponseBodyToDisk(response.body());
                if (result) {
                    listener.onSuccess("");
                } else {
                    listener.onError("Image source error");
                }
                listener.onFinish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError("Image source error");
                listener.onFinish();
            }
        });
    }

    public static void download(Observable<ResponseBody> observable, String describe, HttpMsgListener<ResponseBody> listener) {
        KConfig.Describe = describe;
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {
                    listener.onSubscribe(d);
                }

                @Override
                public void onNext(ResponseBody response) {
                    if (response == null) {
                        listener.onError("网络异常，请稍后再试");
                    } else {
                        listener.onSuccess(response);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError("数据解析异常，请稍后再试");
                    listener.onFinish();
                    ULog.commonE(describe + "-->" + e.toString());
                }

                @Override
                public void onComplete() {
                    listener.onFinish();
                }
            });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        // todo change the file location/name according to your needs
        File futureStudioIconFile = new File("" + File.separator + "Future Studio Icon.png");
        long fileSize = body.contentLength();
        byte[] fileReader = new byte[4096];
        try (InputStream inputStream = body.byteStream();
             OutputStream outputStream = new FileOutputStream(futureStudioIconFile)) {
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
