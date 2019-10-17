package cos.mos.utils.widget.list;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.jetbrains.annotations.NotNull;


/**
 * @Description 自动分割线
 * @Author Kosmos
 * @Date 2019-10-17 16:41
 * @Email KosmoSakura@gmail.com
 * @Tip 自动判断RecyclerView排列样式添加分割线
 * @Tip 目前包括：StaggeredGridLayoutManager、GridLayoutManager、LinearLayoutManager
 */
public class KDividerDecoration extends RecyclerView.ItemDecoration {
    private ColorDrawable colorDrawable;//分割线颜色
    private int line;//线宽
    private int paddingLeft;//左边距
    private int paddingRight;//右边距
    private boolean drawLastLine = true;//是否显示最后一个item的分割线
    private boolean drawFirstLine = false;//是否显示第一个item的分割线

    /**
     * @param color  分割线颜色
     * @param height 线宽
     */
    public KDividerDecoration(int color, int height) {
        this.colorDrawable = new ColorDrawable(color);
        this.line = height;
    }

    /**
     * @param color        分割线颜色
     * @param height       线宽
     * @param paddingLeft  左边距
     * @param paddingRight 右边距
     */
    public KDividerDecoration(int color, int height, int paddingLeft, int paddingRight) {
        this.colorDrawable = new ColorDrawable(color);
        this.line = height;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    /**
     * @param color        分割线颜色
     * @param height       线宽
     * @param paddingLeft  左边距
     * @param paddingRight 右边距
     * @param firstLine    是否显示最后一个item的分割线
     * @param lastItem     是否显示最后一个item的分割线
     */
    public KDividerDecoration(int color, int height, int paddingLeft, int paddingRight, boolean firstLine, boolean lastItem) {
        this.colorDrawable = new ColorDrawable(color);
        this.line = height;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.drawFirstLine = firstLine;
        this.drawLastLine = lastItem;
    }

    /**
     * @param lastItem 是否显示最后一个item的分割线
     */
    public void setDrawLastLine(boolean lastItem) {
        this.drawLastLine = lastItem;
    }

    /**
     * @param firstLine 是否显示第一个item的分割线
     */
    public void setDrawFirstLine(boolean firstLine) {
        this.drawFirstLine = firstLine;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) return;

        int position = parent.getChildAdapterPosition(view);
        int orientation = 0;
        int headerCount = 0, footerCount = 0;
        if (adapter instanceof BaseQuickAdapter) {
            headerCount = ((BaseQuickAdapter) adapter).getHeaderLayoutCount();
            footerCount = ((BaseQuickAdapter) adapter).getFooterLayoutCount();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }

        if (position >= headerCount && position < adapter.getItemCount() - footerCount || drawFirstLine) {
            if (orientation == OrientationHelper.VERTICAL) {
                outRect.bottom = line;
            } else {
                outRect.right = line;
            }
        }
    }

    @Override
    public void onDrawOver(@NotNull Canvas c, RecyclerView parent, @NotNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) return;

        int orientation = 0;
        int headerCount = 0, dataCount;
        if (adapter instanceof BaseQuickAdapter) {
            headerCount = ((BaseQuickAdapter) adapter).getHeaderLayoutCount();
            dataCount = adapter.getItemCount();
        } else {
            dataCount = adapter.getItemCount();
        }
        int dataStartPosition = headerCount;
        int dataEndPosition = headerCount + dataCount;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        int start, end;
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.getPaddingLeft() + paddingLeft;
            end = parent.getWidth() - parent.getPaddingRight() - paddingRight;
        } else {
            start = parent.getPaddingTop() + paddingLeft;
            end = parent.getHeight() - parent.getPaddingBottom() - paddingRight;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position >= dataStartPosition && position < dataEndPosition - 1//数据项除了最后一项
                || (position == dataEndPosition - 1 && drawLastLine)//数据项最后一项
                || (!(position >= dataStartPosition && position < dataEndPosition) && drawFirstLine)//header&footer且可绘制
            ) {

                if (orientation == OrientationHelper.VERTICAL) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + line;
                    colorDrawable.setBounds(start, top, end, bottom);
                    colorDrawable.draw(c);
                } else {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left = child.getRight() + params.rightMargin;
                    int right = left + line;
                    colorDrawable.setBounds(left, start, right, end);
                    colorDrawable.draw(c);
                }
            }
        }
    }
}