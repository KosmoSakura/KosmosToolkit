package cos.mos.utils.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;


/**
 * @Description: 螺旋线 蒙版心跳
 * @Author: Kosmos
 * @Date: 2019.06.03 20:30
 * @Email: KosmoSakura@gmail.com
 */
public class LineBar extends View {
    private float width, height;
    private float x, y;
    private Path path;
    private static final float distance = 10;
    private int pointor;
    private Paint paintDst;
    private Handler delivery = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (x > width && y > height) {
                if (listener != null) {
                    listener.complete();
                }
            } else {
                invalidate();
                delivery.postDelayed(runnable, 10);
            }
        }
    };


    public LineBar(Context context) {
        this(context, null, 0);
    }

    public LineBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial();
    }


    private void initial() {
        paintDst = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDst.setAntiAlias(true);
        paintDst.setDither(true);
        paintDst.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paintDst.setStyle(Paint.Style.STROKE);
        paintDst.setColor(Color.WHITE);
        paintDst.setStrokeWidth(3);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setWillNotDraw(true);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void start() {
        path.reset();
        path.moveTo(0, 0);
        x = y = 0;
        pointor = 0;
        delivery.postDelayed(runnable, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        drwaLine(canvas);
        canvas.restore();
    }

    private void drwaLine(Canvas canvas) {
        switch (pointor) {
            case 0:
                x += distance;
                path.lineTo(x, 0);
                break;
            case 1:
                y += distance;
                path.lineTo(0, y);
                break;
            case 2:
                path.lineTo(0, height - y);
                break;
            case 3:
                path.lineTo(x, height);
                break;
            case 4:
                path.lineTo(width - x, height);
                break;
            case 5:
                path.lineTo(width, height - y);
                break;
            case 6:
                path.lineTo(width, y);
                break;
            case 7:
                path.lineTo(width - x, 0);
                break;
        }
        canvas.drawPath(path, paintDst);
        if (pointor < 8) {
            pointor++;
        } else {
            pointor = 0;
        }
    }

    public interface CompleteListener {
        void complete();
    }

    private CompleteListener listener;

    public void setCompleteListener(CompleteListener listener) {
        this.listener = listener;
    }

}