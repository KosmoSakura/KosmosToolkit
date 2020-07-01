package cos.mos.utils.widget.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Description 拖拽
 * @Author Kosmos
 * @Date 2019.01.23 18:26
 * @Email KosmoSakura@gmail.com
 * @tip 2020.7.1 优化拖拽和点击事件
 */
public class DragImageView extends ScaleImageView {
    private float sx, sy;//二维坐标
    private int moving = 0;//移动中
    private MovingListener<DragImageView> listener;//四向坐标监听器
    private TouchStateListener<DragImageView> stateListener;

    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnStateListener(TouchStateListener<DragImageView> stateListener) {
        this.stateListener = stateListener;
    }

    public void setOnMovingListener(MovingListener<DragImageView> listener) {
        this.listener = listener;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                break;
            case MotionEvent.ACTION_UP:
                if (moving < 5) {
                    performClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
