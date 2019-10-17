package cos.mos.utils.widget.list.adapter_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cos.mos.utils.widget.list.KDividerDecoration;

/**
 * @Description 基于RecyclerView的一些封装
 * @Author Kosmos
 * @Date 2019.10.17 16:16
 * @Email KosmoSakura@gmail.com
 * @Tip
 */
public class KRecyclerView extends RecyclerView implements ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator animExpand, animLess;//属性动画
    private ViewGroup.LayoutParams params;
    private int maxHeight = 0;//高度
    private boolean isExpand = true;//是否展开

    public KRecyclerView(Context context) {
        this(context, null);
    }

    public KRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置水平排列
     */
    public void setHorizontal() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(layoutManager);
    }

    /**
     * @param spanCount 网格列数
     * @Tip 设置网格排列
     */
    public void setGridLayout(int spanCount) {
        setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    }

    /**
     * 设置纵向排列
     */
    public void setVertical() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);
    }

    /**
     * @return 列表Y轴上的滚动距离
     */
    public int getDistanceScrollY() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        if (layoutManager == null) return 0;
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView == null) return 0;
        int itemHeight = firstVisiableChildView.getHeight();
        return position * itemHeight - firstVisiableChildView.getTop();
    }

    public void addItemDecorationStyle(int color, int height) {
        addItemDecorationStyle(color, height, 0, false, false);
    }

    /**
     * @param color         颜色
     * @param height        线宽
     * @param margin        边距
     * @param drawFirstLine 是否绘制第一条线
     * @param drawLastLine  是否绘制最后一条线
     */
    public void addItemDecorationStyle(int color, int height, int margin, boolean drawFirstLine, boolean drawLastLine) {
        addItemDecoration(new KDividerDecoration(color, height, margin, margin, drawFirstLine, drawLastLine));
    }

    /**
     * 滚动到底部
     */
    public void scrollToBottom() {
        if (getAdapter() == null)
            return;
        final int position = getAdapter().getItemCount();
        post(() -> scrollToPositionWithOffset(position > 0 ? position - 1 : 0));
    }

    /**
     * @param position 滚到指定下标
     * @Tip 仅限于LinearLayoutManager
     */
    public void scrollToPositionWithOffset(int position) {
        if (getLayoutManager() == null) return;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(position, 0);
        }
    }

    /**
     * @return 网格列表的行数
     */
    public int getLines() {
        if (getAdapter() != null) {
            if (getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager manager = (GridLayoutManager) getLayoutManager();
                double itemCount = (double) getAdapter().getItemCount();
                double spanCount = (double) manager.getSpanCount();
                return (int) Math.ceil(itemCount / spanCount);
            }
            return getAdapter().getItemCount();
        }
        return 0;
    }

    /**
     * @param showLines 显示item数量
     * @Tip 用于折叠列表
     */
    public void setShowLines(final int showLines) {
        if (maxHeight == 0) {
            post(() -> {
                maxHeight = getHeight();
                setLines(showLines);
            });
        } else
            setLines(showLines);
    }

    private void setLines(int showLines) {
        if (getParams() != null && getLines() != 0) {
            getParams().height = maxHeight / getLines() * showLines;
            setLayoutParams(getParams());
            isExpand = false;
        }
    }

    /**
     * @param showLines item数量
     * @Tip 收缩到指定item处
     */
    public void startLessExpandAnim(final int showLines) {
        if (maxHeight == 0) {
            post(() -> {
                maxHeight = getHeight();
                lessExpandAnim(showLines);
            });
        } else
            lessExpandAnim(showLines);
    }

    private void lessExpandAnim(int showLines) {
        if (getLines() == 0)
            return;
        if (animLess == null) {
            animLess = ValueAnimator.ofFloat(maxHeight, maxHeight / getLines() * showLines);
            animLess.addUpdateListener(this);
            animLess.setDuration(500);
        }
        isExpand = false;
        animLess.start();
    }

    /**
     * @param showLines item数量
     * @Tip 展开到指定item处
     */
    public void startExpandAnim(final int showLines) {
        if (getLines() == 0)
            return;
        if (maxHeight == 0) {
            post(() -> {
                maxHeight = getHeight();
                expandAnim(showLines);
            });
        } else
            expandAnim(showLines);
    }

    private void expandAnim(int showLines) {
        if (getLines() == 0)
            return;
        if (animExpand == null) {
            animExpand = ValueAnimator.ofFloat(maxHeight / getLines() * showLines, maxHeight);
            animExpand.addUpdateListener(this);
            animExpand.setDuration(500);
        }
        isExpand = true;
        animExpand.start();
    }

    /**
     * @return 获取展开状态
     */
    public boolean isExpand() {
        return isExpand;
    }

    private ViewGroup.LayoutParams getParams() {
        if (params == null)
            params = getLayoutParams();
        return params;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (getParams() != null) {
            getParams().height = Math.round((float) valueAnimator.getAnimatedValue());
            setLayoutParams(getParams());
//            requestLayout();
        }
    }
}
