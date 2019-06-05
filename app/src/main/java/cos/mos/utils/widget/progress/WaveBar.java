package cos.mos.utils.widget.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import cos.mos.toolkit.java.UText;


/**
 * @Description: 指定形状的水波进度
 * @Author: Kosmos
 * @Date: 2019.06.03 20:30
 * @Email: KosmoSakura@gmail.com
 * 基于fanrunqi的WaveProgressView修改
 * https://github.com/piratch/WaveProgressView
 */
public class WaveBar extends View {
    private int width, height;//控件宽高
    private Path path;
    private Paint paintDst;
    private int waveColor = Color.RED;
    private int textColor = Color.WHITE;
    private String currentText = "";

    private static final float waveRadius = 100f;//波半径
    private int speed = 30;//波浪的上下震动的速度(值越大，震动的越小)
    private float textSize = 41f;
    private float maxProgress = 100f;
    private float progress = 0f;
    private float CurY;//剩余总高度
    private float distance = 0;
    private Handler delivery = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            delivery.postDelayed(runnable, 10);
        }
    };


    public WaveBar(Context context) {
        this(context, null, 0);
    }

    public WaveBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        build();
    }


    /**
     * 叠加方式：
     * Paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))
     * ---------------------------------
     * 1.ADD:饱和相加,对图像饱和度进行相加
     * 2.CLEAR:清除图像
     * 3.DARKEN:变暗,较深的颜色覆盖较浅的颜色，若两者深浅程度相同则混合
     * 5.LIGHTEN:变亮，与DARKEN相反，DARKEN和LIGHTEN生成的图像结果与Android对颜色值深浅的定义有关
     * 6.MULTIPLY:正片叠底像素颜色值=【上层图】素颜色值 x 【下层图】素颜色值 ÷ 255
     * 7.OVERLAY:叠加
     * 8.SCREEN:滤色，色调均和,保留两图中较白的部分，遮盖较暗的部分
     * 9.XOR:两图相交处之外绘制它们，相交处受对应alpha和色值影响，如果:不透明=>相交处不绘制
     * <p>
     * 1.DST:只显示【下层图】
     * 2.DST_OVER:将【下层图】 放在【上层图】上方
     * 3.DST_ATOP:两图相交处绘制【下层图】，不相交处绘制【上层图】，效果受两图alpha影响
     * 5.DST_IN:只在两图相交处绘制【下层图】，效果受【上层图】对应地方alpha影响
     * 6.DST_OUT:只在两图不相交处绘制【下层图】,相交处根据【上层图】alpha进行过滤,【上层图】:不透明=>完全过滤,全透明=>不过滤
     * <p>
     * 1.SRC:只显示【上层图】
     * 2.SRC_ATOP:两图相交处绘制【上层图】,不相交处绘制【下层图】,效果受两图alpha影响
     * 3.SRC_IN:只在两图相交处绘制【上层图】
     * 4.SRC_OUT:只在两图不相交处绘制【上层图】，相交处根据【下层图】的对应地方的alpha进行过滤，【下层图】:不透明=>完全过滤,全透明=>不过滤
     * 5.SRC_OVER:将【上层图】放在【下层图】上方
     * ===========================================
     * setLayerType():硬件加速
     * 1.LAYER_TYPE_SOFTWARE
     * * 1.开了一个buffer，把View画到这个buffer上面去
     * * 2.渲染到Bitmap（无论硬件加速是否打开，都会有一张Bitmap（software layer）
     * * 3.优点：在进行动画，使用software可以只画一次View树，很省。
     * * 4.缺点：硬件加速打开时，更耗时（因为bmp渲染完后，还要渲染到hardware layer上
     * 2.LAYER_TYPE_HARDWARE
     * * 1.开了一个buffer，把View画到这个buffer上面去
     * * 2.硬件加速关闭时，作用同software。
     * * 3.硬件加速打开时，会在FBO（Framebuffer Object）上面做渲染，在进行动画时，View树也只需要画一次。
     * 3.LAYER_TYPE_NONE
     * * 不为这个View树建立单独的layer
     */
    public void build() {
        progress = 0;
        path = new Path();
        paintDst = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDst.setAntiAlias(true);
        paintDst.setDither(true);
        paintDst.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paintDst.setTextAlign(Paint.Align.CENTER);
        paintDst.setStyle(Paint.Style.FILL);
        paintDst.setTextSize(textSize);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setWillNotDraw(true);//一开始不要onDraw()
        delivery.postDelayed(runnable, 100);//自循环
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        CurY = height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();//保存当前的状态
        float CurMidY = height * (maxProgress - progress) / maxProgress;
        if (CurY > CurMidY) {
            CurY -= (CurY - CurMidY) / 10;
        }

        path.reset();
        path.moveTo(0, CurY);
        int waveNum = width / ((int) waveRadius * 2) + 1;
        int multiplier = 0;
        for (int i = 0; i < waveNum; i++) {
            //二阶贝塞尔曲线
            path.quadTo(waveRadius * (multiplier + 1) - distance, CurY - waveRadius / 2,
                waveRadius * (multiplier + 2) - distance, CurY);
            path.quadTo(waveRadius * (multiplier + 3) - distance, CurY + waveRadius / 2,
                waveRadius * (multiplier + 4) - distance, CurY);
            multiplier += 4;
        }
        distance += waveRadius / speed;
        distance %= waveRadius * 4;
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        paintDst.setColor(waveColor);
        canvas.drawPath(path, paintDst);
        canvas.restore();//取出原来所保存的状态

        if (!UText.isEmpty(currentText)) {
            paintDst.setColor(textColor);
            canvas.drawText(currentText, width >> 1, height >> 1, paintDst);
        }
    }

    /**
     * 弃用的方案
     */
    private void createImage() {
        Drawable drawable = getBackground();
        Bitmap backgroundBitmap = null;
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                backgroundBitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                backgroundBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(backgroundBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
        }
        Bitmap finalBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(finalBmp);
        if (backgroundBitmap != null) {
            //缩放到控件大小
            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, width, height, false);
            localCanvas.drawBitmap(backgroundBitmap, 0, 0, paintDst);
        }
    }

    public void setProgress(float progress, String currentText) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        this.progress = progress;
        this.currentText = currentText;
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        setProgress(progress, (int) ((progress / maxProgress) * 1000f) / 10f + "%");
    }

    public WaveBar setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    public WaveBar setText(int textColor, float textSize) {
        this.textColor = textColor;
        this.textSize = textSize;
        return this;
    }

    public WaveBar setWaveColor(int waveColor) {
        this.waveColor = waveColor;
        return this;
    }

    public WaveBar setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public void clear() {
        progress = 0;
        distance = 0;
        CurY = height;
    }
}