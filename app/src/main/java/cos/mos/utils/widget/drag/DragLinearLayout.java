package cos.mos.utils.widget.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Description 拖拽
 * @Author Kosmos
 * @Date 2020.06.30 15:35
 * @Email KosmoSakura@gmail.com
 * @tip 2020.7.1 解决:在顶层控件，点击事件都是本视图不能拖拽
 */
public class DragLinearLayout extends ScaleLinearLayout {
    private float sx, sy;//二维坐标
    private int moving = 0;//移动中
    private MovingListener<DragLinearLayout> listener;//四向坐标监听器
    private TouchStateListener<DragLinearLayout> stateListener;

    public DragLinearLayout(Context context) {
        super(context);
    }

    public DragLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnStateListener(TouchStateListener<DragLinearLayout> stateListener) {
        this.stateListener = stateListener;
    }

    public void setOnMovingListener(MovingListener<DragLinearLayout> listener) {
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
                moving = 0;
                //手指坐标
                sx = event.getRawX();
                sy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moving++;
                //手指移动坐标
                float x = event.getRawX();
                float y = event.getRawY();
                // 获取手指移动的距离
                int dx = (int) (x - sx);
                int dy = (int) (y - sy);
                // 得到imageView最开始的各顶点的坐标
                layout(getLeft() + dx, getTop() + dy, getRight() + dx, getBottom() + dy);
                sx = event.getRawX();
                sy = event.getRawY();
                if (listener != null) {
                    listener.moving(this);
                }
            case MotionEvent.ACTION_UP:
                if (moving < 5) {
                    performClick();
                    return super.dispatchTouchEvent(event);
                } else {
                    return true;
                }
        }
        return super.dispatchTouchEvent(event);
    }
}
