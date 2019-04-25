package cos.mos.utils.widget;

import java.io.FileInputStream;

import cos.mos.toolkit.ULog;
import cos.mos.utils.R;
import cos.mos.utils.init.BaseActivity;
import cos.mos.utils.widget.clip.VideoClipBar;
import cos.mos.utils.widget.clip.WaveClipBar;
import cos.mos.utils.widget.single.KRatingBar;

/**
 * @Description: 自定义控件演示 单独  single
 * @Author: Kosmos
 * @Date: 2019.04.25 14:23
 * @Email: KosmoSakura@gmail.com
 */
public class SampleActivity extends BaseActivity {
    @Override
    protected int layout() {
        return R.layout.activity_sample;
    }

    private KRatingBar bar;
    private VideoClipBar videoBar;
    private WaveClipBar seekbar;

    @Override
    protected void init() {
        bar = findViewById(R.id.rating_bar);
        videoBar = findViewById(R.id.vu_clip);
        seekbar = findViewById(R.id.au_clip);
        try {
            seekbar.setAudio(new FileInputStream("音频路径"), 11, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void logic() {
        bar.setStarEmpty(R.drawable.ic_android)
            .setStarFill(R.drawable.ic_loading)
            .setStarMax(5)
            .setRating(3)
            .setRatingChangeListener(new KRatingBar.RatingChangeListener() {
                @Override
                public void onRatingChange(int rating) {
                    ULog.commonD("感谢您的" + rating + "个星星");
                }
            }).runAnim();
        videoBar.setCursor(1);//整体长度的百分比
        videoBar.reset();//重置
        videoBar.setonRangeListener(new VideoClipBar.RangeListener() {
            @Override
            public void onRange(float pLeft, float pRight) {
                //左右游标百分比
            }

            @Override
            public void onRight() {
                //当播放指针到达右边游标
            }
        });
        seekbar.setCursor(1);//整体长度的百分比
        seekbar.setPoint(-1, -1);//左游标位置百分比
        seekbar.setonRangeListener(new WaveClipBar.RangeListener() {
            @Override
            public void onRange(float leftRange, float rightRange) {
                //左、右游标位置百分比
            }

            @Override
            public void loadComplete() {
                //当播放指针到达右边游标
            }
        });
    }
}
