package cos.mos.utils.ui.permission.adapter;

import cos.mos.utils.ui.permission.bean.Permission;
import cos.mos.utils.ui.permission.callbcak.CheckRequestPermissionsListener;


/**
 * @author cd5160866
 */
public abstract class SimplePermissionsAdapter implements CheckRequestPermissionsListener {
    @Override
    public void onAllPermissionOk(Permission[] allPermissions) {

    }

    @Override
    public void onPermissionDenied(Permission[] refusedPermissions) {

    }
}
