package cos.mos.utils.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import cos.mos.utils.R;
import cos.mos.toolkit.system.AppBean;
import cos.mos.toolkit.init.KActivity;
import cos.mos.utils.ui.data.IgnoreAdapter;
import cos.mos.toolkit.system.UApps;
import cos.mos.toolkit.system.UScreen;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.22 21:26
 * @Email: KosmoSakura@gmail.com
 */
public class BottomSheetDialogSimple extends KActivity {
    @Override
    protected int layout() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void logic() {

    }

    private BottomSheetDialog diaBottom;

    /**
     * 最简单的用法
     */
    private void showDialog1() {
        if (diaBottom == null) {
            View view = View.inflate(context, R.layout.dia_wifi_code, null);
            diaBottom = new BottomSheetDialog(context, R.style.TransparentBottomSheetStyle);
            diaBottom.setContentView(view);
        }
        if (diaBottom.isShowing()) {
            diaBottom.hide();
        } else {
            diaBottom.show();
        }
    }

    /**
     * BottomSheetBehavior的作用演示
     * STATE_HIDDEN: 隐藏状态。默认是false，可通过app:behavior_hideable属性设置。
     * STATE_COLLAPSED: 折叠关闭状态。可通过app:behavior_peekHeight来设置显示的高度,peekHeight默认是0。
     * STATE_DRAGGING: 被拖拽状态
     * STATE_SETTLING: 拖拽松开之后到达终点位置（collapsed or expanded）前的状态。
     * STATE_EXPANDED: 完全展开的状态。
     */
    private void showDialog2() {
        if (diaBottom == null) {
            View view = View.inflate(context, R.layout.dia_wifi_code, null);
            diaBottom = new BottomSheetDialog(context, R.style.TransparentBottomSheetStyle);
            diaBottom.setContentView(view);
            //调用setContentView方法之后
            BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
            behavior.setPeekHeight((int) UScreen.dp2px(500));//手动设置打开高度
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View view, int newState) {
                    //用户滑动关闭BottomSheetDialog
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        diaBottom.dismiss();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float v) {

                }
            });
        }
        if (diaBottom.isShowing()) {
            diaBottom.hide();
        } else {
            diaBottom.show();
        }
    }

    private IgnoreAdapter adapter;

    /**
     * BottomSheetDialog+Listview的滑动冲突问题
     */
    private void showDialog3() {
        List<AppBean> list = UApps.instance().getAppList();
        if (diaBottom == null) {
            View view = View.inflate(context, R.layout.dia_wifi_code, null);
            diaBottom = new BottomSheetDialog(context, R.style.TransparentBottomSheetStyle);
            ListView listView = view.findViewById(R.id.ig_list);
            adapter = new IgnoreAdapter(list, this);
            listView.setAdapter(adapter);
            diaBottom.setContentView(view);
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!listView.canScrollVertically(-1)) {
                        //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                        listView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        listView.requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }
            });
        }
        if (diaBottom.isShowing()) {
            diaBottom.hide();
        } else {
            diaBottom.show();
        }
    }
}
