package cos.mos.utils.ui.pager_tab_frag;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import cos.mos.utils.R;
import cos.mos.utils.init.k.KActivity;

public class OnlyTextActivity extends KActivity {

    @Override
    protected int layout() {
        return R.layout.activity_tab_pager_frag;
    }

    @Override
    protected void init() {
        TabLayout tl = findViewById(R.id.wifi_tl);
        ViewPager vp = findViewById(R.id.wifi_vp);

        Fragment[] fragmentArray = new Fragment[2];
        fragmentArray[0] = new FragOne();
        fragmentArray[1] = new FragTwo();

        KPagerAdapter adapter = new KPagerAdapter(getSupportFragmentManager(), fragmentArray, "One", "Two");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);//预加载管理,除去当前显示页面以外需要被预加载的页面数。
        tl.setupWithViewPager(vp, true);
    }

    @Override
    protected void logic() {

    }

}
