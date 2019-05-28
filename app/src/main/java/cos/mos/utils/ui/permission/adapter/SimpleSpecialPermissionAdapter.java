package cos.mos.utils.ui.permission.adapter;

import cos.mos.utils.ui.permission.bean.Special;
import cos.mos.utils.ui.permission.callbcak.SpecialPermissionListener;

/**
 * @author cd5160866
 */
public abstract class SimpleSpecialPermissionAdapter implements SpecialPermissionListener {

    @Override
    public void onDenied(Special permission) {

    }

    @Override
    public void onGranted(Special permission) {

    }
}
