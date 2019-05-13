package cos.mos.utils.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import cos.mos.toolkit.system.UPermissions;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Handler().postDelayed({
//            //检测权限
//            checkPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }, 200)
        checkPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        wayTwo();
    }

    private void checkPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            toNext();
            return;
        }
        boolean st = true;
        for (String str : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, str)) {
                st = false;
                break;
            }
        }
        if (st) {
            toNext();
        } else {
            ActivityCompat.requestPermissions(this, permissions, 10086);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void toNext() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void wayTwo() {
        new UPermissions(this)
            .check("理由", new UPermissions.Listener() {
                @Override
                public void permission(boolean hasPermission) {
                    if (hasPermission) {
                        toNext();
                    }
                }
            }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
