package cos.mos.utils.mvp.contract;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年10月17日 21:31
 * @Email: KosmoSakura@gmail.com
 */
public abstract class KListMsgListener<T> {
    /**
     * @apiNote 实体类永远不会为空
     */
    public abstract void onSuccess(List<T> list);

    public abstract void onSubscribe(Disposable disposable);

    /**
     * @param describe 错误描述
     * @apiNote 错误描述永远不会为空
     */
    public abstract void onError(String describe);

    /**
     * @apiNote 所有请求结束，选择实现，控制流程用(关闭进度条等）
     */
    public void onFinish() {

    }
}
