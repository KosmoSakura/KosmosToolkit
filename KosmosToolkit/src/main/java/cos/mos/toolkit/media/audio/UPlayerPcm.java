package cos.mos.toolkit.media.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cos.mos.toolkit.R;
import cos.mos.toolkit.init.KApp;

/**
 * @Description: PCM编码的播放器
 * @Author: Kosmos
 * @Date: 2019.05.30 11:08
 * @Email: KosmoSakura@gmail.com
 */
public class UPlayerPcm {
    private AudioTrack audioTrack;
    private byte[] audioData;
    private FileInputStream fileInputStream;
    private String dirPcm;

    public UPlayerPcm(String dirPcm) {
        this.dirPcm = dirPcm;
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    /**
     * 播放，使用stream模式
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playPcm() {
        final int minBufferSize = AudioTrack.getMinBufferSize(44100,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),

            new AudioFormat.Builder().setSampleRate(44100).setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build(),

            minBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);

        audioTrack.play();

        File file = new File(dirPcm);
        try {
            fileInputStream = new FileInputStream(file);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] tempBuffer = new byte[minBufferSize];
                        while (fileInputStream.available() > 0) {
                            int readCount = fileInputStream.read(tempBuffer);
                            if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                readCount == AudioTrack.ERROR_BAD_VALUE) {
                                continue;
                            }
                            if (readCount != 0 && readCount != -1) {
                                audioTrack.write(tempBuffer, 0, readCount);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放，使用static模式
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playInModeStatic() {
        // static模式，需要将音频数据一次性write到AudioTrack的内部缓冲区
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = KApp.instance().getResources().openRawResource(R.raw.voice_end);
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        for (int b; (b = in.read()) != -1; ) {
                            out.write(b);
                        }
                        //得到的数据
                        audioData = out.toByteArray();
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    //读取失败
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                // R.raw.ding铃声文件的相关属性为 22050Hz, 8-bit, Mono
                audioTrack = new AudioTrack(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),

                    new AudioFormat.Builder().setSampleRate(22050).setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build(),

                    audioData.length, AudioTrack.MODE_STATIC, AudioManager.AUDIO_SESSION_ID_GENERATE);

                //写音频数据
                audioTrack.write(audioData, 0, audioData.length);
                //开始播放
                audioTrack.play();
            }
        }.execute();
    }

}
