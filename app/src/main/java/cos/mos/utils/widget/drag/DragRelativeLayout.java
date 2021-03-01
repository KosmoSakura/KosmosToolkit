package cos.mos.utils.widget.drag;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Description 拖拽
 * @Author Kosmos
 * @Date 2020.06.30 15:35
 * @Email KosmoSakura@gmail.com
 * @tip 2020.7.1 解决:在顶层控件，点击事件都是本视图不能拖拽
 * @tip 20201.3.1 优化拖拽算法
 */
public class DragRelativeLayout extends ScaleRelativeLayout {
    private float sx, sy;//二维坐标
    private final PointF start = new PointF();
    private MovingListener<DragRelativeLayout> listener;//四向坐标监听器
    private TouchStateListener<DragRelativeLayout> stateListener;

    public DragRelativeLayout(Context context) {
        super(context);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnStateListener(TouchStateListener<DragRelativeLayout> stateListener) {
        this.stateListener = stateListener;
    }

    public void setOnMovingListener(MovingListener<DragRelativeLayout> listener) {
        this.listener = listener;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override//解决:在顶层控件，点击事件都是本视图不能拖拽
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (stateListener != null) {
            stateListener.state(this, event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指坐标
                start.set(event.getRawX(), event.getRawY());
                sx = event.getRawX();
                sy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取手指移动的距离
                int dx = (int) (event.getRawX() - sx);
                int dy = (int) (event.getRawY() - sy);
                // 得到imageView最开始的各顶点的坐标
                layout(getLeft() + dx, getTop() + dy, getRight() + dx, getBottom() + dy);
                sx = event.getRawX();
                sy = event.getRawY();
                if (listener != null) {
                    listener.moving(this);
                }
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getRawX() - start.x) + Math.abs(event.getRawY() - start.y) < 1f) {
                    performClick();
                    return super.dispatchTouchEvent(event);
                } else {
                    return true;
                }
        }
        return super.dispatchTouchEvent(event);
    }
}
