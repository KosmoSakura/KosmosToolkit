package cos.mos.utils.ui.permission.request;

import android.annotation.TargetApi;

import cos.mos.utils.ui.permission.bean.Special;
import cos.mos.utils.ui.permission.callbcak.RequestPermissionListener;
import cos.mos.utils.ui.permission.callbcak.SpecialPermissionListener;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public interface IPermissionActions {

    /**
     * 请求权限
     *
     * @param permissions 权限
     * @param listener    回调
     */
    @TargetApi(M)
    void requestPermissions(String[] permissions, RequestPermissionListener listener);


    /**
     * 请求特殊权限
     * @param permission 特殊权限
     * @param listener 回调
     */
    void requestSpecialPermission(Special permission, SpecialPermissionListener listener);

}
