package cos.mos.utils.widget.special_image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import cos.mos.utils.R;

/**
 * @Description 圆角&比例尺寸
 * @Author Kosmos
 * @Date 2018年06月23日 22:21
 * @Email KosmoSakura@gmail.com
 * @Tip 博客地址：https://blog.csdn.net/zull_kos_mos/article/details/80788701
 */
public class KAutoImage extends android.support.v7.widget.AppCompatImageView {
    /**
     * 长宽比例:
     * 默认0，（小于0）不按比例绘制
     * 大于0，根据参考边按照比例设置长宽
     */
    private float ratio = 0;//长宽比例
    private int referEdge = 1;//比例参考边：1-width，2-height,默认：1
    private float radius = 0;//圆角弧度
    private int ratioW, ratioH;
    private final Matrix matrix = new Matrix();
    private Bitmap bmp;
    private Paint bmpPaint;
    private Path path = new Path();
    private RectF rectF = new RectF();

    public KAutoImage(Context context) {
        super(context);
        initViews();
    }

    public KAutoImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KAutoImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KAutoImage, defStyleAttr, 0);
        ratio = typedArray.getFloat(R.styleable.KAutoImage_cos_ratio, 1);
        referEdge = typedArray.getInt(R.styleable.KAutoImage_cos_refer, 1);
        radius = typedArray.getDimensionPixelSize(R.styleable.KAutoImage_cos_radius, 0);
        typedArray.recycle();
        initViews();
    }

    private void initViews() {
        bmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        bmpPaint.setStyle(Paint.Style.FILL);
        bmpPaint.setAntiAlias(true);
//        ULog.d("比例：" + ratio + ",参考边：" + (referEdge == 1 ? "宽" : "高")  + "，弧度：" + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio > 0) {
            //参考边为：宽
            if (referEdge == 1) {
                ratioW = MeasureSpec.getSize(widthMeasureSpec);
                ratioH = (int) (ratioW * ratio);

            } else {
                ratioH = MeasureSpec.getSize(heightMeasureSpec);
                ratioW = (int) (ratioH * ratio);
            }
            super.onMeasure(MeasureSpec.makeMeasureSpec(ratioW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(ratioH, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
//        ULog.v("宽高1：ratioW=" + ratioW + ",ratioH=" + ratioH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.top = 0;
        rectF.left = 0;
        rectF.right = getWidth(); // 宽度
        rectF.bottom = getHeight(); // 高度
        setBitmapShader();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        bmp = getBitmapFromDrawable(getDrawable());
        setBitmapShader();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        bmp = getBitmapFromDrawable(drawable);
        setBitmapShader();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (radius > 0 && scaleType != ScaleType.CENTER_CROP) {
            throw new IllegalArgumentException("设置圆弧图片的时候，缩放类型最好是：CENTER_CROP");
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (bmp != null) {
            path.reset();
            path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
            canvas.drawPath(path, bmpPaint);
        }
    }


    private void setBitmapShader() {
        if (bmpPaint == null) {
            return;
        }
        if (bmp == null) {
            invalidate();
            return;
        }
        BitmapShader bmpShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bmpPaint.setShader(bmpShader);

        matrix.set(null);//固定为CENTER_CROP
        // 缩放
        float scale = Math.max(getWidth() * 1f / bmp.getWidth(), getHeight() * 1f / bmp.getHeight());
        //居中
        float dx = (getWidth() - bmp.getWidth() * scale) / 2;
        float dy = (getHeight() - bmp.getHeight() * scale) / 2;
        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);
        bmpShader.setLocalMatrix(matrix);
        invalidate();//刷新
    }

    /**
     * drawable转换成bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
