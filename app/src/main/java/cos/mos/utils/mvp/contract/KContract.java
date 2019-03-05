package cos.mos.utils.mvp.contract;

import cos.mos.utils.mvp.KRequest;
import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 21:05
 * @Email: KosmoSakura@gmail.com
 */
public interface KContract {
    void rxMvpDisposable(Disposable disposable);

    KRequest getRequest();
}
