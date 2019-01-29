package cos.mos.utils.ui;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import cos.mos.utils.init.BaseActivity;


/**
 * 关于A拖到B位置的处理
 * 注意：
 * setOnDragListener的所有分子都要返回true才行
 */
public class DragTip extends BaseActivity {
    private boolean inRight = false;
    private int id1, id2;

    @Override
    protected int layout() {
        return -1;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void logic() {
        findViewById(id1).setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.performClick();
                v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
            }
            return false;
        });
        findViewById(id2).setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // 拖拽开始:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // 被拖拽View进入目标区域:
                    inRight = true;
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // 被拖拽View在目标区域移动:
                    inRight = true;
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    // 被拖拽View离开目标区域:
                    inRight = false;
                    return true;
                case DragEvent.ACTION_DROP:
                    // 放开被拖拽View
                    if (inRight) {
                        //doSth
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // 拖拽完成
                    return true;
            }
            return false;
        });
    }

}
