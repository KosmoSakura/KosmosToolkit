package cos.mos.utils.ui.sideslip;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import cos.mos.utils.R;
import cos.mos.utils.dao.DbHelper;
import cos.mos.utils.dao.UserBean;
import cos.mos.utils.init.k.KFragment;
import cos.mos.utils.utils.ULogBj;
import cos.mos.utils.utils.java.UText;
import cos.mos.utils.utils.listener.KOnFreshListener;
import cos.mos.utils.utils.system.UScreen;
import cos.mos.utils.widget.list.MyDividerDecoration;

/**
 * @Description: 侧滑删除
 * @Author: Kosmos
 * @Date: 2018.11.20 15:43
 * @Email: KosmoSakura@gmail.com
 */
public class SideLipFragment extends KFragment {
    private SpringView spv;
    private RecyclerView rv;
    private SideLipAdapter adapter;
    private ArrayList<UserBean> list;

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
        rv.addItemDecoration(new MyDividerDecoration(ContextCompat.getColor(context, R.color.fun_bg_white),
            (int) UScreen.dp2px(10), (int) UScreen.dp2px(10), (int) UScreen.dp2px(10)));
        rv.setAdapter(adapter);
    }

    @Override
    protected void logic() {
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (position < 0 || position > list.size()) {
                return;
            }
            UserBean bean = list.get(position);
            switch (view.getId()) {
                case R.id.item_qr_right:
                    list.remove(position);
                    DbHelper.deleteByBean(bean);
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
        List<UserBean> userBeans = DbHelper.SearchAll();
        if (!UText.isEmpty(userBeans)) {
            ULogBj.commonD("-->" + userBeans.size());
            list.addAll(userBeans);
        }
        adapter.notifyDataSetChanged();
    }
}
