package cos.mos.utils.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import cos.mos.toolkit.system.UPermissions;
import cos.mos.toolkit.ui.UDialog;
import cos.mos.utils.R;
import cos.mos.utils.mvp.bean.ImageBean;
import cos.mos.utils.mvp.contract.KOnFreshListener;
import cos.mos.utils.widget.list.AdapterImage;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void toNext() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void logic() {
        new UPermissions(this)
            .check("我要权限，给我权限", new UPermissions.Listener() {

                @Override
                public void permission(boolean hasPermission) {
                    spv.callFreshDelay();
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        spv.setListener(new KOnFreshListener() {
            @Override
            public void onRefresh() {
                presenter.getImageList(999);
            }
        });
        adapterImage.setOnItemClickListener((adapter, view, position) -> {

        });
    }

}
