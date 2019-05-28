package cos.mos.utils.ui.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import cos.mos.utils.ui.permission.bean.Permission;
import cos.mos.utils.ui.permission.callbcak.CheckRequestPermissionListener;
import cos.mos.utils.ui.permission.soul.SoulPermission;

/**
 * @Author: 椰子罐头
 * @link github:https://github.com/soulqw/SoulPermission
 * @link 掘金：https://juejin.im/post/5cc184cee51d453f151c7fa6
 * 优势：
 * 1.解耦Activity和Fragment、不再需要Context、不再需要onPermissionResult
 * 2.内部涵盖版本判断，一行代码解决权限相关操作，无需在调用业务方写权限适配代码，继而实现真正调用时请求的“真运行时权限”
 * 3.接入成本低，零入侵，仅需要在gradle配置一行代码
 */
public class SoulSample {
    /**
     * 拨打指定电话
     */
    private void makeCall(Context context, String phoneNumber) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
            new CheckRequestPermissionListener() {
                @Override
                public void onPermissionOk(Permission permission) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent.setData(data);
                    if (!(context instanceof Activity)) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                }

                @Override
                public void onPermissionDenied(Permission permission) {
                    //SoulPermission提供栈顶Activity
                    Activity activity = SoulPermission.getInstance().getTopActivity();
                    if (null == activity) {
                        return;
                    }
                    //用户第一次拒绝了权限且没有勾选"不再提示"的情况下这个值为true，此时告诉用户为什么需要这个权限。
                    if (permission.shouldRationale()) {
                        new AlertDialog.Builder(activity)
                            .setTitle("提示")
                            .setMessage("如果你拒绝了权限，你将无法拨打电话，请点击授予权限")
                            .setPositiveButton("授予", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    makeCall(activity, phoneNumber);//重新执行请求原始流程
                                    SoulPermission.getInstance().goPermissionSettings();
                                }
                            }).create().show();
                    } else {
                        Toast.makeText(context, "本次拨打电话授权失败,请手动去设置页打开权限，或者重试授权权限", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
