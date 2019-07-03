package cos.mos.toolkit.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import cos.mos.toolkit.R;
import cos.mos.toolkit.init.KApp;


/**
 * @Description: 通知工具，适配Android8.0
 * @Author: Kosmos
 * @Date: 2018.12.25 11:04
 * @Email: KosmoSakura@gmail.com
 * mBuilder.setContentTitle("测试标题")//设置通知栏标题
 * .setContentText("测试内容") /<span style="font-family: Arial;">/设置通知栏显示内容</span>
 * .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
 * .setNumber(number) //设置通知集合的数量
 * .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
 * .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
 * .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
 * .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
 * .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
 * .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
 * //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE cos.mos.utils.ui.permission
 * .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
 */
public class UNotify {
    /**
     * Service的onCreate中通知
     */
    private abstract class SampleService extends Service {
        public static final int NOTICE_ID = 10010;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT));
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
                builder.setSmallIcon(R.drawable.lib_error);
                startForeground(NOTICE_ID, builder.build());
            } else {
                startForeground(NOTICE_ID, new Notification());
            }
            new Thread(() -> {
                SystemClock.sleep(1000);
                stopForeground(true);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(NOTICE_ID);
            }).start();

            //返回START_STICKY，当Service进程被kill后，系统会尝试重新创建这个Service
            //保留Service的状态为开始状态，但不保留传递的Intent对象
            //onStartCommand 方法一定会被重新调用
            return START_STICKY;
        }
    }

    private static NotificationManager notifyMgr = (NotificationManager) KApp.instance().getSystemService(Context.NOTIFICATION_SERVICE);
    private static final String CHANNEL_ID = "SAKURA_RUN";
    private static final String CHANNEL_NAME = "SAKURA_NAME";
    private static UNotify nft;
    private Notification notify;

    private UNotify() {
    }

    public static UNotify instance() {
        if (nft == null) {
            synchronized (UNotify.class) {
                if (nft == null) {
                    nft = new UNotify();
                }
            }
        }
        return nft;
    }

    /**
     * @param title 标题
     * @param msg   信息
     * @param icon  图标
     * @apiNote 栗子
     * UNotify.instance()
     * .init("流氓1", "1号流氓程序正在运行中", R.drawable.svg_reboot_red)
     * .cleared(true)
     * .show(2);
     */
    public UNotify init(String title, String msg, int icon) {
        createChannel();
        notify = new NotificationCompat.Builder(KApp.instance(), CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(msg)
            .setWhen(System.currentTimeMillis())
            .build();
        return nft;
    }

    /**
     * @apiNote 添加点击意图-栗子
     * UNotify.instance()
     * .init("流氓1", "1号流氓程序正在运行中", R.drawable.svg_reboot_red, MainActivity.class)
     * .cleared(true)
     * .show(2);
     */
    public UNotify initIntent(String title, String msg, int icon, Class aClass) {
        createChannel();
        Intent intent = new Intent(KApp.instance(), aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(KApp.instance(), 0,
            intent, 0);
        notify = new NotificationCompat.Builder(KApp.instance(), CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build();
        return nft;
    }

    /**
     * @param autoCLear 能不能自动清除
     */
    public UNotify cleared(boolean autoCLear) {
        if (autoCLear) {
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            notify.flags = Notification.FLAG_NO_CLEAR;
        }
        return nft;
    }

    public void show(int id) {
        notifyMgr.notify(id, notify);
    }

    private static void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notifyMgr.createNotificationChannel(channel);
        }
    }

    public void clear(int id) {
        notifyMgr.cancel(id);
    }

    public void clearAll() {
        notifyMgr.cancelAll();
    }
}
