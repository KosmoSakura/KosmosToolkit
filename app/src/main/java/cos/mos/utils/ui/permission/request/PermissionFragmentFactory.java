package cos.mos.utils.ui.permission.request;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import cos.mos.utils.ui.permission.request.fragment.PermissionFragment;
import cos.mos.utils.ui.permission.request.fragment.PermissionSupportFragment;


/**
 * @author cd5160866
 */
class PermissionFragmentFactory {

    private static final String FRAGMENT_TAG = "permission_fragment_tag";

    static IPermissionActions create(Activity activity) {
        IPermissionActions action;
        if (activity instanceof FragmentActivity) {
            FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            PermissionSupportFragment permissionSupportFragment = (PermissionSupportFragment) supportFragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (null == permissionSupportFragment) {
                permissionSupportFragment = new PermissionSupportFragment();
                supportFragmentManager.beginTransaction()
                        .add(permissionSupportFragment, FRAGMENT_TAG)
                        .commitNowAllowingStateLoss();
            }
            action = permissionSupportFragment;
        } else {
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            PermissionFragment permissionFragment = (PermissionFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
            if (null == permissionFragment) {
                permissionFragment = new PermissionFragment();
                activity.getFragmentManager().beginTransaction()
                        .add(permissionFragment, FRAGMENT_TAG)
                        .commitAllowingStateLoss();
            }
            action = permissionFragment;
        }
        return action;
    }
}
