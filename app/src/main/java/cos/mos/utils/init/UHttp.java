package cos.mos.utils.init;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cos.mos.library.utils.UText;
import cos.mos.library.constant.KConfig;
import cos.mos.utils.mvp.bean.ResultBean;
import cos.mos.utils.mvp.contract.KListMsgListener;
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
    public static <T> void start(Observable<ResultBean<T>> observable, String describe, KListMsgListener<T> listener) {
        KConfig.Describe = describe;
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ResultBean<T>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    listener.onSubscribe(d);
                }

                @Override
                public void onNext(ResultBean<T> dto) {
                    if (dto == null) {
                        listener.onError("Network exception, please try again later");
                    } else {
                        if (dto.getCode() == 0) {
                            listener.onSuccess(dto.getData());
                        } else {
                            listener.onError(UText.isNull(dto.getMsg(), "Network exception, please try again later"));
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError("Data parsing exception, please try again later");
                    listener.onFinish();
                }

                @Override
                public void onComplete() {
                    listener.onFinish();
                }
            });
    }

    public static void download(Call<ResponseBody> resultCall) {
        KConfig.Describe = "图片下载";
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null || response.body() == null) {
//                    listener.onError("Image source error");
                    return;
                }
                if (writeResponseBodyToDisk(response.body())) {
//                    listener.onSuccess();
                } else {
//                    listener.onError("Image source error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                listener.onError("Image source error");
            }
        });
    }


    private static boolean writeResponseBodyToDisks(ResponseBody body) {
        File futureStudioIconFile = new File(Constant.getDownloadPath() + System.currentTimeMillis() + ".jpg");
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

    private static boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            File fileDr = new File(Constant.getDownloadPath());
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(Constant.getDownloadPath(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
