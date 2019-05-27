package cos.mos.toolkit.media.audio;

import android.media.MediaPlayer;
import android.os.SystemClock;

import java.io.IOException;
import java.util.Random;


/**
 * @Description: 媒体播放
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class UMedia {
    private static UMedia sound;
    private MediaPlayer mMediaPlayer;
    private boolean isPlaying = false;//当前是否正在播放音频
    private Random random = new Random();
    private boolean lock;
    private String dirAudio;

    public static UMedia instance() {
        if (sound == null) {
            synchronized (UMedia.class) {
                if (sound == null) {
                    sound = new UMedia();
                }
            }
        }
        return sound;
    }

    private UMedia() {
    }

    /**
     * @param msec 毫秒
     */
    public void seekTo(int msec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    public void play(String dirAudio) {
        if (lock) {
            return;
        }
        this.dirAudio = dirAudio;
        lock = true;
        if (!isPlaying) {
            //目前MediaPlayer不播放音频
            if (mMediaPlayer == null) {
                startPlaying(); //从头开始
            } else {
                resumePlaying(); //恢复当前暂停的媒体播放器
            }
        } else {
            // 暂停
            pausePlaying();
        }
        SystemClock.sleep(1000);
        lock = false;
    }

    private void startPlaying() {
        if (dirAudio == null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(dirAudio);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(mp -> mMediaPlayer.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void prepareMediaPlayerFromPoint(int progress) {
        if (dirAudio == null) {
            return;
        }
        //设置mediaPlayer从音频文件的progress开始
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(dirAudio);
            mMediaPlayer.prepare();
            mMediaPlayer.seekTo(progress);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void pausePlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    private void resumePlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    private void stopPlaying() {
        dirAudio = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        isPlaying = !isPlaying;
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
