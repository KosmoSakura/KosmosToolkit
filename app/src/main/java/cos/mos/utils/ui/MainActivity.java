package cos.mos.utils.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cos.mos.library.utils.UDialog;
import cos.mos.library.utils.UPermissions;
import cos.mos.utils.R;
import cos.mos.utils.init.BaseActivity;
import cos.mos.utils.mvp.adapter.AdapterImage;
import cos.mos.utils.mvp.bean.ImageBean;
import cos.mos.utils.mvp.contract.KOnFreshListener;

public class MainActivity extends BaseActivity implements MainListener {
    private SpringView spv;
    private RecyclerView rv;
    private MainPresenter presenter;
    private AdapterImage adapterImage;
    private ArrayList<ImageBean> listImage;
的次数

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        spv = findViewById(R.id.main_sv);
        rv = findViewById(R.id.main_rv);

        presenter = new MainPresenter(this, this);
        listImage = new ArrayList<>();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        adapterImage = new AdapterImage(listImage, dm.widthPixels / 3);
        adapterImage.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(adapterImage);
        spv.setHeader(new DefaultHeader(context));
        spv.setEnableFooter(false);
    }

    @Override
    protected void logic() {
        new UPermissions(this).check("我要权限，给我权限", new UPermissions.Listener() {
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


    private void toGoSystem() {
        UDialog.getInstance(this, false, false)
            .showNoticeWithOnebtn("We need the following permissions to make the program run properly",
                "To authorize", (result, dia) -> {
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        packageURI);
                    startActivity(intent);
                    finish();
                });
    }

    @Override
    public void success(List<ImageBean> list) {
        spv.onFinishFreshAndLoadDelay();
        listImage.clear();
        listImage.addAll(list);
        adapterImage.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        spv.onFinishFreshAndLoadDelay();
    }

    @Override
    public FragmentActivity getActivity() {
        return this;
    }
}
