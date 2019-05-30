package cos.mos.toolkit.media.audio;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import cos.mos.toolkit.R;
import cos.mos.toolkit.init.KApp;


/**
 * @Description: 音频池工具
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class USampleSound {
    private static USampleSound sound;
    private SoundPool soundPool;
    private int soundId;


    public static USampleSound instance() {
        if (sound == null) {
            sound = new USampleSound();
        }
        return sound;
    }

    private USampleSound() {
        getPool();
    }

    private SoundPool getPool() {
        if (soundPool == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes localAudioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
                soundPool = new SoundPool.Builder().setAudioAttributes(localAudioAttributes).setMaxStreams(1).build();
            } else {
                soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            }
        }
        soundId = soundPool.load(KApp.instance(), R.raw.voice_end, 1);
        return soundPool;
    }

    /**
     * @apiNote soundPool.load加载需要时间，成功之后才能播放
     * 所以我在app你们初始化
     */
    public void play() {
        getPool().play(soundId, 1, 1, 1, 0, 1);
    }
}
