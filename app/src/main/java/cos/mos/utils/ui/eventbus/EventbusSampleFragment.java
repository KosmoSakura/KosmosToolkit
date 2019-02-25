package cos.mos.utils.ui.eventbus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cos.mos.utils.zkosmoslibrary.utils.ULogBj;
import cos.mos.utils.zkosmoslibrary.init.KFragment;
import cos.mos.utils.R;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.20 15:43
 * @Email: KosmoSakura@gmail.com
 */
public class EventbusSampleFragment extends KFragment {

    @Override
    protected int layout() {
        return R.layout.frag_one;
    }

    @Override
    protected void init() {
        initEventBus = true;
    }

    @Override
    protected void logic() {
        EventBus.getDefault().postSticky("refresh");
    }

    /**
     * 注解中的属性,都有默认值的:
     * threadMode 表示在哪个线程中接收消息
     * sticky 如果为true,表示收到粘性事件;相应的在发送事件时,要使用postSticky(Object event);
     * priority 优先级,值越大,越先处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    public void onMainTabClick(String show) {
        ULogBj.commonD("收到：" + show);
    }
}
