package cos.mos.toolkit.media.audio;

import android.media.AudioManager;
import android.media.SoundPool;

import cos.mos.utils.R;
import cos.mos.utils.init.k.KApp;

/**
 * @Description: 音频池工具
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class USoundSample {
    private static USoundSample sound;
    private SoundPool soundPool;
    private int soundId;


    public static USoundSample instance() {
        if (sound == null) {
            sound = new USoundSample();
        }
        return sound;
    }

    private USoundSample() {
        getPool();
    }

    private SoundPool getPool() {
        if (soundPool == null) {
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
            soundId = soundPool.load(KApp.instance(), R.raw.voice_end, 1);
        }
        return soundPool;
    }

    /**
     * @param index musicId中的下标
     * @apiNote soundPool.load加载需要时间，成功之后才能播放
     * 所以我在app你们初始化
     */
    public void play() {
        getPool().play(soundId, 1, 1, 1, 0, 1);
    }
}
