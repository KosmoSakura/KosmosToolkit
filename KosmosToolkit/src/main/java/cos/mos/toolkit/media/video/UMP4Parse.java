package cos.mos.toolkit.media.video;

import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 视频封装：基于MP4 Parser封装
 * @Author: Kosmos
 * @Date: 2019.02.22 19:41
 * @Email: KosmoSakura@gmail.com
 */
public class UMP4Parse {
    /**
     * @param mp4PathList [输入]Mp4文件路径的集合(支持m4a)(不支持wav)
     * @param outPutPath  [输出]结果文件全部名称包含后缀(比如.mp4)
     * @apiNote 对Mp4文件集合进行追加合并(按照顺序一个一个拼接起来)
     */
    public static void appendMp4List(List<String> mp4PathList, String outPutPath) {
        try {
            List<Movie> mp4MovieList = new ArrayList<>();// Movie对象集合[输入]
            for (String mp4Path : mp4PathList) {// 将每个文件路径都构建成一个Movie对象
                mp4MovieList.add(MovieCreator.build(mp4Path));
            }

            List<Track> audioTracks = new LinkedList<>();// 音频通道集合
            List<Track> videoTracks = new LinkedList<>();// 视频通道集合

            for (Movie mp4Movie : mp4MovieList) {// 对Movie对象集合进行循环
                for (Track inMovieTrack : mp4Movie.getTracks()) {
                    if ("soun".equals(inMovieTrack.getHandler())) {// 从Movie对象中取出音频通道
                        audioTracks.add(inMovieTrack);
                    }
                    if ("vide".equals(inMovieTrack.getHandler())) {// 从Movie对象中取出视频通道
                        videoTracks.add(inMovieTrack);
                    }
                }
            }
            Movie resultMovie = new Movie();// 结果Movie对象[输出]
            if (!audioTracks.isEmpty()) {// 将所有音频通道追加合并
                resultMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }

            if (!videoTracks.isEmpty()) {// 将所有视频通道追加合并
                resultMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }

            Container outContainer = new DefaultMp4Builder().build(resultMovie);// 将结果Movie对象封装进容器
            FileChannel fileChannel = new RandomAccessFile(String.format(outPutPath), "rw").getChannel();
            outContainer.writeContainer(fileChannel);// 将容器内容写入磁盘
            fileChannel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param aacPathList [输入]AAC文件路径的集合(不支持wav)
     * @param outPutPath  [输出]结果文件全部名称包含后缀(比如.aac)
     * @throws IOException 格式不支持等情况抛出异常
     * @apiNote 对AAC文件集合进行追加合并(按照顺序一个一个拼接起来)
     */
    public static void appendAacList(List<String> aacPathList, String outPutPath) {
        try {
            List<Track> audioTracks = new LinkedList<>();// 音频通道集合
            for (int i = 0; i < aacPathList.size(); i++) {// 将每个文件路径都构建成一个AACTrackImpl对象
                audioTracks.add(new AACTrackImpl(new FileDataSourceImpl(aacPathList.get(i))));
            }

            Movie resultMovie = new Movie();// 结果Movie对象[输出]
            if (!audioTracks.isEmpty()) {// 将所有音频通道追加合并
                resultMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }

            Container outContainer = new DefaultMp4Builder().build(resultMovie);// 将结果Movie对象封装进容器
            FileChannel fileChannel = new RandomAccessFile(String.format(outPutPath), "rw").getChannel();
            outContainer.writeContainer(fileChannel);// 将容器内容写入磁盘
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static List<Movie> moviesList = new ArrayList<>();
    private static List<Track> videoTracks = new ArrayList<>();
    private static List<Track> audioTracks = new ArrayList<>();

    //将两个mp4视频进行拼接
    public static void appendMp4(List<String> mMp4List, String outputpath) {
        try {
            for (int i = 0; i < mMp4List.size(); i++) {
                Movie movie = MovieCreator.build(mMp4List.get(i));
                moviesList.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Movie m : moviesList) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }

        Movie result = new Movie();
        try {
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Container out = new DefaultMp4Builder().build(result);
        try {
            FileChannel fc = new FileOutputStream(new File(outputpath)).getChannel();
            out.writeContainer(fc);
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        moviesList.clear();
    }


    /**
     * @param aacPath .aac
     * @param mp4Path .mp4
     * @param outPath .mp4
     * @apiNote 将 AAC 和 MP4 进行混合[替换了视频的音轨]
     */
    public static boolean muxAacMp4(String aacPath, String mp4Path, String outPath) {
        boolean flag = false;
        try {
            AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(aacPath));
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }

            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 视频部分
            resultMovie.addTrack(aacTrack);// 音频部分

            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
            flag = true;
            Log.e("update_tag", "merge finish");
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }


    /**
     * @param m4aPath .m4a[同样可以使用.mp4]
     * @param mp4Path .mp4
     * @param outPath .mp4
     * @apiNote 将 M4A 和 MP4 进行混合[替换了视频的音轨]
     */
    public static void muxM4AMp4(String m4aPath, String mp4Path, String outPath) throws IOException {
        Movie audioMovie = MovieCreator.build(m4aPath);
        Track audioTracks = null;// 获取视频的单纯音频部分
        for (Track audioMovieTrack : audioMovie.getTracks()) {
            if ("soun".equals(audioMovieTrack.getHandler())) {
                audioTracks = audioMovieTrack;
            }
        }
        Movie videoMovie = MovieCreator.build(mp4Path);
        Track videoTracks = null;// 获取视频的单纯视频部分
        for (Track videoMovieTrack : videoMovie.getTracks()) {
            if ("vide".equals(videoMovieTrack.getHandler())) {
                videoTracks = videoMovieTrack;
            }
        }
        Movie resultMovie = new Movie();
        resultMovie.addTrack(videoTracks);// 视频部分
        resultMovie.addTrack(audioTracks);// 音频部分

        Container out = new DefaultMp4Builder().build(resultMovie);
        FileOutputStream fos = new FileOutputStream(new File(outPath));
        out.writeContainer(fos.getChannel());
        fos.close();
    }


    /**
     * @param mp4Path .mp4
     * @param outPath .mp4
     * @apiNote 分离mp4视频的音频部分，只保留视频部分
     */
    public static void splitMp4(String mp4Path, String outPath) {
        try {
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 视频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param mp4Path .mp4
     * @param outPath .aac
     * @apiNote 分离mp4的视频部分，只保留音频部分
     */
    public static void splitAac(String mp4Path, String outPath) {
        try {
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取音频的单纯视频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks);// 音频部分

            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param mp4Path    .mp4
     * @param mp4OutPath mp4视频输出路径
     * @param aacOutPath aac视频输出路径
     * @apiNote 分离mp4视频的音频部分，只保留视频部分
     */
    public static void splitVideo(String mp4Path, String mp4OutPath, String aacOutPath) {
        try {
            Movie videoMovie = MovieCreator.build(mp4Path);
            Track videTracks = null;// 获取视频的单纯视频部分
            Track sounTracks = null;// 获取视频的单纯音频部分
            for (Track videoMovieTrack : videoMovie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videTracks = videoMovieTrack;
                }
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    sounTracks = videoMovieTrack;
                }
            }
            Movie videMovie = new Movie();
            videMovie.addTrack(videTracks);// 视频部分

            Movie sounMovie = new Movie();
            sounMovie.addTrack(sounTracks);// 音频部分
            // 视频部分
            Container videout = new DefaultMp4Builder().build(videMovie);
            FileOutputStream videfos = new FileOutputStream(new File(mp4OutPath));
            videout.writeContainer(videfos.getChannel());
            videfos.close();
            // 音频部分
            Container sounout = new DefaultMp4Builder().build(sounMovie);
            FileOutputStream sounfos = new FileOutputStream(new File(aacOutPath));
            sounout.writeContainer(sounfos.getChannel());
            sounfos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param mp4Path .mp4 添加字幕之前
     * @param outPath .mp4 添加字幕之后
     * @apiNote 对 Mp4 添加字幕
     */
    public static void addSubtitles(String mp4Path, String outPath) throws IOException {
        Movie videoMovie = MovieCreator.build(mp4Path);

        TextTrackImpl subTitleEng = new TextTrackImpl();// 实例化文本通道对象
        subTitleEng.getTrackMetaData().setLanguage("eng");// 设置元数据(语言)

        subTitleEng.getSubs().add(new TextTrackImpl.Line(0, 1000, "Five"));// 参数时间毫秒值
        subTitleEng.getSubs().add(new TextTrackImpl.Line(1000, 2000, "Four"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(2000, 3000, "Three"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(3000, 4000, "Two"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(4000, 5000, "one"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(5001, 5002, " "));// 省略去测试
        videoMovie.addTrack(subTitleEng);// 将字幕通道添加进视频Movie对象中

        Container out = new DefaultMp4Builder().build(videoMovie);
        FileOutputStream fos = new FileOutputStream(new File(outPath));
        out.writeContainer(fos.getChannel());
        fos.close();
    }


    /**
     * @param mp4Path    .mp4
     * @param fromSample 起始位置   不是传入的秒数
     * @param toSample   结束位置   不是传入的秒数
     * @param outPath    .mp4
     * @apiNote 将 MP4 切割
     */
    public static void cropMp4(String mp4Path, long fromSample, long toSample, String outPath) {
        try {
            Movie mp4Movie = MovieCreator.build(mp4Path);
            Track videoTracks = null;// 获取视频的单纯视频部分
            for (Track videoMovieTrack : mp4Movie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks = videoMovieTrack;
                }
            }
            Track audioTracks = null;// 获取视频的单纯音频部分
            for (Track audioMovieTrack : mp4Movie.getTracks()) {
                if ("soun".equals(audioMovieTrack.getHandler())) {
                    audioTracks = audioMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(new AppendTrack(new CroppedTrack(videoTracks, fromSample, toSample)));// 视频部分
            resultMovie.addTrack(new AppendTrack(new CroppedTrack(audioTracks, fromSample, toSample)));// 音频部分
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
