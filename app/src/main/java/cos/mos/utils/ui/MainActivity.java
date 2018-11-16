package cos.mos.utils.ui;

import android.util.DisplayMetrics;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
        spv.setListener(new KOnFreshListener() {
            @Override
            public void onRefresh() {
                presenter.getImageList(999);
            }
        });
        adapterImage.setOnItemClickListener((adapter, view, position) -> {

        });
        spv.callFreshDelay();
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
