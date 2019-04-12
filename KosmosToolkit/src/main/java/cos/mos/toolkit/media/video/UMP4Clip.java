package cos.mos.toolkit.media.video;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 视频裁剪：基于MP4 Parser封装
 * @Author: Kosmos
 * @Date: 2019.02.21 17:35
 * @Email: KosmoSakura@gmail.com
 */
public class UMP4Clip {
    private double startTime, endTime; // 开始,结束裁剪的时间
    private List<Track> tracks;//视频轨道信息(视频流、声道)
    private Movie movie;//解析视频信息


    public interface ClipListener {
        void result(boolean success);
    }

    /**
     * @param inFile  被剪文件
     * @param outFile 输出文件
     * @param startS  开始时间(秒)
     * @param endS    结束时间(秒)
     */
    public void run(String inFile, String outFile, double startS, double endS, ClipListener listener) {
        this.startTime = startS;
        this.endTime = endS;
        try {
            timeCorrecting(inFile);
            positionCalculated();
            doClip(outFile);
            listener.result(true);
        } catch (IOException e) {
            listener.result(false);
        }
    }

    /**
     * @param videoFile 被剪文件
     * @apiNote 开始时间和结束时间进行校正
     */
    private void timeCorrecting(String videoFile) throws IOException {
        // 生成Movie对象(解析视频信息)
        movie = MovieCreator.build(videoFile);
        // 获取视频轨道信息(视频流、声道)
        tracks = movie.getTracks();
        // 设置新的轨道信息
        movie.setTracks(new LinkedList<>());
        // 时间是否准确的
        boolean timeCorrected = false;

        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                // true, false表示短截取, false,true表示长截取
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                timeCorrected = true;
            }
        }
    }

    /**
     * 根据校正后的时间得到剪辑开始和结束的位置
     */
    private void positionCalculated() {
        // 遍历计算裁剪时间(视频、音频) - 重新计算裁剪时间
        for (Track track : tracks) {
            long currentSample = 0;//视频截取到当前位置的时间
            double currentTime = 0;//视频的时间长度
            long startSample = -1;//截取的开始时间
            long endSample = -1;//截取结束时间
            // 获取编码过的数据
            List<TimeToSampleBox.Entry> listEntrys = track.getDecodingTimeEntries();
            // 遍历,计算关键帧差
            for (int i = 0, c = listEntrys.size(); i < c; i++) {
                TimeToSampleBox.Entry entry = listEntrys.get(i);
                for (int j = 0; j < entry.getCount(); j++) {
                    if (currentTime <= startTime) {
                        startSample = currentSample;
                    }
                    if (currentTime <= endTime) {
                        endSample = currentSample;
                    } else {
                        break;
                    }
                    currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                    currentSample++;
                }
            }
            //创建一个新的视频文件
            movie.addTrack(new CroppedTrack(track, startSample, endSample));
        }
    }


    /**
     * @param outFile 输出文件
     */
    private void doClip(String outFile) throws IOException {
        //合成视频MP4
        Container container = new DefaultMp4Builder().build(movie);
        File trimFile = new File(outFile);
        if (!trimFile.exists()) {
            trimFile.createNewFile();
        }
        // 写入流
        FileOutputStream fos = new FileOutputStream(trimFile);
        FileChannel fc = fos.getChannel();
        container.writeContainer(fc);
        //关流
        fc.close();
        fos.close();
    }


    /**
     * @param track   true = 获取与裁剪时间最大值关键帧时间 false = 获取与裁剪时间最接近的时间
     * @param cutHere 裁剪的时间
     * @param next    是否获取最大值
     * @return 修正关键帧时间
     */
    private double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        // 偏移量数组长度
        int tLength = track.getSyncSamples().length;
        // 时间偏差量计算
        double[] timeOfSyncSamples = new double[tLength];
        // 当前偏差量
        long currentSample = 0;
        // 当前时间
        double currentTime = 0;
        // 获取编码过的数据
        List<TimeToSampleBox.Entry> listEntrys = track.getDecodingTimeEntries();
        // 遍历,计算关键帧差
        for (int i = 0, c = listEntrys.size(); i < c; i++) {
            TimeToSampleBox.Entry entry = listEntrys.get(i);
            for (int j = 0; j < entry.getCount(); j++) {
                // 获取偏移量索引
                int tofPos = Arrays.binarySearch(track.getSyncSamples(), currentSample + 1);
                if (tofPos >= 0) {
                    timeOfSyncSamples[tofPos] = currentTime;
                }
                currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
        }
        // 获取裁剪位置，之间的关键帧数据
        double previous = 0;
        // 遍历全部关键帧时间
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
