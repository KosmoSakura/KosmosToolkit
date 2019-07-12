package cos.mos.utils.widget;

import android.animation.ValueAnimator;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cos.mos.toolkit.ULog;
import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;
import cos.mos.utils.initial.BaseActivity;
import cos.mos.utils.widget.chart.ColumnarChartScroller;
import cos.mos.utils.widget.chart.ColumnarChartScrollerBean;
import cos.mos.utils.widget.chart.LineBean;
import cos.mos.utils.widget.chart.LineChart;
import cos.mos.utils.widget.progress.AudioBar;
import cos.mos.utils.widget.progress.LineBar;
import cos.mos.utils.widget.progress.ScanningBar;
import cos.mos.utils.widget.progress.WaveShapeBar;
import cos.mos.utils.widget.progress.clip.VideoClipBar;
import cos.mos.utils.widget.progress.clip.WaveClipBar;
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

    private ColumnarChartScroller columnarChartScroller;
    private KRatingBar bar;
    private LineChart lineChart;
    private VideoClipBar videoBar;
    private WaveClipBar seekbar;
    private WaveShapeBar wpv;
    private ScanningBar sv1;
    private LineBar lneb;
    private View lnebInner;
    private AudioBar auBar;

    @Override
    protected void init() {
        auBar = findViewById(R.id.aubar);
        bar = findViewById(R.id.rating_bar);
        videoBar = findViewById(R.id.vu_clip);
        seekbar = findViewById(R.id.au_clip);
        lineChart = findViewById(R.id.tbShow);
        sv1 = findViewById(R.id.st2_f1);
        lneb = findViewById(R.id.st3_line);
        lnebInner = findViewById(R.id.st3_text);
        columnarChartScroller = findViewById(R.id.tb_cht);
        try {
            seekbar.setAudio(new FileInputStream("音频路径"), 11, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void logic() {
        lineChartSample();
        columnarChartScroller();
        ratingBarSample();
        videoClipSample();
        waveClipSample();
        scanningBarample();
        waveShapBarSample();

        lneb.start();
        lneb.setCompleteListener(new LineBar.CompleteListener() {
            @Override
            public void complete() {
                lnebInner.setVisibility(View.VISIBLE);
                lnebInner.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_scale));
            }
        });
    }

    private void columnarChartScroller() {
        List<ColumnarChartScrollerBean> all = new ArrayList<>();
        all.add(new ColumnarChartScrollerBean(1600, "6.29"));
        all.add(new ColumnarChartScrollerBean(2000, "6.30"));
        all.add(new ColumnarChartScrollerBean(1200, "6.31"));
        all.add(new ColumnarChartScrollerBean(443, "7.1"));
        all.add(new ColumnarChartScrollerBean(3000, "7.2"));
        columnarChartScroller.setBarChartData(all, 1500);
    }

    private void scanningBarample() {
        //反向扫描
        sv1.setImages(true, R.drawable.ic_fingerprint_font_f,
            R.drawable.ic_fingerprint_bar_f, R.drawable.ic_fingerprint_mask_f);
        //正向扫描
        sv1.setImages(false, R.drawable.ic_fingerprint_font,
            R.drawable.ic_fingerprint_bar, R.drawable.ic_fingerprint_mask);
        sv1.setOnStateListener(new ScanningBar.StateListener() {
            @Override
            public void state(int done) {
                //done -1：失败，1：成功（手指松开），0：成功（手指未松开）
            }
        });
    }

    private void lineChartSample() {
        ArrayList<LineBean> list = new ArrayList<>();
        list.add(new LineBean(1, 233f));
        list.add(new LineBean(2, 233f));
        list.add(new LineBean(3, 233f));
        list.add(new LineBean(4, 233f));
        list.add(new LineBean(5, 233f));
        lineChart.setChartsDescribe("表描述")
            .setName("属性1描述", "属性2描述")
            .setData(list)
            .show();
    }

    private void ratingBarSample() {
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
    }

    private void videoClipSample() {
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
    }

    private void waveClipSample() {
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

    private void waveShapBarSample() {
        wpv = findViewById(R.id.st3_wpv);
        wpv.setText(ContextCompat.getColor(this, R.color.white), UScreen.dp2px(40))
            .setWaveColor(ContextCompat.getColor(this, R.color.fun_txt_pink))
            .setSpeed(10)//8
            .setMaxProgress(100)
            .build();
        wpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wpv.clear();
                startAnim(new Random().nextInt(100));
            }
        });
    }

    private void startAnim(int max) {
        ValueAnimator animator = ValueAnimator.ofInt(0, max);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            wpv.setProgress(value, value + "%");
        });
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
