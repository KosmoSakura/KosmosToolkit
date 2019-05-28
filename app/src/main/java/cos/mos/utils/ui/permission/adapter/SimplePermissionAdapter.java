package cos.mos.utils.ui.permission.adapter;

import cos.mos.utils.ui.permission.bean.Permission;
import cos.mos.utils.ui.permission.callbcak.CheckRequestPermissionListener;


/**
 * @author cd5160866
 */
public abstract class SimplePermissionAdapter implements CheckRequestPermissionListener {

    @Override
    public void onPermissionOk(Permission permission) {

    }

    @Override
    public void onPermissionDenied(Permission permission) {

    }
}
