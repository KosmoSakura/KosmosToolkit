package cos.mos.utils.widget.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;

/**
 * @Description: 可以拖动的柱状图
 * @Author: Kosmos lbing
 * @Date: 2019.06.28 15:44
 * @Email: KosmoSakura@gmail.com
 * @link https://github.com/yunzheyue/honrizontalBar
 */
public class ColumnarChartScroller extends View {
    private float barInterval;
    private float barWidth;
    private float top_text_size;
    private float bottom_text_size;
    private int bar_color;
    private int bottom_line_color;
    private int top_text_color;
    private int bottom_text_color;
    private float threshold;//虚线阈值
    private int threshold_color;//虚线阈值

    private RectF barRect;
    private Rect textRect;
    private Paint mainPaint;//主笔
    private DashPathEffect dotted;//虚线特效
    private List<ColumnarChartScrollerBean> innerData = new ArrayList<>();
    private float paddingTop;
    private float paddingLeft;
    private float paddingBottom;
    private float paddingRight;
    private int defaultHeight = (int) UScreen.dp2px(180);
    private int bottom_view_height = (int) UScreen.dp2px(30);
    private int top_text_height = (int) UScreen.dp2px(30);
    private float scaleTimes = 1;
    private float lastX = 0;
    private int measureWidth = 0;
    //这是最初的的位置
    private float startOriganalX = 0;
    private HorizontalScrollRunnable horizontalScrollRunnable;
    //临时滑动的距离
    private float tempLength = 0;
    private long startTime = 0;
    private boolean isFling = false;
    private float dispatchTouchX = 0;
    private float dispatchTouchY = 0;
    //是否到达边界
    private boolean isBoundary = false;
    private boolean isMove = false;

    public ColumnarChartScroller(Context context) {
        this(context, null);
    }

    public ColumnarChartScroller(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnarChartScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColumnarChartScroller);
        bar_color = typedArray.getColor(R.styleable.ColumnarChartScroller_bar_color, Color.parseColor("#f9e5c9"));
        top_text_color = typedArray.getColor(R.styleable.ColumnarChartScroller_top_text_color, Color.parseColor("#00ff00"));
        bottom_text_color = typedArray.getColor(R.styleable.ColumnarChartScroller_bottom_text_color, Color.parseColor("#0000ff"));
        bottom_line_color = typedArray.getColor(R.styleable.ColumnarChartScroller_bottom_line_color, Color.parseColor("#000000"));
        threshold_color = typedArray.getColor(R.styleable.ColumnarChartScroller_threshold_color, Color.parseColor("#e5e8ec"));
        barInterval = typedArray.getDimension(R.styleable.ColumnarChartScroller_bar_interval, UScreen.dp2px(18));
        barWidth = typedArray.getDimension(R.styleable.ColumnarChartScroller_bar_width, UScreen.dp2px(18));
        top_text_size = typedArray.getDimension(R.styleable.ColumnarChartScroller_top_text_size, UScreen.sp2px(8));
        bottom_text_size = typedArray.getDimension(R.styleable.ColumnarChartScroller_bottom_text_size, UScreen.sp2px(8));
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mainPaint = new Paint();
        mainPaint.setStrokeCap(Paint.Cap.ROUND);
        mainPaint.setStyle(Paint.Style.FILL);
        mainPaint.setAntiAlias(true);
        mainPaint.setDither(true);
        //设置底部线的宽度
        mainPaint.setStrokeWidth(UScreen.dp2px(0.6f));
        dotted = new DashPathEffect(new float[]{2, 4, 2, 4}, 0);
        barRect = new RectF();
        textRect = new Rect();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setBarChartData(List<ColumnarChartScrollerBean> datas, float threshold) {
        this.innerData.clear();
        if (datas != null) {
            this.innerData.addAll(datas);
        }
        this.threshold = threshold;
        scaleTimes = (float) getMaxValue() / (float) (defaultHeight - bottom_view_height - top_text_height);
        invalidate();
    }

    private int getMaxValue() {
        int defaultValue = 0;
        if (innerData.size() > 0) {
            defaultValue = innerData.get(0).getCount();
            for (int i = 0; i < innerData.size(); i++) {
                if (innerData.get(i).getCount() > defaultValue) {
                    defaultValue = innerData.get(i).getCount();
                }
            }
        }
        return defaultValue;
    }

