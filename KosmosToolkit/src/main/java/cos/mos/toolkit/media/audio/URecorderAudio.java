package cos.mos.toolkit.media.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class URecorderAudio extends Thread {
    private AudioRecord recorder;
    private boolean isRecording;
    //保存目录+名字
    private String dirWave, dirPcm;

    public String getDirWave() {
        return dirWave;
    }

    public String getDirPcm() {
        return dirPcm;
    }

    /**
     * 保存根目录路径
     */
    private void checkDir() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String name = dateFormat.format(new Date());
        //保存根目录路径 .wav
        dirWave = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "WavRecorder";
        //保存根缓存目录路径 .pcm
        dirPcm = dirWave + File.separator + ".cache";
        File file = new File(dirPcm);
        if (!file.exists()) {
            file.mkdirs();
        }
        dirWave = dirWave + name + ".wav";
        dirPcm = dirPcm + name + ".pcm";
    }

    /**
     * pcm转wav
     */
    public void toConvert() {
        new UPcm2Wav().pcmToWav(dirPcm, dirWave);
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
    private void toStart() {
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

    public String toStop() {
        isRecording = false;
        // 释放资源
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            recorder = null;
        }
        return dirPcm;
    }
}
