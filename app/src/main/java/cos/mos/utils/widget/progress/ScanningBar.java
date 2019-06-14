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
 * @eg: 2019.6.14 添加功能：反向扫描
 */
public class ScanningBar extends View implements View.OnTouchListener {
    private Paint paintDst, paint;
    private int width, height;
    private Bitmap bmpFront, bmpBar, bmpMask;//底图，动图，扫描条
    private int idsFront, idsBar, idsMask;
    private Resources resources;
    private int curY, offset = 0;//扫描线偏移值
    private boolean lock, done, interrupt, reverse;//反向
    private Handler delivery = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!interrupt) {
                curY += 2;
                if (curY > (height - offset) && !done && listener != null) {
                    listener.state(0);
                } else {
                    delivery.postDelayed(runnable, 10);
                }
                invalidate();
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
        paintDst = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDst.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paintDst.setAntiAlias(true);
        paintDst.setDither(true);

        curY = -1;
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

    public void setImages(boolean reverse, int idsFront, int idsBar, int idsMask) {
        this.reverse = reverse;
        this.idsFront = idsFront;
        this.idsBar = idsBar;
        this.idsMask = idsMask;
        bmpFront = null;
        bmpBar = null;
        bmpMask = null;
    }

    private void initCurY() {
        if (reverse) {
            curY = height;
        } else {
            curY = -1;
        }
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
            bmpBar = Bitmap.createScaledBitmap(bmpBar, width - offset * 2, height - offset * 2, true);
        }
        if (bmpMask == null) {
            bmpMask = BitmapFactory.decodeResource(resources, idsMask);
            bmpMask = Bitmap.createScaledBitmap(bmpMask, width, 2 * height, true);
        }

        canvas.drawBitmap(bmpFront, 0, 0, paint);
        if (reverse) {
            if (curY > offset && curY < (height - offset)) {
                canvas.drawBitmap(bmpBar, offset, -curY, paint);
            }
            canvas.drawBitmap(bmpMask, 0, -curY, paintDst);
        } else {
            if (curY > offset && curY < (height - offset)) {
                canvas.drawBitmap(bmpBar, offset, curY, paint);
            }
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
                if (curY > (height - offset)) {
                    done = true;
                    if (listener != null) {
                        listener.state(1);
                    }
                } else {
                    curY = -1;
                    done = false;
                    invalidate();
                    if (listener != null) {
                        listener.state(-1);
                    }
                }
                break;
        }
        return true;
    }

    public interface StateListener {
        /**
         * @param done -1：失败，1：成功（手指松开），0：成功（手指未松开）
         */
        void state(int done);
    }

    private StateListener listener;

    public void setOnStateListener(StateListener listener) {
        this.listener = listener;
    }
}
