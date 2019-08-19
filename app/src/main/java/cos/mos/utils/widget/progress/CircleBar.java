package cos.mos.utils.widget.progress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import cos.mos.utils.R;


/**
 * @Description: 圆形进度条
 * @Author: Kosmos
 * @Date: 2019.05.28 16:38
 * @Email: KosmoSakura@gmail.com
 */
public class CircleBar extends View {
    private int outsideColor;//进度的颜色
    private float outsideRadius;//外圆半径大小
    private int insideColor;//背景颜色
    private int progressTextColor;//圆环内文字颜色
    private float progressTextSize;//圆环内文字大小
    private float progressWidth;//圆环的宽度
    private int maxProgress;//最大进度
    private float progress;//当前进度

    private Paint paint;
    private Rect rect;
    private RectF oval;

    public CircleBar(Context context) {
        this(context, null);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleBar, defStyleAttr, 0);
        //已经消耗的进度颜色
        outsideColor = a.getColor(R.styleable.CircleBar_outside_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //剩余的进度颜色
        insideColor = a.getColor(R.styleable.CircleBar_inside_color, ContextCompat.getColor(getContext(), R.color.white));
        //文字颜色
        progressTextColor = a.getColor(R.styleable.CircleBar_progress_text_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //圆半径
        outsideRadius = a.getDimension(R.styleable.CircleBar_outside_radius, dp2px(60.0f));
        //文字大小
        progressTextSize = a.getDimension(R.styleable.CircleBar_progress_text_size, dp2px(14.0f));
        //线圈宽
        progressWidth = a.getDimension(R.styleable.CircleBar_progress_width, dp2px(10.0f));
        //已消耗的进度
        progress = a.getFloat(R.styleable.CircleBar_cb_progress, 50.0f);
        //最大进度
        maxProgress = a.getInt(R.styleable.CircleBar_cb_max_progress, 100);
        a.recycle();
        paint = new Paint();
        oval = new RectF();
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circlePoint = getWidth() / 2;
        //第一步:画背景(即内层圆)
        paint.setColor(insideColor);//设置圆的颜色
        paint.setStyle(Paint.Style.STROKE);//设置空心
        paint.setStrokeWidth(progressWidth);//设置圆的宽度
        paint.setAntiAlias(true);//消除锯齿
        canvas.drawCircle(circlePoint, circlePoint, outsideRadius, paint);//画出圆

        //第二步:画进度(圆弧)
        paint.setColor(outsideColor);//设置进度的颜色
        //用于定义的圆弧的形状和大小的界限
        oval.left = circlePoint - outsideRadius;
        oval.top = circlePoint - outsideRadius;
        oval.right = circlePoint + outsideRadius;
        oval.bottom = circlePoint + outsideRadius;
        //根据进度画圆弧
        canvas.drawArc(oval, 270.0f, 360 * (progress / maxProgress), false, paint);

        //第三步:画圆环内百分比文字
        paint.setColor(progressTextColor);
        paint.setTextSize(progressTextSize);
        paint.setStrokeWidth(0);
        //圆环内文字
        String progressText = getProgressText();
        paint.getTextBounds(progressText, 0, progressText.length(), rect);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  //获得文字的基准线
        canvas.drawText(progressText, (getMeasuredWidth() >> 1) - (rect.width() >> 1), baseline, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) ((2 * outsideRadius) + progressWidth);
        }
        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) ((2 * outsideRadius) + progressWidth);
        }
        setMeasuredDimension(width, height);
    }

    //中间的进度百分比
    private String getProgressText() {
        return (int) ((progress / maxProgress) * 100) + "%";
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.maxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return progress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        this.progress = progress;
        postInvalidate();
    }

    private ValueAnimator animator;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (animator == null) {
                    animator = ObjectAnimator.ofFloat(0, maxProgress);
                    animator.addUpdateListener(animation -> {
                        float value = (float) animation.getAnimatedValue();
                        if (value > 100) {
                            progress = 100;
                            animator.cancel();
                            animator = null;
                            if (listener != null) {
                                listener.onPressed(false);
                            }
                        } else {
                            progress = value;
                        }
                        postInvalidate();
                    });
                    animator.setDuration(2000);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.start();
                    if (listener != null) {
                        listener.onPressed(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                animator.cancel();
                animator = null;
                if (listener != null) {
                    listener.onPressed(false);
                }
                break;
        }
        return true;
    }

    private float density = -1;

    private float dp2px(float dp) {
        if (density == -1) {
            density = getContext().getResources().getDisplayMetrics().density;
        }
        return dp * density + 0.5f;
    }

    private pressedListener listener;

    /**
     * 按住
     */
    public interface pressedListener {
        void onPressed(boolean pressed);
    }

    public void setListener(pressedListener listener) {
        this.listener = listener;
    }
}