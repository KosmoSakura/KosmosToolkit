package cos.mos.utils.widget.list.adapter_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Description:完全展示列表
 * Author: Kosmos
 * Time: 2017/8/17 001711:28
 * Email:ZeroProject@foxmail.com
 * Events:
 */
public class NoScorllRecyclerView extends RecyclerView {
    public NoScorllRecyclerView(Context context) {
        super(context);
    }

    public NoScorllRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScorllRecyclerView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean canScrollVertically(int direction) {
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return original || getChildAt(0) != null && getChildAt(0).getTop() < 0;
        }
        return super.canScrollVertically(direction);

    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
            MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
