package cos.mos.utils.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

/**
 * @Description: 辅助通道服务
 * @Author Kosmos
 * @Date 2018.12.10 15:43
 * @Email: KosmoSakura@foxmail.com
 * @eg 这个服务不用单独启动。要获取里的东西可以直接获取
 * 比如获取包名： String topName = DetectAssistService.topName;//获取栈顶app的包名
 */
public class DetectAssistService extends AccessibilityService {
    public static String topName;

    /**
     * (必要)当系统监测到相匹配的AccessibilityEvent事件时，将调用此方法，
     * 在整个Service的生命周期中，该方法将被多次调用。
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //监听窗口焦点,并且获取焦点窗口的包名
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            topName = event.getPackageName().toString();

            /*
             * 基于以下还可以做很多事情，比如判断当前界面是否是 Activity，是否系统应用等，
             */
//            ComponentName cName = new ComponentName(event.getPackageName().toString(),
//                event.getClassName().toString());
        }
    }

    /**
     * (必要)系统需要中断AccessibilityService反馈时，将调用此方法。
     * AccessibilityService反馈包括服务发起的震动、音频等行为。
     */
    @Override
    public void onInterrupt() {

    }


    /**
     * (可选)当系统成功连接到该AccessibilityService时，将调用此方法。
     * 主要用与一次性配置或调整的代码。
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    /**
     * (可选)系统要关闭该服务是，将调用此方法。\
     * 主要用来释放资源。
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
