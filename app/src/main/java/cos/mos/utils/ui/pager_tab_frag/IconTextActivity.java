package cos.mos.utils.ui.pager_tab_frag;


import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cos.mos.utils.init.k.KActivity;
import cos.mos.utils.R;

public class IconTextActivity extends KActivity {
    private TabLayout tl;
    private ViewPager vp;

    @Override
    protected int layout() {
        return R.layout.activity_tab_pager_frag;
    }

    @Override
    protected void init() {
        tl = findViewById(R.id.wifi_tl);
        vp = findViewById(R.id.wifi_vp);

        Fragment[] fragmentArray = new Fragment[2];
        fragmentArray[0] = new FragOne();
        fragmentArray[1] = new FragTwo();

        String[] titles = {"One", "Two"};
        int[] icons = {R.drawable.sl_qr, R.drawable.sl_history};

        KPagerAdapter adapter = new KPagerAdapter(getSupportFragmentManager(), fragmentArray, titles);
        vp.setAdapter(adapter);
        //预加载管理,除去当前显示页面以外需要被预加载的页面数。
        vp.setOffscreenPageLimit(2);
        tl.setupWithViewPager(vp);
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tl.getTabAt(i);
            if (tab != null) {
                tab.setText(titles[i]).setIcon(icons[i]);
            }
        }
    }

    @Override
    protected void logic() {

    }
}
