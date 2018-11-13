package cos.mos.utils.init;

import cos.mos.library.init.KActivity;
import cos.mos.library.retrofit.HostWrapper;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月02日 13:17
 * @Email: KosmoSakura@gmail.com
 */
public abstract class BaseActivity extends KActivity {
    private RequestServes rs;

    protected RequestServes getServes() {
        if (rs == null) {
            rs = HostWrapper.with().create(RequestServes.class);
        }
        return rs;
    }
}
