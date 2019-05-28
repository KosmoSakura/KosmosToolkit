package cos.mos.toolkit.media.audio;

import android.media.MediaPlayer;

import java.io.IOException;


/**
 * @Description: 媒体播放
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class UMedia {
    private static UMedia sound;
    private MediaPlayer player;
    private boolean isPlaying = false;//当前是否正在播放音频
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
        if (player != null) {
            player.seekTo(msec);
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
            if (player == null) {
                startPlaying(); //从头开始
            } else {
                resume(); //恢复当前暂停的媒体播放器
            }
        } else {
            // 暂停
            pause();
        }
    }

    private void startPlaying() {
        if (dirAudio == null) {
            return;
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(dirAudio);
            player.prepare();
            player.setOnPreparedListener(mp -> player.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(mp -> stop());
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void prepareMediaPlayerFromPoint(int progress) {
        if (dirAudio == null) {
            return;
        }
        //设置mediaPlayer从音频文件的progress开始
        player = new MediaPlayer();
        try {
            player.setDataSource(dirAudio);
            player.prepare();
            player.seekTo(progress);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void resume() {
        if (player != null) {
            player.start();
        }
    }

    private void stop() {
        dirAudio = null;
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
        isPlaying = !isPlaying;
        lock = false;
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
