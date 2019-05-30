package cos.mos.toolkit.media.audio;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.30 10:23
 * @Email: KosmoSakura@gmail.com
 */
public class USample {

    public void MediaSample() {
        //录音
        UMediaRecorder recorder = new UMediaRecorder();
        recorder.start();
        //停止
        if (recorder != null) {
            recorder.toStop();
            recorder = null;
        }
    }
}
