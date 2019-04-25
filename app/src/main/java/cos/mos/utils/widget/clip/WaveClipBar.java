package cos.mos.utils.widget.clip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import cos.mos.toolkit.java.UUnit;
import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;


/**
 * @Description: 波形裁剪条
 * @Author: Kosmos
 * @Date: 2019.02.16 15:29
 * @Email: KosmoSakura@gmail.com
 */
public class WaveClipBar extends View {
    public static final int READ_FRAME_COUNT = 100;
    private final float offset5 = UScreen.dp2px(5);//偏移量
    private final float offset10 = UScreen.dp2px(10);//偏移量
    private final float offset20 = UScreen.dp2px(20);//偏移量
    private final float offset30 = UScreen.dp2px(30);//偏移量
    private InputStream inputStream;
    private Vector<Float> yAxis = new Vector<>(0);
    private boolean firstDraw = true;
    private int clipColor;
    private int baseColor;

    private Bitmap bmpLeft;//左边图标的图片
    private Bitmap bmpRight;//右边图标 的图片
    private boolean isLeftMoving, isRightMoving;//左，右游标是否在动
    private float lineStart = 0;//线（进度条） 开始的位置
    private float lineEnd = getWidth();//线（进度条）的结束位置
    private float bmpHeight;//图标（游标） 高度
    private float bmpWidth;//图标（游标） 宽度
    private float xLeft, xRight;//图标所在X轴的位置
    private Paint paint;
    private float top, bottom;//顶边，底边位置
    private long duration;//总时长（毫秒
    private String[] times;
    private float[] scaleX;//刻度X轴坐标
    private float scaleCount;//刻度数
    private float cursorX;//播放进度

