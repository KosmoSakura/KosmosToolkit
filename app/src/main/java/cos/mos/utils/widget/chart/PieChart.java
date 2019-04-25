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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.R;


/**
 * Description:饼状图
 * <p>
 * Author: Kosmos
 * Time: 2017/3/14 001415:33
 * Email:ZeroProject@foxmail.com
 * Events:填充数据，计算绘图
 */
public class PieChart extends View {

    private List<PinDto> list;
    private float totle = 100;
    private RectF normalOval;//未选择区域
    private RectF selectOval;//选中区域
    private Paint paint;
    private float ow;//圆弧Left
    private float oh;//圆弧Top
    private float d = 500;//圆弧直径
    private float r = d / 2;//圆弧半径
    private float oa;//A点坐标
    private float ob;//A点坐标
    private String charts_name;//表名

    public PieChart(Context context) {
        super(context);
        list = new ArrayList<>();
    }

    public PieChart(Context context, AttributeSet atr) {
        super(context, atr);
        list = new ArrayList<>();
        normalOval = new RectF();
        selectOval = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(22f);//字体大小
    }

    public void setList(List<PinDto> lis) {
        this.list.clear();
        this.list.addAll(lis);
        totle = 0;
        for (int i = 0; i < list.size(); i++) {
            totle += list.get(i).getHumidity();
            if (list.get(i).getId() <= 0) {
                list.get(i).setId(+new Random().nextInt(10000) + i);
            }
        }
        invalidate();
    }

    public void setList(List<PinDto> lis, String charts_name) {
        this.list.clear();
        this.list.addAll(lis);
        this.charts_name = charts_name;
        totle = 0;
        for (int i = 0; i < list.size(); i++) {
            totle += list.get(i).getHumidity();
            if (list.get(i).getId() <= 0) {
                list.get(i).setId(new Random().nextInt(10000) + i);
            }
        }
        invalidate();
    }

    public void setCharts_name(String charts_name) {
        this.charts_name = charts_name;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST
            || heightMode == MeasureSpec.AT_MOST) {
            width = 400;
            height = 400;
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ow = getWidth() / 15;//圆弧Left
        oh = getHeight() / 13;//圆弧Top

        addDefault(normalOval);
    }


    private void addDefault(RectF rectF) {
        rectF.left = ow;
        rectF.top = oh;
        rectF.right = ow + d;
        rectF.bottom = oh + d;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (list != null) {
            canvas.drawColor(Color.TRANSPARENT);// 设置背景颜色(透明)

            float qw = ow + d + 50;//文字矩形标识Left
            float qh = oh + 20;//文字矩形标识Left
            float margen = 40;//间距倍率
            oa = ow + d;
            ob = oh + r;

//            Paint pCircle = new Paint();//圆形画笔
//            pCircle.setAntiAlias(true);
//            pCircle.setColor(getResources().getColor(R.color.white));
//            pCircle.setStyle(Paint.Style.STROKE);
//            pCircle.setStrokeWidth(3);
//            canvas.drawCircle(ow + d / 2, oh + d / 2, d / 2, pCircle);//圆圈

            //表名
            if (!UText.isEmpty(charts_name)) {
                paint.setColor(getResources().getColor(R.color.white));
                canvas.drawText(charts_name, getWidth() / 2, oh + d + 15, paint);
            }

            float start = 0;

            for (int i = 0; i < list.size(); i++) {
                PinDto dto = list.get(i);
                paint.setColor(dto.getColor());//圆弧颜色

                float hum = dto.getHumidity() / totle * 360;
                if (selectId == dto.getId() && itemSelect != null) {
                    moveRac(dto.getX(), dto.getY());
                    paint.setColor(dto.getColor());
                    canvas.drawArc(selectOval, start, hum, true, paint);
                } else {
                    canvas.drawArc(normalOval, start, hum, true, paint);//圆弧
                }
                //设置圆心角
                dto.setX(start);
                dto.setY(hum + start);

                // 矩形的边界
                RectF mRectOval = new RectF(qw, qh + margen * i, qw + 20, qh + margen * i + 20);

                //显示数值
                DecimalFormat df = new DecimalFormat("#0.00");
                String point_txt = df.format(new BigDecimal(dto.getHumidity() / totle * 100)) + "%";

                //渲染文本
                canvas.drawRect(mRectOval, paint);//矩形
                if (dto.getTxt_color() != -1) {
                    paint.setColor(dto.getTxt_color());//圆弧颜色
                }
                canvas.drawText(dto.getName() + "\b" + point_txt, qw + 30, qh + 18 + margen * i, paint);

                //计算文字位置坐标 圆心O(ow+r,oh+r)
//                float aob = dto.getY();
//
//                float x = (float) (ow + r + r * Math.cos(aob));
//                float y = (float) (oh + r + r * Math.sin(aob));
//
//                if (aob >= 90 && aob <= 180) {
//                    aob = 180 - aob;
//                    x = (float) (ow + r * Math.cos(aob));
//                    y = (float) (oh + r + r * Math.sin(aob));
//                } else if (aob >= 180 && aob <= 270) {
//                    aob = aob - 180;
//                    x = (float) (ow + r * Math.cos(aob));
//                    y = (float) (oh + r + r * Math.sin(aob));
//                } else if (aob >= 270 && aob <= 360) {
//                    aob = 360 - aob;
//                    x = (float) (ow + r + r * Math.cos(aob));
//                    y = (float) (oh + r + r * Math.sin(aob));
//                }
//                paint.setColor(getResources().getColor(R.color.white));
//                canvas.drawText(point_txt, (x + oa) / 2, (y + ob) / 2, paint);
//                oa = x;
//                ob = y;

                start += hum;//增加圆心角
                list.set(i, dto);
            }
        }
        invalidate();
    }

