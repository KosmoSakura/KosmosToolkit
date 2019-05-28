package cos.mos.utils.ui.permission.callbcak;

import android.app.Activity;

/**
 * @author cd5160866
 */
public interface CheckStatusCallBack {

    /**
     * 状态OK
     *
     * @param activity 状态可用的回调
     */
    void onStatusOk(Activity activity);

    /**
     * 状态错误
     */
    void onStatusError();
}
