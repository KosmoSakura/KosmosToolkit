package cos.mos.utils.widget.list;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;


/**
 * @Description 网格布局分割线
 * @Author Kosmos
 * @Date 2019-10-17 16:32
 * @Email KosmoSakura@gmail.com
 */
public class KSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;//分割线宽
    private boolean mPaddingEdgeSide = true;//保留边距（为分割线宽）
    private boolean mPaddingStart = true;//保留左边分割线
    private boolean mPaddingEnd = true;//保留右边分割线
    private boolean mPaddingHeaderFooter = false;//保留上下分割线

    /**
     * @param space 分割线宽
     */
    public KSpaceDecoration(int space) {
        this.space = space;
    }


    /**
     * @param mPaddingEdgeSide 保留边距（为分割线宽）
     */
    public void setPaddingEdgeSide(boolean mPaddingEdgeSide) {
        this.mPaddingEdgeSide = mPaddingEdgeSide;
    }

    /**
     * @param mPaddingStart 保留左边分割线
     */
    public void setPaddingStart(boolean mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
    }

    /**
     * @param mPaddingEnd 保留右边分割线
     */
    public void setmPaddingEnd(boolean mPaddingEnd) {
        this.mPaddingEnd = mPaddingEnd;
    }

    /**
     * @param mPaddingHeaderFooter 保留上下分割线
     */
    public void setPaddingHeaderFooter(boolean mPaddingHeaderFooter) {
        this.mPaddingHeaderFooter = mPaddingHeaderFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int orientation = 0;
        int spanIndex = 0;
        int headerCount = 0, footerCount = 0;
        if (parent.getAdapter() instanceof BaseQuickAdapter) {
            headerCount = ((BaseQuickAdapter) parent.getAdapter()).getHeaderLayoutCount();
            footerCount = ((BaseQuickAdapter) parent.getAdapter()).getFooterLayoutCount();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            spanCount = 1;
            spanIndex = 0;
        }

        //普通Item的尺寸
        int itemCount = parent.getAdapter().getItemCount();
        if ((position >= headerCount && position < itemCount - footerCount)) {

            if (orientation == LinearLayout.VERTICAL) {
                float expectedWidth = (float) (parent.getWidth() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
                float originWidth = (float) parent.getWidth() / spanCount;
                float expectedX = (mPaddingEdgeSide ? space : 0) + (expectedWidth + space) * spanIndex;
                float originX = originWidth * spanIndex;
                outRect.left = (int) (expectedX - originX);
                outRect.right = (int) (originWidth - outRect.left - expectedWidth);
                if (position - headerCount < spanCount && mPaddingStart) {//设置第一行元素的top间距（如果mPaddingStart为true）
                    outRect.top = space;
                }
                outRect.bottom = space;//设置每个的元素的bottom间距
                int yu = itemCount % spanCount;//元素总数对单行个数求余
                int count = yu == 0 ? spanCount : yu;//最后一行的元素个数
                if (position - footerCount > itemCount - count - 1 && !mPaddingEnd) {//设置最后一行元素的bottom设为0（如果mPaddingEnd为false）
                    outRect.bottom = 0;
                }
            } else {
                float expectedHeight = (float) (parent.getHeight() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
                float originHeight = (float) parent.getHeight() / spanCount;
                float expectedY = (mPaddingEdgeSide ? space : 0) + (expectedHeight + space) * spanIndex;
                float originY = originHeight * spanIndex;
                outRect.bottom = (int) (expectedY - originY);
                outRect.top = (int) (originHeight - outRect.bottom - expectedHeight);
                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.left = space;
                }
                outRect.right = space;
            }
        } else if (mPaddingHeaderFooter) {
            if (orientation == LinearLayout.VERTICAL) {
                outRect.right = outRect.left = mPaddingEdgeSide ? space : 0;
                outRect.top = mPaddingStart ? space : 0;
            } else {
                outRect.top = outRect.bottom = mPaddingEdgeSide ? space : 0;
                outRect.left = mPaddingStart ? space : 0;
            }
        }

    }

}