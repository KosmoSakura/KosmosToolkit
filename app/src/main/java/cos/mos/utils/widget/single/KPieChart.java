package cos.mos.utils.widget.single;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cos.mos.toolkit.java.UColor;
import cos.mos.toolkit.java.UText;
import cos.mos.toolkit.system.UScreen;
import cos.mos.utils.R;


/**
 * @Description: 2个区域的饼状图
 * @Author: Kosmos
 * @Date: 2019.03.12 14:43
 * @Email: KosmoSakura@gmail.com
 * @apiNote 宽度必须大于高度的1.5倍
 */
public class KPieChart extends View {
    private float d;//圆弧直径
    private float padding = UScreen.dp2px(15);//间距
    private float TS10 = UScreen.dp2px(10);//字体
    private float TS12 = UScreen.dp2px(12);//字体
    private RectF oval;//圆弧区域
    private String name1, name2;//名1，名2
    private float hum1, hum2;//占比度数1，占比度数2(百分比）
    private int color1, color2;//区块颜色
    private Paint paint;
    private boolean show;
    private String chartsDescribe;//表名

    public KPieChart(Context context) {
        super(context);
        init();
    }

    public KPieChart(Context context, AttributeSet atr) {
        super(context, atr);
        init();
    }

    private void init() {
        show = false;
        oval = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public KPieChart setChartsDescribe(String chartsDescribe) {
        this.chartsDescribe = chartsDescribe;
        return this;
    }

    public KPieChart setName(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
        return this;
    }

    public KPieChart setHumidity(float hum1, float hum2) {
        this.hum1 = hum1 * 360 / (hum1 + hum2);
        this.hum2 = hum2 * 360 / (hum1 + hum2);
        show = true;
        return this;
    }

    public KPieChart setMainColor(int color) {
        this.color1 = color;
        this.color2 = UColor.ColorLighter(color);
        return this;
    }

    public void show() {
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        d = h - padding * 2;
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (show) {
            //圆弧
            oval.left = padding;
            oval.top = padding;
            oval.right = padding + d;
            oval.bottom = padding + d;

            paint.setTextSize(TS10);
            paint.setColor(color1);
            canvas.drawArc(oval, 0, hum1, true, paint);
            canvas.drawRect(padding * 2 + d, padding + d * 0.3f, padding * 3 + d, padding * 2 + d * 0.3f, paint);
            canvas.drawText(name1, padding * 3.5f + d, padding * 1.8f + d * 0.3f, paint);

            paint.setColor(color2);
            canvas.drawArc(oval, hum1, hum2, true, paint);
            canvas.drawRect(padding * 2 + d, padding + d * 0.6f, padding * 3 + d, padding * 2 + d * 0.6f, paint);
            canvas.drawText(name2, padding * 3.5f + d, padding * 1.8f + d * 0.6f, paint);

            //表描述 14%
            if (!UText.isEmpty(chartsDescribe)) {
                paint.setColor(UColor.getColor(R.color.gray_light));
                paint.setTextSize(TS12);
                canvas.drawText(chartsDescribe, padding * 0.5f + d / 2, padding * 1.8f + d, paint);
            }
        }
    }
}
