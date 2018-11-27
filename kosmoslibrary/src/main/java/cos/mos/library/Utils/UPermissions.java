package cos.mos.library.Utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;
import cos.mos.library.Utils.UDialog;
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

    public void check(String notice, Listener listener, String... pers) {
        for (String per : pers) {
            if (!rxPermissions.isGranted(per)) {
                count++;
                UDialog.getInstance(activity, false, false)
                    .showNoticeWithOnebtn(notice, "Agreed", (result, dia) -> {
                        Disposable subscribe = rxPermissions.request(pers)
                            .subscribe(granted -> {
                                if (!granted) {
                                    if (count > 2) {
                                        toGoSystem(notice);
                                    }
                                }
                                check(notice, listener, pers);
                            });
                        dia.dismiss();
                        rxDisposable(subscribe);
                    });
                return;
            }
        }
        if (listener != null) {
            listener.permission();
        }
    }

    private void toGoSystem(String notice) {
        UDialog.getInstance(activity, false, false)
            .showNoticeWithOnebtn(notice,
                "To authorize", (result, dia) -> {
                    Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        packageURI);
                    activity.startActivity(intent);
                    activity.finish();
                });
    }

    private void rxDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public interface Listener {
        void permission();
    }
}
