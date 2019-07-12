package cos.mos.utils.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cos.mos.utils.R;


/**
 * @Description: 音频播放条
 * @Author: Kosmos
 * @Date: 2019.05.28 16:38
 * @Email: KosmoSakura@gmail.com
 */
public class AudioBar extends View {
    private int baseColor;//底色
    private int coverColor;//覆盖色
    private float padding;//间距
    private float lineWith;//线宽
    private float maxProgress;//最大进度
    private float progress;//当前进度

    private Paint paint;
    private float w, h;

    public AudioBar(Context context) {
        this(context, null);
    }

    public AudioBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AudioBar, defStyleAttr, 0);
        //底色
        baseColor = a.getColor(R.styleable.AudioBar_base_color, Color.parseColor("#ffffff"));
        //覆盖色
        coverColor = a.getColor(R.styleable.AudioBar_cover_color, Color.parseColor("#ff2b81"));
        //间距
        padding = a.getDimension(R.styleable.AudioBar_line_padding, 30f);
        //线宽
        lineWith = a.getDimension(R.styleable.AudioBar_line_with, 10f);
        //已消耗的进度
        progress = a.getFloat(R.styleable.AudioBar_progress, 0);
        //最大进度
        maxProgress = a.getFloat(R.styleable.AudioBar_max_progress, 100);
        a.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineWith);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float lx = 0, ly = 0;
        int flag = 0;
        float ppg = progress * w / maxProgress;
        while (lx < w) {
            if (lx < ppg) {
                paint.setColor(coverColor);
            } else {
                paint.setColor(baseColor);
            }
            if (lx > ppg - padding / 2 && lx < ppg + padding / 2) {
                ly = h / 2;
                paint.setColor(coverColor);
            } else {
                //0 1 2 3 4 5 6
                switch (flag) {
                    case 0:
                    case 6:
                        ly = h / 6;
                        break;
                    case 1:
                    case 5:
                        ly = h / 5;
                        break;
                    case 2:
                    case 4:
                        ly = h / 4;
                        break;
                    case 3:
                        ly = h / 3;
                        break;
                }
            }
            canvas.drawLine(lx, (h - ly) / 2, lx, (h + ly) / 2, paint);
            flag++;
            if (flag > 7) {
                flag = 0;
            }
            lx += padding;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
    }


    public synchronized void setMaxProgress(float maxProgress) {
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
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        this.progress = progress;
        postInvalidate();
    }
}