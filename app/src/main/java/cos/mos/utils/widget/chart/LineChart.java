package cos.mos.utils.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;


/**
 * @Description: 2个区域的饼状图
 * @Author: Kosmos
 * @Date: 2019.03.12 14:43
 * @Email: KosmoSakura@gmail.com
 * @apiNote 宽度必须大于高度的1.5倍
 */
public class LineChart extends View {
    //字体
    private final float TS5 = UScreen.dp2px(5);
    private final float TS10 = UScreen.dp2px(10);
    private final float TS12 = UScreen.dp2px(12);
    private final float padding = UScreen.dp2px(12);//间距
    private final int MaxCount = 12;//最大锚点数目
    private int w, h;//控件宽高
    private Paint paint;
    private Path path;
    private String name1, name2;//名1，名2
    private String chartsDescribe;//表名
    private Context context;
    private ArrayList<LineBean> list;
    private float max, min, totle, average, length;//最大值，最小值， 总值，平均值,长度

    public LineChart(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LineChart(Context context, AttributeSet atr) {
        super(context, atr);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.3f);
        path = new Path();
    }

    public LineChart setChartsDescribe(String chartsDescribe) {
        this.chartsDescribe = chartsDescribe;
        return this;
    }

    public LineChart setName(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
        return this;
    }

    public LineChart setData(ArrayList<LineBean> list) {
        max = 0f;
        min = 0f;
        totle = 0f;
        average = 0f;
        length = 0;
        if (list == null || list.isEmpty()) {
            return this;
        }
        this.list = list;
        length = list.size();
        float value;
        for (LineBean bean : list) {
            value = bean.getValue();
            totle += value;
            if (max < value) {
                max = value;
            }
            if (min == 0 || min > value) {
                min = value;
            }
//            Log.d("Kosmos", "Day：" + bean.getKey() + ",Vl:" + bean.getValue());
        }
        average = totle / length;
        path = new Path();
        return this;
    }

    public void show() {
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        if (chartsDescribe != null) {
            paint.setColor(getColor(R.color.gray_deep));
            paint.setTextSize(TS12);
            canvas.drawText(chartsDescribe, padding, padding * 2f, paint);
            canvas.drawText(name1 + formatTwo(totle), padding, padding * 3.5f, paint);
            canvas.drawText(name2 + formatTwo(average), padding, padding * 5f, paint);
        }
        float startY = padding * 6f;
        float endY = h - padding * 2f;
        canvas.drawLine(0f, startY, w, startY, paint);
        canvas.drawLine(0f, endY, w, endY, paint);
        float textY = h - padding;
        if (list != null && !list.isEmpty()) {
            LineBean bean;
            int key;
            float value;
            float diff = endY - startY;
            float x, y;
            for (int i = 0; i < length; i++) {
                bean = list.get(i);
                if (bean == null) {
                    continue;
                }
                key = bean.getKey();
                value = bean.getValue();
                x = w * i / length + TS5;
                y = endY - diff * (value - min) / (max - min);
                if (i == 0) {
                    path.reset();
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                    path.moveTo(x, y);
                }
                if (value == min) {
                    paint.setColor(getColor(R.color.fun_txt_green));
                } else if (value == max) {
                    paint.setColor(getColor(R.color.red_deep));
                } else {
                    paint.setColor(getColor(R.color.gray_deep));
                }
                canvas.drawCircle(x, y, 5f, paint);
                canvas.drawText(Integer.toString(key), x - TS5, textY, paint);
            }
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(getColor(R.color.gray_deep));
            canvas.drawPath(path, paint);
        }
    }


    private int getColor(int color) {
        return ContextCompat.getColor(context, color);
    }

    private static float formatTwo(float digit) {
        return (int) (digit * 100f) / 100f;
    }
}
