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
    private MediaPlayer player;
    private boolean isPlaying = false;//当前是否正在播放音频
    private String url;

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
     * @param dirAudio 地址
     * @apiNote 修改播放音频
     */
    public void audioChange(String dirAudio) {
        this.url = dirAudio;
        if (!isPlaying) {
            //目前MediaPlayer不播放音频
            if (player == null) {
                newPlay(dirAudio); //从头开始
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
    public void newPlay(String dirAudio) {
        if (dirAudio == null) {
            return;
        }
        this.url = dirAudio;
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
        if (url == null) {
            return;
        }
        //设置mediaPlayer从音频文件的progress开始
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
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
        url = null;
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