    public WaveClipBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public WaveClipBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveClipBar, 0, 0);
        clipColor = ta.getInteger(R.styleable.WaveClipBar_waveClipColor, 0);
        baseColor = ta.getInteger(R.styleable.WaveClipBar_waveBaseColor, 0);

        bmpLeft = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_right);
        bmpRight = BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_left);
        //游标图片的真实高度 之后通过缩放比例可以把图片设置成想要的大小
        bmpHeight = bmpLeft.getHeight();
        bmpWidth = bmpLeft.getWidth();
        ta.recycle();
    }

    /**
     * @param intStream 音频输入流
     * @param duration  总时长（毫秒
     * @param scale     刻度数
     */
    public void setAudio(InputStream intStream, long duration, int scale) {
        this.inputStream = intStream;
        this.duration = duration;
        this.scaleCount = scale;
        times = new String[scale];
        scaleX = new float[scale];
        invalidate();
    }

    /**
     * @param cursor 整体长度的百分比，作用域顶部游标
     * @apiNote -1为极左极右
     */
    public void setCursor(float cursor) {
        cursorX = cursor * (lineEnd - lineStart);
        if (cursorX < lineStart) {
            cursorX = lineStart;
        }
        if (cursorX > lineEnd) {
            cursorX = lineEnd;
        }
        invalidate();
    }

    private class CalculateYAxisPoints extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void[] params) {
            try {
                readWavFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WavFileException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            invalidate();
        }
    }


    /**
     * @param start     路径的起点
     * @param end       路径的终点
     * @param paintPath 画笔
     * @param canvas    画布
     * @apiNote 使用yAxis数组从头到尾绘制路径。
     */
    private void drawPath(int start, int end, Paint paintPath, Canvas canvas) {
        Path path = new Path();
        float viewHeightHalf = bottom / 2;
        for (; start <= end; start += 2) {
            if (start >= yAxis.size()) {
                break;
            }
            if (Math.floor(yAxis.get(start)) >= 1) {
                path.moveTo(start, viewHeightHalf - (yAxis.get(start)));
                path.lineTo(start, viewHeightHalf + (yAxis.get(start)));
            } else {
                path.moveTo(start, (float) (viewHeightHalf * 1.01));
                path.lineTo(start, (float) (viewHeightHalf * 0.99));
            }
        }
        path.close();
        canvas.drawPath(path, paintPath);
        path.reset();
    }


    /**
     * 读取波形
     */
    private void readWavFile() throws IOException, WavFileException {
        // 打开指定为第一个参数的wav文件
        if (inputStream == null) return;
        WavFile wavFile = WavFile.openWavFile(inputStream);
        // 获取wav文件中的音频通道数 bmpWidth
        int numChannels = wavFile.getNumChannels();
        // 创建一个100帧的缓冲区
        double[] buffer = new double[READ_FRAME_COUNT * numChannels];

        int framesRead;
        int samplesPerPixel = (int) (wavFile.getNumFrames() * numChannels) / (getMeasuredWidth());

        double avrg = 0;
        int overallCounter = 0;
        int pointsInAvrg = 0;
        float yPoint = 0;//y轴上的单个值
        double max = Double.MIN_VALUE;
        double maxLoc = Double.MIN_VALUE;
        do {
            //将帧读入缓冲区
            framesRead = wavFile.readFrames(buffer, READ_FRAME_COUNT);
            // 循环遍历帧并存储yAxis值
            if (framesRead != 1) {
                for (int s = 0; s < framesRead * numChannels; s += 1) {
                    if (buffer[s] > maxLoc) maxLoc = buffer[s];
                    //每256帧中获得最大值
                    if (overallCounter % 256 == 0) {
                        avrg += Math.abs(maxLoc);
                        maxLoc = Double.MIN_VALUE;
                        pointsInAvrg++;
                    }
                    //平均采样率
                    if (overallCounter % samplesPerPixel == 0) {
                        if (pointsInAvrg == 0)
                            yAxis.add((float) 0);
                        else {
                            yPoint = (float) (avrg / pointsInAvrg);
                            yAxis.add(yPoint);
                            if (yPoint > max) max = (yPoint);
                            pointsInAvrg = 0;
                            avrg = 0;
                        }
                    }
                    overallCounter++;
                }
            }
        } while (framesRead != 0);

        //相对于屏幕尺寸和填充的比率
        double ratio = ((getMeasuredHeight() / 2) - getPaddingTop() - getPaddingBottom()) / max;
        //值相对于比率标准化
        for (overallCounter = 0; overallCounter < yAxis.size(); overallCounter++) {
            yAxis.set(overallCounter, (float) (yAxis.get(overallCounter) * ratio));
        }
        //关流
        wavFile.close();
        if (listener != null) {
            listener.loadComplete();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        //线的有效位置
        lineStart = 0;
        lineEnd = w;
        //初始化 游标位置
        xLeft = cursorX = lineStart;
        xRight = lineEnd - bmpWidth;
        //选中矩形
        top = 0;
        bottom = h;
        for (int i = 0; i < scaleCount; i++) {
            if (i == 0) {
                times[i] = "0.0s";
                scaleX[i] = 0;
            } else {
                times[i] = fotmatMax((int) (1000 * (scaleCount - i)));
                scaleX[i] = i * (lineEnd - lineStart) / (scaleCount - 1) - offset30;
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private String fotmatMax(int digit) {
        return UUnit.sizeFormatbitTime(duration * 1f / digit);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(false);
            paint.setTextSize(20);
        }
        if (firstDraw) {
            firstDraw = false;
            new CalculateYAxisPoints().execute();
        }
        //绘制全部波形
        paint.setColor(baseColor);
        paint.setStyle(Paint.Style.STROKE);
        drawPath((int) lineStart, (int) lineEnd, paint, canvas);
        //绘制选中波形
        paint.setColor(clipColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(xLeft, top + bmpHeight, xRight + bmpWidth, bottom - bmpHeight, paint);

        canvas.drawBitmap(bmpLeft, xLeft, bottom - bmpHeight, paint);
        canvas.drawBitmap(bmpRight, xRight, bottom - bmpHeight, paint);

        //绘制5个刻度
        paint.setColor(Color.WHITE);
        for (int i = 1; i < scaleCount; i++) {
            canvas.drawLine(scaleX[i] + offset10, top,
                scaleX[i] + offset10, top + offset5, paint);
            canvas.drawText(times[i], scaleX[i], top + bmpHeight / 2, paint);
        }
        paint.setColor(Color.RED);
        canvas.drawLine(cursorX, top + bmpHeight, cursorX, bottom - bmpHeight, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float nowX = event.getX();
        float nowY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下 在游标范围上
                boolean rightY = Math.abs(nowY - bottom) < bmpHeight;
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
                    if (nowX <= xRight - bmpWidth) {
                        xLeft = nowX;
                        if (xLeft < lineStart) {
                            xLeft = lineStart;
                        }
                        cursorX = xLeft;
                        //更新进度
                        updateRange();
                        postInvalidate();
                    }
                } else if (isRightMoving) {
                    //当前 X坐标在线上 且在左边游标的右边
                    if (nowX >= xLeft + bmpWidth) {
                        xRight = nowX;
                        if (xRight > (lineEnd - bmpWidth)) {
                            xRight = lineEnd - bmpWidth;
                        }
                        cursorX = xLeft;
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
        //当前X轴坐标百分比
        if (listener != null) {
            listener.onRange((xLeft - lineStart) / (lineEnd - lineStart),
                (xRight + bmpWidth - lineStart) / (lineEnd - lineStart));
        }
    }

    /**
     * @param start 左游标位置百分比
     * @param end   右游标位置百分比
     * @apiNote -1为极左极右
     */
    public void setPoint(float start, float end) {
        xLeft = start < 0 ? lineStart : start * (lineEnd - lineStart);
        xRight = end < 0 ? lineEnd - bmpWidth : end * (lineEnd - lineStart) - bmpWidth;
        updateRange();
        invalidate();
    }


    /**
     * 百分比换为X轴坐标
     */
    private float computRangeBack(float durationNow) {
        return durationNow * (lineEnd - lineStart) + lineStart;
    }

    public interface RangeListener {
        /**
         * 左、右游标位置百分比
         */
        void onRange(float leftRange, float rightRange);

        void loadComplete();
    }

    private RangeListener listener;

    public void setonRangeListener(RangeListener listener) {
        this.listener = listener;
    }
}


