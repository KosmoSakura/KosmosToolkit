package cos.mos.library.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

/**
 * @Description: 音频池工具
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class USound {
    private SoundPool soundPool;
    private static USound sound;
    private SparseIntArray musicId = new SparseIntArray();


    //单例模式
    public static USound instance() {
        if (sound == null) {
            synchronized (USound.class) {
                if (sound == null) {
                    sound = new USound();
                }
            }
        }
        return sound;
    }

    private USound() {
        //加载音频文件
//        int soundId = soundPool.load(context, R.raw.voice_end, 1);
//        soundPool.load(context, R.raw.voice_move, 1);
//        soundPool.load(context, R.raw.voice_toggle, 1);
    }

    /**
     * @param res res为要被加载的raw资源
     * @apiNote SoundPool提供了如下4个load方法：
     * int load(Context context, int resld, int priority)：从 resld 所对应的资源加载声音。
     * int load(FileDescriptor fd, long offset, long length, int priority)：加载 fd 所对应的文件的offset开始、长度为length的声音。
     * int load(AssetFileDescriptor afd, int priority)：从afd 所对应的文件中加载声音。
     * int load(String path, int priority)：从path 对应的文件去加载声音。
     */
    public void load(Context context, int... res) {
        soundPool = new SoundPool(res.length,//允许同时存在的声音数量
            AudioManager.STREAM_SYSTEM,//声音流的类型，有：STREAM_RING、STREAM_MUSIC,一般都是使用后者
            0);//质量
        for (int index = 0; index < res.length; index++) {
            musicId.put(index, soundPool.load(context, res[index], 1));
        }
    }

    /**
     * @param index musicId中的下标
     */
    public void play(int index) {
        //播放音频
        soundPool.play(musicId.get(index),
            1,//左声道音量大小:0.0f - 1.0f,大音量的百分比
            1,//右声道音量大小
            0,//优先级，值越大优先级越高
            0,//是否需要循环播放，取值不限,负数表示无穷循环(官方建议，如果无穷循环，用-1，当然-2、-3等也行),
            // 播放次数=循环次数+1。比如0表示循环0次
            1);//播放速率(倍数)，取值0.5f - 2.0f，1表示正常速率播放
    }
}