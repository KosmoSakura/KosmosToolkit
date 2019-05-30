package cos.mos.toolkit.media.audio;

import android.media.MediaPlayer;

import java.io.IOException;


/**
 * @Description: 媒体播放
 * @Author: Kosmos
 * @Date: 2018.11.30 16:47
 * @Email: KosmoSakura@gmail.com
 */
public class UPlayerMedia {
    private static UPlayerMedia sound;
    private MediaPlayer player;
    private boolean isPlaying = false;//当前是否正在播放音频
    private String dirAudio;

    public static UPlayerMedia instance() {
        if (sound == null) {
            synchronized (UPlayerMedia.class) {
                if (sound == null) {
                    sound = new UPlayerMedia();
                }
            }
        }
        return sound;
    }

    private UPlayerMedia() {
    }

    /**
     * @param msec 毫秒
     * @apiNote 跳转播放
     */
    public void seekTo(int msec) {
        if (player != null) {
            player.seekTo(msec);
        }
    }

    /**
     * 自动播放（内部判断流程）
     */
    public void toPlayAuto(String dirAudio) {
        this.dirAudio = dirAudio;
        if (!isPlaying) {
            //目前MediaPlayer不播放音频
            if (player == null) {
                toPlay(dirAudio); //从头开始
            } else {
                toResume(); //恢复当前暂停的媒体播放器
            }
        } else {
            // 暂停
            toPause();
        }
    }

    /**
     * 播放
     */
    private void toPlay(String dirAudio) {
        if (dirAudio == null) {
            return;
        }
        this.dirAudio = dirAudio;
        player = new MediaPlayer();
        try {
            player.setDataSource(dirAudio);
            player.prepare();
            player.setOnPreparedListener(mp -> player.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(mp -> toStop());
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 从某一个点开始播放
     */
    private void toPlayBySeek(int progress) {
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
                    toStop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保持屏幕打开
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 暂停
     */
    public void toPause() {
        if (player != null) {
            player.pause();
        }
    }

    /**
     * 恢复播放
     */
    public void toResume() {
        if (player != null) {
            player.start();
        }
    }

    /**
     * 停止
     */
    private void toStop() {
        dirAudio = null;
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
        isPlaying = !isPlaying;
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
