package cos.mos.utils.init;

import cos.mos.library.init.KFragment;
import cos.mos.library.retrofit.HostWrapper;

/**
 * @Description: base
 * @Author: Kosmos
 * @Date: 2018年08月02日 15:01
 * @Email: KosmoSakura@gmail.com
 */
public abstract class BaseFragment extends KFragment {
    private RequestServes rs;

    protected RequestServes getServes() {
        if (rs == null) {
            rs = HostWrapper.with().create(RequestServes.class);
        }
        return rs;
    }
}
