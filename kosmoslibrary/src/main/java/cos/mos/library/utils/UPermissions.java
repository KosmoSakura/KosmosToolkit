package cos.mos.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.fragment.app.FragmentActivity;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: 权限工具，基于RxPermissions
 * @Author: Kosmos
 * @Date: 2018.11.27 11:03
 * @Email: KosmoSakura@gmail.com
 */
public class UPermissions {
    private RxPermissions rxPermissions;
    private static CompositeDisposable compositeDisposable;
    private FragmentActivity activity;
    private int count;
    private int maxNoticeCount = 2;//弹窗最大弹出次数

    public UPermissions(FragmentActivity activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
        count = 0;
    }

    public static void clear() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * @param max 通知最大弹出次数，默认为2次
     *            当次数为1，则响应式弹出通知
     * @apiNote 用户体验优化
     */
    public UPermissions setMaxNoticeCount(int max) {
        maxNoticeCount = max;
        return this;
    }

    public void check(String notice, Listener listener, String... pers) {
        for (String per : pers) {
            if (!rxPermissions.isGranted(per)) {
                count++;
                UDialog.getInstance(activity, false, false)
                    .showNoticeWithOnebtn(notice, "Agreed", (result, dia) -> {
                        Disposable subscribe = rxPermissions.request(pers)
                            .subscribe(granted -> {
                                if (granted) {
                                    if (listener != null) {
                                        listener.permission(true);
                                    }
                                } else {
                                    if (maxNoticeCount == 1) {
                                        if (listener != null) {
                                            listener.permission(false);
                                        }
                                    } else {
                                        if (count > maxNoticeCount) {
                                            UDialog.getInstance(activity, false, false)
                                                .showNoticeWithOnebtn(notice,
                                                    "To authorize", (result1, dia1) -> {
                                                        UIntent.goSys();
                                                        activity.finish();
                                                    });

                                        } else {
                                            check(notice, listener, pers);
                                        }
                                    }
                                }
                            });
                        dia.dismiss();
                        rxDisposable(subscribe);
                    });
                return;
            }
        }
        if (listener != null) {
            listener.permission(true);
        }
    }


    private void rxDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public interface Listener {
        void permission(boolean hasPermission);
    }
}
