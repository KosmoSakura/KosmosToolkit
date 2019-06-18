package cos.mos.utils.widget.list.adapter_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rato.tianyi.R;
import com.rato.tianyi.basic.utils.TxtUtil;


/**
 * Created by vpcsd on 2016/8/23
 */
public class SwipeRecyclerView extends NestedScrollView {

    private static final float OFFSET_RATIO = 0.3f;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvLoadingText;
    //x上次保存的
    private int mLastMotionX;
    //y上次保存的
    private int mLastMotionY;
    //滑动状态
    private int mPullState;
    //上滑
    private int PULL_UP_STATE = 2;
    private int PULL_FINISH_STATE = 0;
    //当前滑动的距离
    private int curTransY;
    //尾部的高度
    private int footerHeight;
    //内容布局
    private View contentView;
    //尾部局
    private View footerView;
    private LinearLayout linearView;
    //是否上拉加载更多
    private boolean isLoadNext = false;
    //是否在加载中
    private boolean isLoading = false;

    private OnSwipeRecyclerViewListener onSwipeRecyclerViewListener;

    private boolean isCancelLoadNext = false;


    private boolean isCanUpLoad;
    private boolean isMax;

    private LinearLayout llEmpt;
    private TextView tEmpt;
    private ImageView iEmpt;
    private TextView btnEmpt;
    private boolean alwaysHideEmpty;

    /**
     * 传入null，则不显示空布局
     */
    public void initEmpt(String text) {
        alwaysHideEmpty = TxtUtil.isEmpty(text);
        tEmpt.setText(TxtUtil.isNull(text));
        btnEmpt.setVisibility(View.GONE);
        iEmpt.setImageResource(R.drawable.icon_empty);
    }

    public void showEmpty(boolean empty) {
        isCanUpLoad = !empty;
        if (isCanUpLoad || alwaysHideEmpty) {
            footerView.setVisibility(View.VISIBLE);
            llEmpt.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
            llEmpt.setVisibility(View.VISIBLE);
        }
    }

    public void initEmpt(String text, int resID, String btnTxt, OnClickListener listener) {
        tEmpt.setText(text);
        if (TxtUtil.isEmpty(btnTxt)) {
            btnEmpt.setVisibility(View.GONE);
        } else {
            btnEmpt.setText(btnTxt);
            btnEmpt.setVisibility(View.VISIBLE);
            btnEmpt.setOnClickListener(listener);
        }
        iEmpt.setImageResource(resID);
    }

    public boolean isCanUpLoad() {
        return isCanUpLoad;
    }

    public void setCanUpLoad(boolean canUpLoad) {
        isCanUpLoad = canUpLoad;
        if (tvLoadingText != null) {
            if (isCanUpLoad) {
                tvLoadingText.setVisibility(View.VISIBLE);
            } else {
                tvLoadingText.setVisibility(View.GONE);
            }
        }
    }

    public void setMaxStatus(boolean b) {
        isMax = b;
    }

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }


    private void initView(final Context context) {

        linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        addView(linearView, linearParams);

        contentView = LayoutInflater.from(context).inflate(R.layout.lay_swiperecyclerview, null);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swiperefreshlayout);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerview);
        footerView = LayoutInflater.from(context).inflate(R.layout.lay_swiperecyclerview_footerview, null);
        tvLoadingText = (TextView) footerView.findViewById(R.id.loading_text);

        llEmpt = (LinearLayout) contentView.findViewById(R.id.list_is_empty);
        iEmpt = (ImageView) contentView.findViewById(R.id.frag_logo_image);
        tEmpt = (TextView) contentView.findViewById(R.id.frag_logo_empt_t);
        btnEmpt = (TextView) contentView.findViewById(R.id.frag_logo_empt_btn);

        swipeRefreshLayout.setEnabled(false);//禁止下拉
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    swipeRefreshLayout.setRefreshing(true);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            if (onSwipeRecyclerViewListener != null) {
                                onSwipeRecyclerViewListener.onRefresh();
                            }
                            isLoading = false;
                        }
                    }, 2000);
                }
            }
        });

        linearView.addView(contentView);
        linearView.addView(footerView);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int height = getHeight() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    40, context.getResources().getDisplayMetrics());//动态详情和照片详情，评论列表由于底部有个40dp高的操作区，把列表挡住了一部分，所以减掉40dp
                if (height != 0) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams recycleParams = contentView.getLayoutParams();
                    recycleParams.height = height;
                    contentView.setLayoutParams(recycleParams);

                    ViewGroup.LayoutParams footerParams = tvLoadingText.getLayoutParams();
                    footerHeight = footerParams.height;

                    ViewGroup.LayoutParams contentParams = linearView.getLayoutParams();
                    contentParams.height = height + footerHeight;
                    linearView.setLayoutParams(contentParams);
                    curTransY = 0;
                }
            }
        });
    }

    public void setSwipeRefreshColor(int color) {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(color));
    }

    public void isCancelLoadNext(Boolean isCancelLoadNext) {
        this.isCancelLoadNext = isCancelLoadNext;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastMotionX;
                int deltaY = y - mLastMotionY;
                if (Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(deltaY) > 10 && isCanUpLoad() == true) {
                    if (isRefreshViewScroll(deltaY)) {
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    private boolean isRefreshViewScroll(int deltaY) {
        if (deltaY < 0 && !recyclerView.canScrollVertically(1) && curTransY <= footerHeight && !isLoading && !isCancelLoadNext) {
            mPullState = PULL_UP_STATE;
            isLoading = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastMotionY;
                if (mPullState == PULL_UP_STATE) {
                    curTransY += deltaY;
                    if (Math.abs(curTransY) > Math.abs(footerHeight)) {
                        curTransY = -footerHeight;
                    }
                    linearView.setTranslationY(curTransY);
                    if (Math.abs(curTransY) == Math.abs(footerHeight)) {
                        isLoadNext = true;
                    } else {
                        isLoadNext = false;
                    }
                }
                mLastMotionY = y;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isLoadNext) {
                    changeFooterState(true);
                    mPullState = PULL_FINISH_STATE;
                    if (onSwipeRecyclerViewListener != null) {
                        onSwipeRecyclerViewListener.onLoadNext();
                    } else {
                        hideTranslationY();
                        isLoading = false;
                        isLoadNext = false;
                    }
                } else {
                    hideTranslationY();
                    isLoading = false;
                }
                return true;

        }

        return super.onTouchEvent(event);
    }

    public void onLoadFinish() {
        if (curTransY == 0) {
            return;
        }
        isLoading = false;
        isLoadNext = false;
        hideTranslationY();
    }

    private void hideTranslationY() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearView, "translationY", curTransY, 0).setDuration(1000);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                curTransY = 0;
                changeFooterState(false);
            }
        });
    }

    private void changeFooterState(boolean loading) {
        if (isMax) {
            tvLoadingText.setText("没有更多了");
        } else {
            if (loading) {
                tvLoadingText.setText("正在努力的加载中...");
            } else {
                tvLoadingText.setText("加载更多");
            }
        }

    }

    public void setOnSwipeRecyclerViewListener(OnSwipeRecyclerViewListener onSwipeRecyclerViewListener) {
        this.onSwipeRecyclerViewListener = onSwipeRecyclerViewListener;
    }

    public interface OnSwipeRecyclerViewListener {
        public void onRefresh();

        public void onLoadNext();
    }
}
