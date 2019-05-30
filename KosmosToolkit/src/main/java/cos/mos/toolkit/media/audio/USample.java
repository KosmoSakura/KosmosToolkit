package cos.mos.toolkit.media.audio;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.30 10:23
 * @Email: KosmoSakura@gmail.com
 */
public class USample {
    private URecorderMedia media;

    public void MediaSample() {
        //录音
        media = new URecorderMedia();
        media.start();
        //停止
        if (media != null) {
            media.toStop();
            media = null;
        }
    }

    private URecorderAudio audio;

    public void AudioSample() {
        //录音
        audio = new URecorderAudio();
        audio.start();
        //停止
        if (audio != null) {
            audio.toStop();
            audio = null;
        }
    }

}
