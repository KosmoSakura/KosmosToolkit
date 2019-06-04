package cos.mos.utils.widget.progress.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;


/**
 * @Description: 视频裁剪条
 * @Author: Kosmos
 * @Date: 2019.02.16 15:29
 * @Email: KosmoSakura@gmail.com
 */
public class VideoClipBar extends View {
    private int baseColor;
    private Bitmap bmpLeft, bmpRight, bmpTop;//图标的图片
    private final float BarHeight = UScreen.dp2px(10);//条条的宽度
    private final float minLength = UScreen.dp2px(5);//最小剪切长度
    private float lineStart = 0;//线（进度条） 开始的位置
    private float lineEnd = getWidth();//线（进度条）的结束位置
    private boolean isLeftMoving, isRightMoving;//左，右游标是否在动
    private float bmpHeight, bmpWidth;//图标（游标） 尺寸
    private float xLeft, xRight, xTop;//图标所在X轴的位置
    private float yTop, yBottom;//上下图标的y轴值

    private Paint paint;
    private float l, r, t, b;

    public VideoClipBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public VideoClipBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveClipBar, 0, 0);
        baseColor = ta.getInteger(R.styleable.WaveClipBar_waveBaseColor, 0);

        bmpLeft = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_left);
        bmpRight = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_right);
        bmpTop = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_point);
        //游标图片的真实高度 之后通过缩放比例可以把图片设置成想要的大小
        bmpHeight = bmpLeft.getHeight();
        bmpWidth = bmpLeft.getWidth();
        ta.recycle();
    }

    protected void onDraw(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
        }
        //游标
        canvas.drawBitmap(bmpLeft, xLeft, yBottom, paint);
        canvas.drawBitmap(bmpRight, xRight, yBottom, paint);
        canvas.drawBitmap(bmpTop, xTop, yTop, paint);
        //进度矩形
//        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(baseColor);
        canvas.drawRect(l, t, r, b, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        //有效宽度
        lineStart = bmpWidth;
        lineEnd = w - bmpWidth;
        //进度矩形
        l = lineStart;
        r = lineEnd;
        t = bmpHeight;
        b = t + BarHeight;
        //上下图标的y轴值
        yTop = 0;
        yBottom = b;
        //初始化 游标位置
        xLeft = 0;
        xRight = lineEnd;
        xTop = bmpWidth / 2;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float nowX = event.getX();
        float nowY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下 在游标范围上
                boolean rightY = Math.abs(nowY - yBottom) < bmpHeight;
                //按下 在左边游标上
                boolean LSlide = Math.abs(nowX - xLeft) < bmpWidth;
                //按下 在右边游标上
                boolean RSlide = Math.abs(nowX - xRight) < bmpWidth;
                if (rightY && LSlide) {
                    isLeftMoving = true;
                    isRightMoving = false;
                } else if (rightY && RSlide) {
                    isRightMoving = true;
                    isLeftMoving = false;
                } else {
                    isLeftMoving = false;
                    isRightMoving = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //左边游标是运动状态
                if (isLeftMoving) {
                    //当前 X坐标在线上 且在右边游标的左边
                    if (nowX <= xRight - bmpWidth - minLength) {
                        xLeft = nowX;

                        if (xLeft < lineStart) {
                            xLeft = 0;
                        }
                        xTop = xLeft + bmpWidth / 2;
                        //更新进度
                        updateRange();
                        postInvalidate();
                    }
                } else if (isRightMoving) {
                    //当前 X坐标在线上 且在左边游标的右边
                    if (nowX >= xLeft + bmpWidth + minLength) {
                        xRight = nowX;
                        if (xRight > lineEnd) {
                            xRight = lineEnd;
                        }
                        xTop = xLeft + bmpWidth / 2;
                        //更新进度
                        updateRange();
                        postInvalidate();
                    }
                }
                break;
            //手指抬起
            case MotionEvent.ACTION_UP:
                isRightMoving = false;
                isLeftMoving = false;
                break;
        }
        return true;
    }

    private void updateRange() {
        //接口 实现值的传递
        if (listener != null) {
            listener.onRange((xLeft + bmpWidth - lineStart) / (lineEnd - lineStart),
                (xRight - lineStart) / (lineEnd - lineStart));
        }
    }

    /**
     * @param cursor 整体长度的百分比，作用域顶部游标
     * @apiNote -1为极左极右
     */
    public void setCursor(float cursor) {
//        KtLog.commonE("【百分比：" + cursor * (lineEnd - lineStart) + "】" +
//            "\n图宽：" + bmpWidth / 2 + "\n【LR：" + xLeft + "-" + xRight);
        xTop = cursor * (lineEnd - lineStart) + bmpWidth;
        if (xTop < (xLeft + bmpWidth)) {
            xTop = xLeft + bmpWidth / 2;
        } else if (xTop > xRight) {
            xTop = xRight - bmpWidth / 2;
            if (listener != null) {
                listener.onRight();
            }
        } else {
            xTop = xTop - bmpWidth / 2;
        }
//        KtLog.commonI("cursor:" + cursor + ",xTop:" + xTop + "【】【】LR：" + xLeft + "++" + xRight);
        invalidate();
    }

    public void reset() {
        //初始化 游标位置
        xLeft = 0;
        xRight = lineEnd;
        xTop = bmpWidth / 2;
        invalidate();
    }

    public interface RangeListener {
        /**
         * @param pLeft  左游标百分比
         * @param pRight 右游标百分比
         */
        void onRange(float pLeft, float pRight);

        /**
         * 当播放指针到达右边游标
         */
        void onRight();
    }

    private RangeListener listener;

    public void setonRangeListener(RangeListener listener) {
        this.listener = listener;
    }
}


