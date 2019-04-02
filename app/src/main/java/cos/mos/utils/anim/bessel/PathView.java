package cos.mos.utils.anim.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description: 辅助绘制轨迹
 * @Author: 郑亮
 * @Date: 2016.11.09 23:02
 * @QQ: 1023007219
 * @link: https://www.jianshu.com/p/f64c3cd25f67
 */
public class PathView extends View {
    private Paint paint;
    private Path path;

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //设置画笔未实心
        paint.setStyle(Paint.Style.STROKE);
        //设置颜色
        paint.setColor(Color.GREEN);
        //设置画笔宽度
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path == null) {
            path = new Path();
        }
        path.moveTo(60, 60);
        path.lineTo(460, 460);
        path.quadTo(660, 260, 860, 460);
        path.cubicTo(160, 660, 960, 1060, 260, 1260);
        canvas.drawPath(path, paint);
    }
}
