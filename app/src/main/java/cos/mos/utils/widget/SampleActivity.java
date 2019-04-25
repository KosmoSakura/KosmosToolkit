package cos.mos.utils.widget;

import cos.mos.toolkit.ULog;
import cos.mos.utils.R;
import cos.mos.utils.init.BaseActivity;
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

    @Override
    protected void init() {
        bar = findViewById(R.id.rating_bar);
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
    }
}
