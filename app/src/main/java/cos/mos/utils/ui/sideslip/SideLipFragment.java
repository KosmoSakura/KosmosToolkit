package cos.mos.utils.ui.sideslip;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;

import cos.mos.toolkit.ULogBj;
import cos.mos.toolkit.init.KFragment;
import cos.mos.toolkit.listener.KOnFreshListener;
import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;
import cos.mos.utils.widget.list.KDividerDecoration;
import cos.mos.utils.widget.list.SideBean;
import cos.mos.utils.widget.list.adapter.SideLipAdapter;

/**
 * @Description: 侧滑删除示例
 * @Author: Kosmos
 * @Date: 2018.11.20 15:43
 * @Email: KosmoSakura@gmail.com
 */
public class SideLipFragment extends KFragment {
    private SpringView spv;
    private RecyclerView rv;
    private SideLipAdapter adapter;
    private ArrayList<SideBean> list;

    @Override
    protected int layout() {
        return R.layout.frag_side_lip;
    }

    @Override
    protected void init() {
        spv = findViewById(R.id.qr_hty_spv);
        rv = findViewById(R.id.qr_hty_rv);

        spv.setHeader(new DefaultHeader(context));
        list = new ArrayList<>();
        adapter = new SideLipAdapter(list);
        adapter.openLoadAnimation();
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter.setEmptyView(R.layout.lay_empty, rv);
        rv.addItemDecoration(new KDividerDecoration(ContextCompat.getColor(context, R.color.fun_bg_white),
            (int) UScreen.dp2px(10), (int) UScreen.dp2px(10), (int) UScreen.dp2px(10)));
        rv.setAdapter(adapter);
    }

    @Override
    protected void logic() {
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (position < 0 || position > list.size()) {
                return;
            }
            SideBean bean = list.get(position);
            switch (view.getId()) {
                case R.id.item_qr_right:
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        });
        spv.setListener(new KOnFreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                spv.onFinishFreshAndLoadDelay();
            }
        });
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        ULogBj.commonD("onResume");
    }

    private void refresh() {
        list.clear();
        //GreenDao依赖包在注释里
//        List<SideBean> userBeans = DbHelper.SearchAll();
//        if (!KtText.isEmpty(userBeans)) {
//            ULogBj.commonD("-->" + userBeans.size());
//            list.addAll(userBeans);
//        }
        for (int i = 0; i < 10; i++) {
            list.add(new SideBean());
        }
        adapter.notifyDataSetChanged();
    }
}
