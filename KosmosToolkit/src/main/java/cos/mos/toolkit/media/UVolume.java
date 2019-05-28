package cos.mos.toolkit.media;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.ref.WeakReference;

import cos.mos.toolkit.init.KApp;

/**
 * @Description: 音量工具
 * @Author: Kosmos
 * @Date: 2019.05.27 10:51
 * @Email: KosmoSakura@gmail.com
 * 关于音量设置：
 * 因为音量是一个相对单位，每台设备的相对系数都可能不同。
 * 所以，如果想要设置一台手机的音量，需要以手机返回的最值音量作为参考
 * <uses-cos.mos.utils.ui.permission android:name="android.cos.mos.utils.ui.permission.MODIFY_AUDIO_SETTINGS"/>
 */
public class UVolume {
    private AudioManager audioMgr;
    private static UVolume volume;
    private WeakReference<Context> ctx;

    private UVolume() {
    }

    public static UVolume instance() {
        if (volume == null) {
            volume = new UVolume();
        }
        volume.ctx.clear();
        volume.ctx = null;
        return volume;
    }

    public static UVolume instance(Context context) {
        if (volume == null) {
            volume = new UVolume();
        }
        volume.ctx = new WeakReference<>(context);
        return volume;
    }

    private AudioManager getMgr() {
        if (audioMgr == null) {
            if (ctx == null) {
                audioMgr = (AudioManager) KApp.instance().getSystemService(Context.AUDIO_SERVICE);
            } else {
                audioMgr = (AudioManager) ctx.get().getSystemService(Context.AUDIO_SERVICE);
            }
        }
        return audioMgr;
    }

    /**
     * @param streamType 音量分类
     * @return 当前音量
     * @apiNote 乱输入返回-1
     */
    public int getVolume(int streamType) {
        switch (streamType) {
            case AudioManager.STREAM_MUSIC://音乐播放、媒体音量
            case AudioManager.STREAM_RING://电话铃声
            case AudioManager.STREAM_VOICE_CALL://通话
            case AudioManager.STREAM_SYSTEM://系统声音
            case AudioManager.STREAM_ALARM://闹钟、警报
            case AudioManager.STREAM_NOTIFICATION://顶部状态栏通知、Notification
            case AudioManager.STREAM_DTMF://双音多频
                return getMgr().getStreamVolume(streamType);
            default:
                return -1;
        }
    }

    /**
     * @param streamType 音量分类
     * @return 设备最大音量
     * @apiNote 乱输入返回-1
     */
    public int getVolumeMax(int streamType) {
        switch (streamType) {
            case AudioManager.STREAM_MUSIC://音乐播放、媒体音量
            case AudioManager.STREAM_RING://电话铃声
            case AudioManager.STREAM_VOICE_CALL://通话
            case AudioManager.STREAM_SYSTEM://系统声音
            case AudioManager.STREAM_ALARM://闹钟、警报
            case AudioManager.STREAM_NOTIFICATION://顶部状态栏通知、Notification
            case AudioManager.STREAM_DTMF://双音多频
                return getMgr().getStreamMaxVolume(streamType);
            default:
                return -1;
        }
    }

    /**
     * @param streamType 音量分类
     * @return 设备最大音量
     * @apiNote 乱输入返回-1
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public int getVolumeMin(int streamType) {
        switch (streamType) {
            case AudioManager.STREAM_MUSIC://音乐播放、媒体音量
            case AudioManager.STREAM_RING://电话铃声
            case AudioManager.STREAM_VOICE_CALL://通话
            case AudioManager.STREAM_SYSTEM://系统声音
            case AudioManager.STREAM_ALARM://闹钟、警报
            case AudioManager.STREAM_NOTIFICATION://顶部状态栏通知、Notification
            case AudioManager.STREAM_DTMF://双音多频
                return getMgr().getStreamMinVolume(streamType);
            default:
                return -1;
        }
    }

    /**
     * @param streamType 音量分类
     * @param direction  调整方向
     * @apiNote direction取值
     * AudioManager.ADJUST_LOWER：减少一格音量
     * AudioManager.ADJUST_RAISE：增加一格音量
     * AudioManager.ADJUST_SAME：保持不变音量, 主要用于向用户展示当前的音量
     */
    public void setAdjust(int streamType, int direction) {
        switch (streamType) {
            case AudioManager.STREAM_MUSIC://音乐播放、媒体音量
            case AudioManager.STREAM_RING://电话铃声
            case AudioManager.STREAM_VOICE_CALL://通话
            case AudioManager.STREAM_SYSTEM://系统声音
            case AudioManager.STREAM_ALARM://闹钟、警报
            case AudioManager.STREAM_NOTIFICATION://顶部状态栏通知、Notification
            case AudioManager.STREAM_DTMF://双音多频
                getMgr().adjustStreamVolume(streamType, direction, AudioManager.FLAG_SHOW_UI);
                break;
        }
    }

    /**
     * @param streamType 音量分类
     * @apiNote 把音量设置为系统最大值
     */
    public void setVolumeMax(int streamType) {
        setVolume(streamType, getVolumeMax(streamType));
    }

    /**
     * @param streamType 音量分类
     * @param volume     音量∈[min,max],超出无效
     * @apiNote 附加参数（用 | 链接）
     * AudioManager.FLAG_SHOW_UI:改变音量显示音量条（=按音量键出现的那）
     * AudioManager.FLAG_PLAY_SOUND：改变音量时是否播放声音
     * AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE：隐藏音量条
     */
    public void setVolume(int streamType, int volume) {
        switch (streamType) {
            case AudioManager.STREAM_MUSIC://音乐播放、媒体音量
            case AudioManager.STREAM_RING://电话铃声
            case AudioManager.STREAM_VOICE_CALL://通话
            case AudioManager.STREAM_SYSTEM://系统声音
            case AudioManager.STREAM_ALARM://闹钟、警报
            case AudioManager.STREAM_NOTIFICATION://顶部状态栏通知、Notification
            case AudioManager.STREAM_DTMF://双音多频
                getMgr().setStreamVolume(streamType, volume, AudioManager.FLAG_SHOW_UI);
                break;
        }
    }
}
