package cos.mos.utils.mvp;

import cos.mos.utils.mvp.contract.KContract;
import io.reactivex.disposables.Disposable;

/**
 * @Description: presenter的基类
 * @Author: Kosmos
 * @Date: 2018.11.13 17:25
 * @Email: KosmoSakura@gmail.com
 */
public class KPresenter {
    private KContract contract;

    protected KRequest getRequest() {
        return contract.getRequest();
    }

    public KPresenter(KContract contract) {
        this.contract = contract;
    }

    protected void rxDisposable(Disposable disposable) {
        contract.rxMvpDisposable(disposable);
    }
}
