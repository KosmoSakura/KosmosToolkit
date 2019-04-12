package cos.mos.utils.utils;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import cos.mos.utils.init.Constant;
import cos.mos.utils.mvp.KRequest;
import cos.mos.utils.retrofit.file.FileWrapper;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Description: 下载+壁纸
 * @Author: Kosmos
 * @Date: 2018.11.16 15:08
 * @Email: KosmoSakura@gmail.com
 * 1.基于Retrofit2图片下载
 * 2.2种方式设置壁纸
 * 3.glide4.8获取bmp
 * @eg 最新修改日期：2019-1-7-RxJava线程管理
 */
public class UImage {
    public static final int FLAG_DOWNLOAD = 1;//下载
    public static final int FLAG_WALL_SYSTEM = 2;//调用系统设置壁纸
    public static final int FLAG_WALL_DIRECT = 3;//直接设置壁纸
    private RxPermissions permissions;//权限
    private Bitmap bmp;//位图
    private FragmentActivity activity;
    private static CompositeDisposable disposable;
    private String imageLink;//图片地址
    private ImageListener listener;
    private int flag;//功能区分
    private Handler handler = new Handler(message -> {
        if (message.what == 1995) {
            if (flag == FLAG_WALL_SYSTEM) {
                setWallPaperSystem();//调用系统设置壁纸
            } else if (flag == FLAG_WALL_DIRECT) {
                setWallPaperDirect();//直接设置壁纸
            }
        }
        return false;
    });

    public static UImage with(FragmentActivity activity) {
        return new UImage(activity);
    }

    private UImage(FragmentActivity activity) {
        this.activity = activity;
    }

    /**
     * 1-下载
     * 2-调用系统设置壁纸
     * 3-直接设置壁纸
     */
    public void factory(int flag, String url, ImageListener listsener) {
        this.listener = listsener;
        this.flag = flag;
        imageLink = url;
        if (permissions == null) {
            permissions = new RxPermissions(activity);
        }
        if (permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            toNext();
        } else {
            Disposable subscribe = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        toNext();
                    }
                });
            rxDisposable(subscribe);
        }
    }

    /**
     * 根据flag分发
     */
    private void toNext() {
        if (handler.hasMessages(747)) {
            handler.removeMessages(747);
        }
        handler.sendEmptyMessageDelayed(747, 1000);
        if (flag == FLAG_DOWNLOAD) {
            downloadImage();//下载
        } else {
            getBMP();//通过glide获取bmp
        }
    }

    /**
     * 通过glide获取bmp
     */
    private void getBMP() {
        new Thread(() -> {
            FutureTarget<Bitmap> futureTarget =
                Glide.with(activity)
                    .asBitmap()
                    .load(imageLink)
                    .submit();//.submit(600, 800)输出600*800的bmp
            try {
                bmp = futureTarget.get();//得到bmp
                handler.sendEmptyMessage(1995);
            } catch (ExecutionException e) {
                listener.error(flag, "Image source error");
            } catch (InterruptedException e) {
                listener.error(flag, "Image source error");
            }
        }).start();
    }

    /**
     * 下载到SD卡
     */
    private void downloadImage() {
        FileWrapper.getInstance(null).create(KRequest.class)
            .download(imageLink).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response == null || response.body() == null) {
                    listener.error(flag, "Image source error");
                    return;
                }
                rxDisposable(Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    if (writeResponseBodyToDisk(response.body())) {
                        emitter.onNext(true);
                    } else {
                        emitter.onNext(false);
                    }
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            listener.success(flag, "");
                        } else {
                            listener.error(flag, "Image source error");
                        }
                    }));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.error(flag, "Image source error");
            }
        });
    }

    /**
     * @param body 将流写进sd卡
     * @return 成功失败
     */
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

    /**
     * 需要SD卡权限
     * 调用系统的Intent.ACTION_ATTACH_DATA,
     * 该Intent会唤起所有的设置壁纸程序以及设置联系人头像程序,用户可以通过ChooseActivity进行选择:
     */
    private void setWallPaperSystem() {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("mimeType", "image/*");
        Uri uri = Uri.parse(MediaStore.Images.Media.
            insertImage(activity.getContentResolver(), bmp, null, null));
        intent.setData(uri);
        activity.startActivity(intent);
        handler.removeCallbacksAndMessages(null);
        listener.success(flag, "");
    }

    /**
     * 不需要SD卡权限
     * 直接设置壁纸
     */
    private void setWallPaperDirect() {
        WallpaperManager manager = WallpaperManager.getInstance(activity);
        try {
            manager.setBitmap(bmp);
            listener.success(flag, "Change wallpaper successfully");
        } catch (IOException e) {
            listener.error(flag, "Failed to change wallpaper");
        }
        handler.removeCallbacksAndMessages(null);
    }

    private void rxDisposable(Disposable d) {
        if (disposable == null) {
            disposable = new CompositeDisposable();
        }
        disposable.add(d);
    }

    public static void clear() {
        if (disposable != null) {
            disposable.clear();
        }
    }

    public interface ImageListener {
        void success(int flag, String msg);

        void error(int flag, String msg);
    }

}