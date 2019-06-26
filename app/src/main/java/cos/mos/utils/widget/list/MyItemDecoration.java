package cos.mos.utils.widget.list;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 网格布局,直接传入 四边间距
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private int left, top, right, bottom;

    public MyItemDecoration(int padding) {
        this.left = padding;
        this.top = padding;
        this.right = padding;
        this.bottom = padding;
    }

    public MyItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left, top, right, bottom);
    }
}