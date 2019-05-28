package cos.mos.utils.ui.permission.bean;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import cos.mos.utils.ui.permission.soul.SoulPermission;
import cos.mos.utils.ui.permission.debug.PermissionDebug;

/**
 * @author cd5160866
 */
public class Permission {

    private static final String TAG = Permission.class.getSimpleName();

    private static final int FLAG_IS_GRANTED = 1;

    private static final int FLAG_SHOULD_RATIONALE = 1 << 1;

    private int mFlags = 0;
    /**
     * 权限名称
     */
    public String permissionName;

    /**
     * 根据名称构建一个默认权限对象，默认失败
     *
     * @param permissionName 权限名称
     */
    public static Permission getDefault(@NonNull String permissionName) {
        return new Permission(permissionName, PackageManager.PERMISSION_DENIED, false);
    }

    /**
     * 根据名称构建一个默认成功的权限对象
     *
     * @param permissionName 权限名称
     */
    public static Permission getDefaultSuccess(@NonNull String permissionName) {
        return new Permission(permissionName, PackageManager.PERMISSION_GRANTED, false);
    }

    public Permission(String permissionName, int isGranted, boolean shouldRationale) {
        this.permissionName = permissionName;
        if (isGranted == PackageManager.PERMISSION_GRANTED) {
            mFlags |= FLAG_IS_GRANTED;
        }
        if (shouldRationale) {
            mFlags |= FLAG_SHOULD_RATIONALE;
        }
    }

    /**
     * @return 是否已经授予
     */
    public boolean isGranted() {
        return (mFlags & FLAG_IS_GRANTED) != 0;
    }

    /**
     * @return 是否需要给用户一个解释
     */
    public boolean shouldRationale() {
        return (mFlags & FLAG_SHOULD_RATIONALE) != 0;
    }

    @Override
    public String toString() {
        return permissionName + " isGranted: " + isGranted() + " shouldRationale " + shouldRationale();
    }

    /**
     * 获取权限名称描述
     *
     * @return desc of cos.mos.utils.ui.permission
     */
    public String getPermissionNameDesc() {
        Context context = SoulPermission.getInstance().getContext();
        if (null == context) {
            PermissionDebug.e(TAG, "soul cos.mos.utils.ui.permission do not inited");
            return "";
        }
        String desc;
        switch (permissionName) {
            case Manifest.permission_group.CAMERA:
            case Manifest.permission.CAMERA:
                desc = "相机权限";
                break;
            case Manifest.permission_group.CONTACTS:
            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.WRITE_CONTACTS:
            case Manifest.permission.GET_ACCOUNTS:
                desc = "通讯录权限";
                break;
            case Manifest.permission_group.PHONE:
            case Manifest.permission.CALL_PHONE:
                desc = "电话权限";
                break;
            case Manifest.permission_group.STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                desc = "存储权限";
                break;
            case Manifest.permission_group.LOCATION:
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                desc = "定位权限";
                break;
            case Manifest.permission.READ_PHONE_STATE:
                desc = "获取手机状态权限";
                break;
            default:
                desc = "未知权限";
                break;
        }
        return desc;
    }
}