    private void circlr(Canvas canvas, float cx, float cy) {
        canvas.drawCircle(cx, cy, 15, paint);
    }

    //画文字

    /**
     * @param x 扇形起始度数
     * @param y 扇形圆心角
     *          计算偏移量
     */
    private void moveRac(float x, float y) {
        addDefault(selectOval);
        float middle = (x + y) / 2;
        int doubls = 15;//移动倍率
        if (middle <= 90) {
            int top = (int) (Math.sin(Math.toRadians(middle)) * doubls);
            int left = (int) (Math.cos(Math.toRadians(middle)) * doubls);
            selectOval.left += left;
            selectOval.right += left;
            selectOval.top += top;
            selectOval.bottom += top;
        } else if (middle > 90 && middle <= 180) {
            middle = 180 - middle;
            int top = (int) (Math.sin(Math.toRadians(middle)) * doubls);
            int left = (int) (Math.cos(Math.toRadians(middle)) * doubls);
            selectOval.left -= left;
            selectOval.right -= left;
            selectOval.top += top;
            selectOval.bottom += top;
        } else if (middle > 180 && middle <= 270) {
            middle = 270 - middle;
            int left = (int) (Math.sin(Math.toRadians(middle)) * doubls);
            int top = (int) (Math.cos(Math.toRadians(middle)) * doubls);
            selectOval.left -= left;
            selectOval.right -= left;
            selectOval.top -= top;
            selectOval.bottom -= top;
        } else if (middle > 270 && middle <= 360) {
            middle = 360 - middle;
            int top = (int) (Math.sin(Math.toRadians(middle)) * doubls);
            int left = (int) (Math.cos(Math.toRadians(middle)) * doubls);
            selectOval.left += left;
            selectOval.right += left;
            selectOval.top -= top;
            selectOval.bottom -= top;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float x = event.getX();
            float y = event.getY();
            int radius = 0;
            //在矩形内部
            if (x >= ow && x <= ow + d && y >= oh && y <= oh + d) {
                float ox = ow + r; //圆心O(ox,oy)
                float oy = oh + r;
                //在圆形内部
                if (Math.pow(ox - x, 2) + Math.pow(oy - y, 2) <= Math.pow(d / 2, 2)) {
                    // 第一象限
                    if (x >= ox && y >= oy) {
                        radius = (int) (Math.atan((y - oy) * 1.0f / (x - ox)) * 180 / Math.PI);
                    }
                    // 第二象限
                    else if (x <= ox && y >= oy) {
                        radius = (int) (Math.atan((ox - x) / (y - oy)) * 180 / Math.PI + 90);
                    }
                    // 第三象限
                    else if (x <= ox && y <= oy) {
                        radius = (int) (Math.atan((oy - y) / (ox - x)) * 180 / Math.PI + 180);
                    }
                    // 第四象限
                    else if (x >= ox && y <= oy) {
                        radius = (int) (Math.atan((x - ox) / (oy - y)) * 180 / Math.PI + 270);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        PinDto dto = list.get(i);
                        if (dto.getX() <= radius && dto.getY() >= radius) {
                            selectId = dto.getId();
                            if (itemSelect != null) {
                                itemSelect.onItemSelect(dto);
                            }
                            invalidate();
                            return true;
                        }
                    }
                } else { //在圆形内部
                    selectId = -1;
                }
            } else {//在矩形内部
                selectId = -1;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private int selectId = -1;

    private void logs(String s) {
        Log.d("Zero", "" + s);

    }

    public interface ItemSelect {
        void onItemSelect(PinDto dto);
    }

    private ItemSelect itemSelect;

    public void setOnItemSelectListener(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }
}
