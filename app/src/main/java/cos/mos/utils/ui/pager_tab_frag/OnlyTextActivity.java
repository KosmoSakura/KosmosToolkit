package cos.mos.utils.ui.pager_tab_frag;


import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cos.mos.library.init.KActivity;
import cos.mos.utils.R;

public class OnlyTextActivity extends KActivity {
    private TabLayout tl;
    private ViewPager vp;
    private Fragment[] fragmentArray;

    @Override
    protected int layout() {
        return R.layout.activity_tab_pager_frag;
    }

    @Override
    protected void init() {
        tl = findViewById(R.id.wifi_tl);
        vp = findViewById(R.id.wifi_vp);

        fragmentArray = new Fragment[3];
        fragmentArray[0] = new WifiScanFragment();
        fragmentArray[1] = new WifiListFragment();
        fragmentArray[2] = new WifiHistoryFragment();

        String[] titles = {"Scan", "Generate", "History"};
        int[] icons = {R.drawable.sl_qr, R.drawable.sl_generate, R.drawable.sl_history};
        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tl.newTab();
            tab.setIcon(icons[i]);
            tab.setText(titles[i]);
            tl.addTab(tab);
        }
        KPagerAdapter adapter = new KPagerAdapter(getSupportFragmentManager(), fragmentArray);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);//预加载管理,除去当前显示页面以外需要被预加载的页面数。
    }

    @Override
    protected void logic() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                tl.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tl.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
