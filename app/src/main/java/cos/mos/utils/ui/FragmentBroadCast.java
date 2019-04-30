package cos.mos.utils.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import cos.mos.utils.init.KFragment;

/**
 * @Description: Fragment广播
 * @Author: Kosmos
 * @Date: 2019.04.30 21:21
 * @Email: KosmoSakura@gmail.com
 */
public class FragmentBroadCast extends KFragment {
    private LocalBroadcastManager broadcastMgr;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播
        }
    };

    @Override
    protected int layout() {
        return 0;
    }

    @Override
    protected void init() {
        //发送广播
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("to_do_again"));

        //Fragment注册广播
        broadcastMgr = LocalBroadcastManager.getInstance(getActivity());
        broadcastMgr.registerReceiver(receiver, new IntentFilter("to_do_again"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastMgr.unregisterReceiver(receiver)
    }

    @Override
    protected void logic() {

    }
}
