package cos.mos.toolkit.media.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cos.mos.toolkit.R;
import cos.mos.toolkit.init.KApp;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * @Description: 简易录音机
 * @Author: Kosmos
 * @Date: 2019.05.29 14:05
 * @Email: KosmoSakura@gmail.com
 * 1.录音-》保存为wav
 * 2.丢子线程去
 * ---------------------------------------------
 * 权限：
 * Manifest.permission.RECORD_AUDIO
 * Manifest.permission.WRITE_EXTERNAL_STORAGE
 * ---------------------------------------------
 * AudioRecord(基于字节流录音)
 * 优点：可以实现语音的实时处理，进行边录边播，对音频的实时处理。
 * 缺点：输出的是PCM的语音数据，如果保存成音频文件是不能被播放器播放的。要用到AudioTrack这个去进行处理。
 */
public class UAudioRecord extends Thread {
    private AudioRecord recorder;
    private AudioTrack audioTrack;
    private byte[] audioData;
    private FileInputStream fileInputStream;
    private boolean isRecording;
    //保存目录+名字
    private String dirWave, dirPcm;

    public String getDirWave() {
        return dirWave;
    }

    /**
     * 保存根目录路径
     */
    private void checkDir() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WavRecorder" + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String name = dateFormat.format(new Date());
        //保存根目录路径
        dirWave = file.getAbsolutePath() + File.separator + name + ".pcm";
        //保存根缓存目录路径

    }

    /**
     * @param pcmFileAbsolute 源文件绝对路径
     */
    public void toConvert(String pcmFileAbsolute) {
        UPcm2Wav pcmToWavUtil = new UPcm2Wav();
        pcmToWavUtil.pcmToWav(pcmFileAbsolute, dirWave);
    }

    @Override
    public void run() {
        toStop();
        checkDir();
        toStart();
    }

    /**
     * 开始录音
     */
    public void toStart() {
        final int minBufferSize = AudioRecord.getMinBufferSize(44100,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
            AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

        final byte[] data = new byte[minBufferSize];
        final File file = new File(dirPcm);

        recorder.startRecording();
        isRecording = true;
        // pcm数据无法直接播放，保存为WAV格式。
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (null != os) {
                    while (isRecording) {
                        int read = recorder.read(data, 0, minBufferSize);
                        // 如果读取音频数据没有出现错误，就将数据写入到文件
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                os.write(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void toStop() {
        isRecording = false;
        // 释放资源
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            //recordingThread = null;
        }
    }

    /**
     * 播放，使用stream模式
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playInModeStream() {
        final int minBufferSize = AudioTrack.getMinBufferSize(44100,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build(),
            new AudioFormat.Builder().setSampleRate(44100)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),
            minBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE);
        audioTrack.play();

        File file = new File(checkDirCache(), getFileName());
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
                audioTrack = new AudioTrack(
                    new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                    new AudioFormat.Builder().setSampleRate(22050)
                        .setEncoding(AudioFormat.ENCODING_PCM_8BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build(),
                    audioData.length,
                    AudioTrack.MODE_STATIC,
                    AudioManager.AUDIO_SESSION_ID_GENERATE);
                Log.d(TAG, "Writing audio data...");
                audioTrack.write(audioData, 0, audioData.length);
                Log.d(TAG, "Starting playback");
                audioTrack.play();
                Log.d(TAG, "Playing");
            }

        }.execute();

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
}
