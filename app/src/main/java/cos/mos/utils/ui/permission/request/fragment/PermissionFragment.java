package cos.mos.utils.ui.permission.request.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import cos.mos.utils.ui.permission.soul.Constants;
import cos.mos.utils.ui.permission.soul.PermissionTools;
import cos.mos.utils.ui.permission.bean.Permission;
import cos.mos.utils.ui.permission.bean.Special;
import cos.mos.utils.ui.permission.callbcak.RequestPermissionListener;
import cos.mos.utils.ui.permission.callbcak.SpecialPermissionListener;
import cos.mos.utils.ui.permission.checker.SpecialChecker;
import cos.mos.utils.ui.permission.debug.PermissionDebug;
import cos.mos.utils.ui.permission.request.IPermissionActions;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class PermissionFragment extends Fragment implements IPermissionActions {

    private static final String TAG = PermissionFragment.class.getSimpleName();

    private String[] permissions;

    private Special specialToRequest;

    private RequestPermissionListener runtimeListener;

    private SpecialPermissionListener specialListener;

    private boolean isRequestRunTimePermission = false;

    private boolean isContainerAlready = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(M)
    @Override
    public void requestPermissions(String[] permissions, RequestPermissionListener listener) {
        this.runtimeListener = listener;
        this.permissions = permissions;
        this.isRequestRunTimePermission = true;
        if (!isContainerAlready) {
            return;
        }
        requestPermissions(permissions, Constants.REQUEST_CODE_PERMISSION);
    }

    @Override
    public void requestSpecialPermission(Special permission, SpecialPermissionListener listener) {
        this.specialListener = listener;
        this.specialToRequest = permission;
        this.isRequestRunTimePermission = false;
        if (!isContainerAlready) {
            return;
        }
        requestPermissionSpecial();
    }

    @SuppressLint("NewApi")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isContainerAlready = true;
        //request Runtime
        if (isRequestRunTimePermission) {
            requestPermissions(permissions, Constants.REQUEST_CODE_PERMISSION);
            return;
        }
        //request special
        requestPermissionSpecial();
    }

    @TargetApi(M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission[] permissionResults = new Permission[permissions.length];
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < permissions.length; ++i) {
                Permission permission = new Permission(permissions[i], grantResults[i], this.shouldShowRequestPermissionRationale(permissions[i]));
                permissionResults[i] = permission;
            }
        }
        if (runtimeListener != null && PermissionTools.isActivityAvailable(getActivity())) {
            runtimeListener.onPermissionResult(permissionResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = getActivity();
        if (specialListener == null || !PermissionTools.isActivityAvailable(activity)) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_SPECIAL) {
            boolean result = new SpecialChecker(activity, specialToRequest).check();
            if (result) {
                specialListener.onGranted(specialToRequest);
            } else {
                specialListener.onDenied(specialToRequest);
            }
        }
    }

    private void requestPermissionSpecial() {
        Intent intent = PermissionTools.getSpecialPermissionIntent(getActivity(), specialToRequest);
        if (null == intent) {
            PermissionDebug.w(TAG, "create intent failed");
            return;
        }
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_PERMISSION_SPECIAL);
        } catch (Exception e) {
            e.printStackTrace();
            PermissionDebug.e(TAG, e.toString());
        }
    }

}
