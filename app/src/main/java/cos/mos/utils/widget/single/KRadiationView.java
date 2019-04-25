package cos.mos.utils.widget.single;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.List;

import cos.mos.utils.R;


/**
 * @Description: 圆心辐射动画
 * @Author: Kosmos
 * @Date: 2019.03.12 14:43
 * @Email: KosmoSakura@gmail.com
 * @apiNote 控件宽高必须相等
 */
public class KRadiationView extends View {
    private static final long delay = 33;//扩散延迟
    private int color;//圆的主题色
    //    private int centerColor;//中心圆颜色
    //    private int radiaColor;//扩散圆颜色
    private int radius = 100;//中心圆半径
    private int radiusMax;//扩散最大半径根据控件宽计算
    private int distance = 5;//每次圆递增间距
    private float centerX, centerY;//圆心坐标
    private List<Integer> spreadRadius = new ArrayList<>();//扩散圆层级数，元素为扩散的距离
    private List<Integer> alphas = new ArrayList<>();//对应每层圆的透明度
    private Paint paint;


    public KRadiationView(Context context) {
        this(context, null, 0);
    }

    public KRadiationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KRadiationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpreadView, defStyleAttr, 0);
//        radius = a.getInt(R.styleable.SpreadView_spread_radius, radius);
//        radiusMax = a.getInt(R.styleable.SpreadView_spread_max_radius, maxRadius);
//        centerColor = a.getColor(R.styleable.SpreadView_spread_center_color, ContextCompat.getColor(context, R.color.colorAccent));
//        radiaColor = a.getColor(R.styleable.SpreadView_spread_spread_color, ContextCompat.getColor(context, R.color.colorAccent));
//        distance = a.getInt(R.styleable.SpreadView_spread_distance, distance);
//        a.recycle();

        //最开始不透明且扩散距离为0
//        alphas.add(255);
//        spreadRadius.add(0);
//        spreadPaint = new Paint();
//        spreadPaint.setAntiAlias(true);
//        spreadPaint.setAlpha(255);
//        spreadPaint.setColor(spreadColor);

        color = ContextCompat.getColor(context, R.color.white);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        radiusMax = w >> 1;
        centerX = radiusMax;
        centerY = h >> 1;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setColor(color);
            paint.setAntiAlias(true);
        }
        //绘制扩散的圆
        canvas.drawCircle(centerX, centerY, radiusMax, paint);
        showAnim();
        postInvalidateDelayed(delay);
    }

    /**
     * 中间圆的动画
     */
    private void setAnim1() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从原始放大到1.4倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        startAnimation(as);
    }

    /**
     * 最外层圆的动画
     */
    private void showAnim() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从1.4倍放大到1.8倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.1f);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        startAnimation(as);
    }
}
