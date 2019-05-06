package cos.mos.utils.init;

import cos.mos.toolkit.init.KFragment;
import cos.mos.utils.net.retrofit.HostWrapper;
import cos.mos.utils.mvp.KRequest;
import cos.mos.utils.mvp.contract.KContract;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: base
 * @Author: Kosmos
 * @Date: 2018年08月02日 15:01
 * @Email: KosmoSakura@gmail.com
 */
public abstract class BaseFragment extends KFragment implements KContract {
    private KRequest rs;

    protected KRequest getServes() {
        if (rs == null) {
            rs = HostWrapper.with().create(KRequest.class);
        }
        return rs;
    }

    @Override
    public void rxMvpDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
