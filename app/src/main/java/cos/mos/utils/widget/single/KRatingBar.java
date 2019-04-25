package cos.mos.utils.widget.single;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cos.mos.utils.R;


/**
 * @Description: 带动画的RatingBar
 * @Author: Kosmos
 * @Date: 2018.11.02 14:58
 * @Email: KosmoSakura@foxmail.com
 */
public class KRatingBar extends LinearLayout implements View.OnClickListener {
    private RatingChangeListener listener;
    private int starMax;
    private float starWidth;
    private float starHeight;
    private Drawable starEmpty;
    private Drawable starFill;
    private int rating;//点亮的星星数目
    private Context context;

    public KRatingBar setRatingChangeListener(RatingChangeListener listener) {
        this.listener = listener;
        return this;
    }

    public KRatingBar setStarFill(int res) {
        this.starFill = ContextCompat.getDrawable(context, res);
        return this;
    }

    public KRatingBar setStarEmpty(int res) {
        this.starEmpty = ContextCompat.getDrawable(context, res);
        return this;
    }

    public KRatingBar setStarMax(int starMax) {
        this.starMax = starMax;
        return this;
    }

    public KRatingBar setRating(int rating) {
        this.rating = rating > starMax ? starMax : rating;
        return this;
    }

    public void runAnim() {
        drawStar();
    }

    public void setStarWidth(float starWidth) {
        this.starWidth = starWidth;
    }

    public void setStarHeight(float starHeight) {
        this.starHeight = starHeight;
    }

    public KRatingBar(Context context) {
        this(context, null);
    }

    public KRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KRatingBar, defStyleAttr, 0);
        starEmpty = ta.getDrawable(R.styleable.KRatingBar_starEmpty);
        starFill = ta.getDrawable(R.styleable.KRatingBar_starFill);
        starWidth = ta.getDimension(R.styleable.KRatingBar_star_width, 60);
        starHeight = ta.getDimension(R.styleable.KRatingBar_star_height, 120);
        rating = ta.getInteger(R.styleable.KRatingBar_rating, 0);
        starMax = ta.getInteger(R.styleable.KRatingBar_starMax, 5);
        ta.recycle();
        setOrientation(LinearLayout.HORIZONTAL);
        //防止使用者乱输点亮的星星数目
        rating = rating > starMax ? starMax : rating;
        //绘制初试状态
        drawInit(context);
    }

    /**
     * @apiNote 绘制初试状态
     */
    private void drawInit(Context context) {
        for (int i = 0; i < starMax; i++) {
            ImageView image = getImageView(context);
            if (i < rating) {
                image.setImageDrawable(starFill);
            } else {
                image.setImageDrawable(starEmpty);
            }
            image.setOnClickListener(this);
            addView(image);
            animInit(image);
        }
    }

    /**
     * @apiNote 入场动画
     */
    private void animInit(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(scaleX, scaleY);
        animSet.setDuration(800);
        animSet.start();
    }

    @Override
    public void onClick(View view) {
        rating = indexOfChild(view) + 1;
        drawStar();
        if (listener != null) {
            listener.onRatingChange(rating);
        }
    }

    private ImageView getImageView(Context context) {
        ImageView image = new ImageView(context);
        LayoutParams para = new LayoutParams(Math.round(starWidth), Math.round(starHeight), 1);
        image.setLayoutParams(para);
        return image;
    }

    /**
     * @apiNote 绘制点亮的星星
     */
    private void drawStar() {
        for (int i = 0; i < starMax; i++) {
            ImageView image = (ImageView) getChildAt(i);
            if (i < rating) {
                image.setImageDrawable(starFill);
                animClick(image);
            } else {
                image.setImageDrawable(starEmpty);
            }
        }
    }

    /**
     * @apiNote 点击动画
     */
    private void animClick(View view) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotationY", 0, 360);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new LinearInterpolator());
        animSet.play(rotation);
        animSet.setDuration(800);
        animSet.start();
    }

    public interface RatingChangeListener {
        void onRatingChange(int rating);
    }
}