    //进行滑动的边界处理
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int dispatchCurrX = (int) ev.getX();
        int dispatchCurrY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //父容器不拦截点击事件，子控件拦截点击事件。如果不设置为true,外层会直接拦截，从而导致motionEvent为cancle
                getParent().requestDisallowInterceptTouchEvent(true);
                dispatchTouchX = getX();
                dispatchTouchY = getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = dispatchCurrX - dispatchTouchX;
                float deltaY = dispatchCurrY - dispatchTouchY;
                if (Math.abs(deltaY) - Math.abs(deltaX) > 0) {//竖直滑动的父容器拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                //这是向右滑动，如果是滑动到边界，那么就让父容器进行拦截
                if ((dispatchCurrX - dispatchTouchX) > 0 && startOriganalX == 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if ((dispatchCurrX - dispatchTouchX) < 0 && startOriganalX == -getMoveLength()) {//这是向右滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        dispatchTouchX = dispatchCurrX;
        dispatchTouchY = dispatchCurrY;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isBoundary = false;
        isMove = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                startTime = System.currentTimeMillis();
                //当点击的时候，判断如果是在fling的效果的时候，就停止快速滑动
                if (isFling) {
                    removeCallbacks(horizontalScrollRunnable);
                    isFling = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float currX = event.getX();
                startOriganalX += currX - lastX;
                //这是向右滑动
                if ((currX - lastX) > 0) {
                    if (startOriganalX > 0) {
                        startOriganalX = 0;
                        isBoundary = true;
                    }
                } else {
                    //这是向右滑动
                    if (-startOriganalX > getMoveLength()) {
                        startOriganalX = -getMoveLength();
                        isBoundary = true;
                    }
                }
                tempLength = currX - lastX;
                //如果数据量少，根本没有充满横屏，就没必要重新绘制，
                if (measureWidth < innerData.size() * (barWidth + barInterval)) {
                    invalidate();
                }

                lastX = currX;
                break;
            case MotionEvent.ACTION_UP:
                long endTime = System.currentTimeMillis();
                //计算猛滑动的速度，如果是大于某个值，并且数据的长度大于整个屏幕的长度，那么就允许有flIng后逐渐停止的效果
                float speed = tempLength / (endTime - startTime) * 1000;
                if (Math.abs(speed) > 100 && !isFling && measureWidth < innerData.size() * (barWidth + barInterval)) {
                    this.post(horizontalScrollRunnable = new HorizontalScrollRunnable(speed));
                }
                isMove = false;
                performClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                isMove = false;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = innerData.size();
        //坐标轴
        mainPaint.setPathEffect(null);
        mainPaint.setColor(bottom_line_color);
        float stopX;
        float stopY = defaultHeight - bottom_view_height;
        if (size < 8) {
            stopX = 9 * (barWidth + barInterval);
        } else {
            stopX = size * (barWidth + barInterval);
        }
        //Y轴
        canvas.drawLine(0, 0, 0, stopY, mainPaint);
        //X轴
        canvas.drawLine(0, stopY, stopX, stopY, mainPaint);
        if (size <= 0) {
            //如果没有数据 绘制loading...
            String text = "loading...";
            mainPaint.setTextSize(bottom_text_size);
            mainPaint.setColor(bottom_text_color);
            float textWidth = mainPaint.measureText(text);
            canvas.drawText(text, (measureWidth >> 1) - textWidth / 2, (defaultHeight >> 1) - 10, mainPaint);
        } else {
            float startX = paddingLeft + startOriganalX;
            for (int i = 0; i < size; i++) {
                ColumnarChartScrollerBean bean = innerData.get(i);
                int count = bean.getCount();
                String text = bean.getBottomText();

                float startY, thresholdY;
                if (scaleTimes == 0) {
                    startY = thresholdY = stopY;
                } else {
                    thresholdY = stopY - threshold / scaleTimes;
                    startY = stopY - count / scaleTimes;
                }
                //阈值线
                mainPaint.setColor(threshold_color);
                mainPaint.setPathEffect(dotted);
                canvas.drawLine(paddingLeft, thresholdY, stopX, thresholdY, mainPaint);

                //绘制bar
                barRect.left = startX;
                barRect.top = startY;
                barRect.right = startX + barWidth;
                barRect.bottom = stopY;
                mainPaint.setColor(bar_color);
                canvas.drawRect(barRect, mainPaint);
//                canvas.drawRoundRect(barRect, 30f, 30f, mainPaint);

                //绘制bar上的文字
                float topTextWidth = mainPaint.measureText(String.valueOf(count));
                float textStartX = startX + barWidth / 2 - topTextWidth / 2;
                float textStartY = startY - 10;
                mainPaint.setTextSize(top_text_size);
                mainPaint.setColor(top_text_color);
                canvas.drawText(String.valueOf(count), textStartX, textStartY, mainPaint);

                //绘制底部的文字
                float bottomTextWidth = mainPaint.measureText(text);
                float bottomStartX = startX + barWidth / 2 - bottomTextWidth / 2;
                mainPaint.getTextBounds(text, 0, text.length(), textRect);
                //textRect.height()是获取文本的高度;
                float bottomStartY = stopY + 10 + textRect.height();
                mainPaint.setTextSize(bottom_text_size);
                mainPaint.setColor(bottom_text_color);
                canvas.drawText(text, bottomStartX, bottomStartY, mainPaint);

                startX = startX + barWidth + barInterval;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = width = widthSize;
        } else {
            width = getAndroiodScreenProperty().get(0);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            defaultHeight = height = heightSize;
        } else {
            height = defaultHeight;
        }
        setMeasuredDimension(width, height);
        paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingRight = getPaddingRight();
    }

    private ArrayList<Integer> getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(screenWidth);
        integers.add(screenHeight);
        return integers;
    }

    private float getMoveLength() {
        return (barWidth + barInterval) * innerData.size() - measureWidth;
    }

    public boolean isBoundary() {
        return isBoundary;
    }

    public boolean isMove() {
        return isMove;
    }

    private class HorizontalScrollRunnable implements Runnable {
        private float speed;

        HorizontalScrollRunnable(float speed) {
            this.speed = speed;
        }

        @Override
        public void run() {
            if (Math.abs(speed) < 30) {
                isFling = false;
                return;
            }
            isFling = true;
            startOriganalX += speed / 15;
            speed = speed / 1.15f;
            //这是向右滑动
            if ((speed) > 0) {
                if (startOriganalX > 0) {
                    startOriganalX = 0;
                }
            } else {
                //这是向右滑动
                if (-startOriganalX > getMoveLength()) {
                    startOriganalX = -getMoveLength();
                }
            }
            postDelayed(this, 20);
            invalidate();
        }
    }
}


