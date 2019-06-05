package cos.mos.utils.widget.progress;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Description: 指纹扫描
 * @Author: Kosmos
 * @Date: 2019.06.03 20:30
 * @Email: KosmoSakura@gmail.com
 */
public class ScanningBar extends View implements View.OnTouchListener {
    private Paint paintDst, paint;
    private int width, height;
    private Bitmap bmpFront, bmpBar, bmpMask;//底图，动图，扫描条
    private int idsFront, idsBar, idsMask;
    private Resources resources;
    private int curY;
    private boolean lock, done, interrupt;
    private Handler delivery = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!interrupt) {
                curY += 10;
                invalidate();
                delivery.postDelayed(runnable, 50);
            }
        }
    };

    public ScanningBar(Context context) {
        this(context, null);
    }

    public ScanningBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanningBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        initial();
    }

    private void initial() {
        curY = -1;
        paintDst = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDst.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paintDst.setAntiAlias(true);
        paintDst.setDither(true);

        paint = new Paint();
        setLayerType(LAYER_TYPE_HARDWARE, null);
        lock = done = interrupt = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImages(int idsFront, int idsBar, int idsMask) {
        this.idsFront = idsFront;
        this.idsBar = idsBar;
        this.idsMask = idsMask;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (resources == null) {
            resources = getResources();
        }
        if (bmpFront == null) {
            bmpFront = BitmapFactory.decodeResource(resources, idsFront);
            bmpFront = Bitmap.createScaledBitmap(bmpFront, width, height, true);
        }
        if (bmpBar == null) {
            bmpBar = BitmapFactory.decodeResource(resources, idsBar);
            bmpBar = Bitmap.createScaledBitmap(bmpBar, width, height, true);
        }
        if (bmpMask == null) {
            bmpMask = BitmapFactory.decodeResource(resources, idsMask);
            bmpMask = Bitmap.createScaledBitmap(bmpMask, width, 2 * height, true);
        }
        if (curY<0) {
            canvas.drawBitmap(bmpFront, 0, 0, paint);
            canvas.drawBitmap(bmpMask, 0, curY - height, paintDst);
        } else if (curY < height){
            canvas.drawBitmap(bmpFront, 0, 0, paint);
            canvas.drawBitmap(bmpBar, 0, curY, paint);
            canvas.drawBitmap(bmpMask, 0, curY - height, paintDst);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (done) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interrupt = false;
                if (!lock) {
                    lock = true;
                    delivery.postDelayed(runnable, 100);
                }
                break;
            case MotionEvent.ACTION_UP:
                lock = false;
                interrupt = true;
                if (curY > height) {
                    done = true;
                    if (listener != null) {
                        listener.state(true);
                    }
                } else {
                    curY = -1;
                    done = false;
                    invalidate();
                    if (listener != null) {
                        listener.state(false);
                    }
                }
                break;
        }
        return true;
    }

    public interface StateListener {
        void state(boolean done);
    }

    private StateListener listener;

    public void setOnStateListener(StateListener listener) {
        this.listener = listener;
    }
}
