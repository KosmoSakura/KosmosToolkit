package cos.mos.utils.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.R;


/**
 * Description:柱状图
 * <p>
 * Author: Kosmos
 * Time: 2017/3/14 001415:33
 * Email:ZeroProject@foxmail.com
 * Events:填充数据，计算绘图
 */
public class ColumnarChart extends View {

    private List<ColunmnarBean> list;
    private int totle;
    private float tb = 15;
    private String charts_name;//表名

    public ColumnarChart(Context context) {
        super(context);
        list = new ArrayList<>();
    }

    public ColumnarChart(Context context, AttributeSet atr) {
        super(context, atr);
        list = new ArrayList<>();
    }

    public void setList(List<ColunmnarBean> lis) {
        this.list.clear();
        this.list.addAll(lis);
        totle = 0;
        for (int i = 0; i < list.size(); i++) {
            totle += list.get(i).getHumidity();
            if (list.get(i).getId() <= 0) {
                list.get(i).setId(+new Random().nextInt(10000) + i);
            }
        }
        int left = (int) (this.list.size() * tb * 3.3f);
        setLayoutParams(new RelativeLayout.LayoutParams(left, RelativeLayout.LayoutParams.MATCH_PARENT));
        invalidate();
    }

    public void setList(List<ColunmnarBean> lis, String charts_name) {
        this.list.clear();
        this.list.addAll(lis);
        this.charts_name = charts_name;
        totle = 0;
        for (int i = 0; i < list.size(); i++) {
            totle += list.get(i).getHumidity();
            if (list.get(i).getId() <= 0) {
                list.get(i).setId(+new Random().nextInt(10000) + i);
            }
        }
        int left = (int) (this.list.size() * tb * 3.3f);
        setLayoutParams(new RelativeLayout.LayoutParams(left, RelativeLayout.LayoutParams.MATCH_PARENT));
        invalidate();
    }

    public void setCharts_name(String charts_name) {
        this.charts_name = charts_name;
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);// 设置背景颜色(透明)
        int w = getWidth();
        int h = getHeight();
        Paint pLine = new Paint();//坐标轴画笔
        pLine.setStrokeWidth(2f);
        pLine.setColor(getResources().getColor(R.color.fun_txt_black));
        pLine.setStyle(Paint.Style.FILL);
        pLine.setAntiAlias(true);
        canvas.drawLine(w * 0.02f, 0, w * 0.02f, h, pLine);//Y轴
        canvas.drawLine(0, h * 0.9f, w, h * 0.9f, pLine);//X轴

        //表名
        if (!UText.isEmpty(charts_name)) {
            pLine.setColor(getResources().getColor(R.color.white));
            pLine.setTextSize(22f);//字体大小
            canvas.drawText(charts_name, getWidth() / 2, 35, pLine);
        }

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ColunmnarBean dto = list.get(i);
                drawRectf(canvas, dto, i);

            }
        }
        invalidate();
    }


    public void drawRectf(Canvas c, ColunmnarBean dto, int i) {
        //绘制矩形
        float left_right = tb * 3.0f;//间距
        float w = getWidth() * 0.02f + 40;//X轴起始点
        float h = getHeight() * 0.9f;//Y轴终点
        float round = 0;//矩形弧度：tb * 0.3f
        int with_rect = 50;//圆柱宽度

        Paint paint = new Paint();//矩形画笔
        paint.setStrokeWidth(tb * 0.1f);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        boolean isSelect = select_id == dto.getId() && itemSelect != null;//是否选中
        RectF f1 = new RectF();
        float base = dto.getHumidity() * (tb * 10.0f / 100) + 100;
        if (isSelect) {
            paint.setColor(dto.getColor());
        } else {
            paint.setColor(dto.getColor());
        }
        //原始高度的圆柱
//        RectF f = new RectF();
//        f.set(w + left_right * i, getHeight() - tb * 15.0f, w + with_rect + left_right * i, h);//原始高度

//        c.drawRoundRect(f, round, round, paint);//原始高度

//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(getResources().getColor(R.color.deafult_tab_gray));


        //补平底部的弧角
//        f1.set(w + left_right * i,// left
//            h-10,//top
//            w + with_rect + left_right * i,//right
//            h);//bottom
//        c.drawRoundRect(f1, 0, 0, paint);//数据高度
        f1.set(w + left_right * i,// left
            getHeight() - (base + tb * 1.5f),//top
            w + with_rect + left_right * i,//right
            h);//bottom
        c.drawRoundRect(f1, round, round, paint);//数据高度
        dto.setRect(w + left_right * i, getHeight() - (base + tb * 1.5f), w + with_rect + left_right * i, h);//保存原始高度

        if (isSelect) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(dto.getColor());
            f1.set(w + left_right * i - 5,// left
                getHeight() - (base + tb * 1.5f) - 5,//top
                w + with_rect + left_right * i + 5,//right
                h);//bottom
            c.drawRoundRect(f1, round, round, paint);//数据高度
        }


        //绘制分类名字
        float x = w + left_right * i + w + with_rect + left_right * i;
        Paint pTxt = new Paint();
        pTxt.setStrokeWidth(tb * 0.1f);
        pTxt.setTextSize(22f);
        if (dto.getTxt_color() == -1) {
            pTxt.setColor(dto.getColor());
        } else {
            pTxt.setColor(dto.getTxt_color());
        }
        pTxt.setTextAlign(Paint.Align.CENTER);
        c.drawText(dto.getName(), x / 2, getHeight() * 0.9f + 40, pTxt);//分类名字

        pTxt.setColor(getResources().getColor(R.color.white));
        c.drawText(dto.getHumidity() + "", x / 2, getHeight() - (base + tb * 1.5f) - 10, pTxt);//数据高度
        list.set(i, dto);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            boolean isOut = true;
            for (int i = 0; i < list.size(); i++) {
                ColunmnarBean dto = list.get(i);
                float l = dto.getL();
                float r = dto.getR();
                float t = dto.getTop();
                float b = dto.getB();

                if (x > l && x < r && y > t && y < b) {
                    select_id = dto.getId();
                    isOut = false;
                    if (itemSelect != null) {
                        itemSelect.onItemSelect(dto);
                        invalidate();
                    }
                }
            }
            if (isOut) {
                select_id = -1;
            }
        }
        return super.onTouchEvent(event);
    }

    private int select_id = -1;

    private void logs(String s) {
        Log.d("Zero", "Pin:" + s);
    }

    public interface ItemSelect {
        void onItemSelect(ColunmnarBean dto);
    }

    private ItemSelect itemSelect;

    public void setOnItemSelectListener(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }
}
