package cos.mos.utils.init;

import cos.mos.utils.init.k.KActivity;
import cos.mos.utils.retrofit.HostWrapper;
import cos.mos.utils.mvp.KRequest;
import cos.mos.utils.mvp.contract.KContract;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月02日 13:17
 * @Email: KosmoSakura@gmail.com
 */
public abstract class BaseActivity extends KActivity implements KContract {
    private KRequest rs;

    @Override
    public KRequest getRequest() {
        if (rs == null) {
            rs = HostWrapper.with().create(KRequest.class);
        }
        return rs;
    }

    @Override
    public void rxDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
