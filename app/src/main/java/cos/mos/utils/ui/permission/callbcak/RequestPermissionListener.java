package cos.mos.utils.ui.permission.callbcak;


import cos.mos.utils.ui.permission.bean.Permission;

/**
 * @author cd5160866
 */
public interface RequestPermissionListener {

    /**
     * 得到权限检查结果
     *
     * @param permissions 封装权限的数组
     */
    void onPermissionResult(Permission[] permissions);

}
