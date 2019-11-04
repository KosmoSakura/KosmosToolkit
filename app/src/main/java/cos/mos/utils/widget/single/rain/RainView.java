package cos.mos.utils.widget.single.rain;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import cos.mos.utils.R;


/**
 * @Description 粒子雨
 * @Author Kosmos
 * @Date 2017/11/28 002814:37
 * @Email KosmoSakura@gmail.com
 * */
public class RainView extends View {
    private ArrayList<RainItem> rainList = new ArrayList<>();
    private boolean running = true;
    private Thread thread;

    private int rainNum, rainLengh;
    private int rainColor;
    private boolean autoColor;

    public void setRainNum(int rainNum) {
        this.rainNum = rainNum;
        rainList.clear();
        init();
    }

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RainView);
        rainNum = ta.getInteger(R.styleable.RainView_rain_num, 80);
        rainLengh = ta.getInteger(R.styleable.RainView_rain_lengh, 20);
        rainColor = ta.getColor(R.styleable.RainView_rain_color, 0xffffffff);
        autoColor = ta.getBoolean(R.styleable.RainView_auto_color, false);

        ta.recycle();
//        rainList.clear();
//        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (thread == null) {
            thread = new MyThread();
            thread.start();
        } else {
            drawSub(canvas);
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            init();
            while (running) {
                logic();
                postInvalidate(); //线程中更新绘制，重新调用onDraw方法
                try {
                    Thread.sleep(50); //速度太快肉眼看不到，要睡眠
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // 离开屏幕时结束
        //onDetachedFromWindow在销毁资源（既销毁view）之后调用
        running = false;
        super.onDetachedFromWindow();
    }

    protected void init() {
        for (int i = 0; i < rainNum; i++) {
            RainItem item = new RainItem(getWidth(), getHeight());
            rainList.add(item);
        }
    }

    protected void logic() {
        for (int i = 0; i < rainList.size(); i++) {
            rainList.get(i).moveItem();
        }
    }

    protected void drawSub(Canvas canvas) {
        // 完成绘制操作
        for (int i = 0; i < rainList.size(); i++) {
            rainList.get(i).drawItem(canvas);
        }
    }


    private class RainItem {
        private int width;
        private int height;

        RainItem(int width, int height) {
            this.width = width;
            this.height = height;
            initItem();
        }

        private float startX = 0;
        private float startY = 0; // Start Point
        private float stopX = 0;
        private float stopY = 0; // Stop Point
        private float sizeX = 0;
        private float sizeY = 0;
        private Paint paint;
        private float opt;
        private Random random;

        private void initItem() {
            random = new Random();

            sizeX = 1 + random.nextInt(rainLengh / 2); // 随机改变雨点的角度
            sizeY = 1 + random.nextInt(rainLengh);
            // 单个雨点的形状，用线段表示
            startX = random.nextInt(this.width);
            startY = random.nextInt(this.height); // 随机改变雨点的位置
            stopX = startX + sizeX;
            stopY = startY + sizeY;

            paint = new Paint();
            if (autoColor) {
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                paint.setARGB(255, r, g, b);
            } else {
                paint.setColor(rainColor);//蓝色的雨
            }
            paint.setStrokeWidth(4);
        }

        void drawItem(Canvas canvas) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
//            canvas.drawRect(new Rect((int) startX, (int) sizeY, (int) stopX, (int) stopY), paint);
//            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_rain_point_2), startX, startY, paint);
        }

        void moveItem() {
            // 让雨点运动
            opt = 2 + random.nextFloat(); // 随机改变雨点的速度和长度
//            opt = 0.2f + random.nextFloat(); // 随机改变雨点的速度和长度
            startX += sizeX * opt;
            stopX += sizeX * opt;
            startY += sizeY * opt;
            stopY += sizeY * opt;

            // 雨点出了屏幕的时候让它回到起始点
            if (startY > this.height || startX > this.width) {
                startX = random.nextInt(this.width);
                startY = random.nextInt(this.height);
                stopX = startX + sizeX;
                stopY = startY + sizeY;
            }
        }

    }
}
