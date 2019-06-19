package cos.mos.utils.initial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import cos.mos.toolkit.init.KFragment;

/**
 * @Description: Fragment广播 抽象父类
 * @Author: Kosmos
 * @Date: 2019.04.30 21:21
 * @Email: KosmoSakura@gmail.com
 */
public abstract class BroadCastFragment extends KFragment {
    private LocalBroadcastManager broadcastMgr;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播
            onKReceive();
        }
    };


    @Override
    protected void init() {
        if (doCast()) {
            //Fragment注册广播
            broadcastMgr = LocalBroadcastManager.getInstance(context);
            broadcastMgr.registerReceiver(receiver, new IntentFilter("to_do_again"));
        }
    }

    /**
     * 发送广播
     */
    protected void sendBroadcast() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("to_do_again"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastMgr != null) {
            broadcastMgr.unregisterReceiver(receiver);
        }
    }

    protected abstract boolean doCast();

    protected void onKReceive() {
    }
}
