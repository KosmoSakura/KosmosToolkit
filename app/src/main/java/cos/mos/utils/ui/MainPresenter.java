package cos.mos.utils.ui;

import android.Manifest;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import cos.mos.toolkit.ULogBj;
import cos.mos.toolkit.java.UText;
import cos.mos.utils.net.retrofit.file.FileProgressCallback;
import cos.mos.utils.net.retrofit.file.FileWrapper;
import cos.mos.utils.widget.ProgressDialog;
import cos.mos.utils.net.retrofit.UHttp;
import cos.mos.utils.mvp.KPresenter;
import cos.mos.utils.mvp.KRequest;
import cos.mos.utils.mvp.bean.ImageBean;
import cos.mos.utils.mvp.contract.KContract;
import cos.mos.utils.mvp.contract.KListMsgListener;
import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 21:04
 * @Email: KosmoSakura@gmail.com
 */
class MainPresenter extends KPresenter {
    private MainListener listener;
    private RxPermissions permissions;

    MainPresenter(KContract contract, MainListener listener) {
        super(contract);
        this.listener = listener;
    }

    void getImageList(int id) {
        ProgressDialog.instance().startProgressDialog(listener.getActivity());
        UHttp.start(getRequest().getImageList(id), "图片列表", new KListMsgListener<ImageBean>() {
            @Override
            public void onSuccess(List<ImageBean> list) {
                ProgressDialog.instance().stopProgressDialog();
                if (UText.isEmpty(list)) {
                    listener.onError("There is no data");
                } else {
                    listener.success(list);
                }
            }

            @Override
            public void onSubscribe(Disposable disposable) {
                rxDisposable(disposable);
            }

            @Override
            public void onError(String describe) {
                ProgressDialog.instance().stopProgressDialog();
                listener.onError(describe);
            }
        });
    }

    /**
     * 校验sd卡权限
     */
    void download() {
        if (permissions == null) {
            permissions = new RxPermissions(listener.getActivity());
        }
        if (permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            toDownload();
        } else {
            Disposable subscribe = permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        toDownload();
                    }
                });
            rxDisposable(subscribe);
        }
    }

    private void toDownload() {
        ProgressDialog.instance().startProgressDialog(listener.getActivity());
        KRequest rs = FileWrapper.getInstance(new FileProgressCallback() {
            @Override
            public void onLoading(long total, long progress) {
                ULogBj.commonD("All:" + total + ",Progress:" + progress);
            }
        }).create(KRequest.class);
//        UHttp.download(rs.download("url"), new KDownloadListener() {
//            @Override
//            public void onSuccess() {
//                UProgress.getInstance().stopProgressDialog();
//            }
//
//            @Override
//            public void onError(String describe) {
//                UProgress.getInstance().stopProgressDialog();
//            }
//        });
    }
}
