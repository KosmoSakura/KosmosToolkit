package cos.mos.utils.widget.list.adapter_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by ZeroProject on 2016/9/7 0007 17:08
 * Email:ZeroProject@foxmail.com
 */
public class NoScorllView extends ScrollView {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    private OnScrollListener onScrollListener;

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */

    public NoScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滚动接口
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d != 0) {
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int scrollY = this.getScrollY();
        if (onScrollListener != null) {
            onScrollListener.onScroll(scrollY);
        }
//        switch(ev.getAction()){
//            case MotionEvent.ACTION_UP:
//                if(onScrollListener != null){
//                    onScrollListener.onScroll(scrollY);
//                }
//                break;
//        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         *
         * @param scrollY
         */
        public void onScroll(int scrollY);
    }
}
