package cos.mos.utils.ui.pager_tab_frag;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import io.reactivex.annotations.Nullable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.20 15:52
 * @Email: KosmoSakura@gmail.com
 */
public class KPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] arr;
    private String[] title;


    public KPagerAdapter(FragmentManager fm, Fragment[] arr, String... title) {
        super(fm);
        this.arr = arr;
        this.title = title;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (title == null) {
            return "";
        } else {
            return title[position];
        }
    }

    @Override
    public Fragment getItem(int position) {
        return arr[position];
    }

    @Override
    public int getCount() {
        return arr.length;
    }
}
