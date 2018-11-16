package cos.mos.utils.ui;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import cos.mos.utils.mvp.bean.ImageBean;


/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 21:32
 * @Email: KosmoSakura@gmail.com
 */
public interface MainListener {
    void success(List<ImageBean> list);

    void onError(String msg);

    FragmentActivity getActivity();
}
