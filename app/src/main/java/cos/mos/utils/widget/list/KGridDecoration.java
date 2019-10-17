package cos.mos.utils.widget.list;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * @Description 网格布局分割线, 直接传入 四边间距
 * @Author Kosmos
 * @Date 2019-10-17 16:30
 * @Email KosmoSakura@gmail.com
 */
public class KGridDecoration extends RecyclerView.ItemDecoration {
    private int left, top, right, bottom;//四项线宽

    /**
     * @Tip 四边等宽
     */
    public KGridDecoration(int padding) {
        this.left = padding;
        this.top = padding;
        this.right = padding;
        this.bottom = padding;
    }

    /**
     * @Tip 四边自定义线宽
     */
    public KGridDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(left, top, right, bottom);
    }
}