package cos.mos.utils.ui;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import cos.mos.utils.initial.BaseActivity;

/**
 * @Description 拖拽Api示例
 * @Author Kosmos
 * @Date 2019年1月29日
 * @Email KosmoSakura@gmail.com
 * @Tip 关于A拖到B位置的处理
 * 关于目标View的设置：
 * 这里不存在目标View的设置，
 * onDrag只有拖拽到 所有设置了setOnDragListener的View区域时才会回调
 * 且onDrag中的参数View，是设置OnDrag的View，而不是被拖拽的View
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
        findViewById(id1).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                view.performClick();
                /**
                 *ClipData data：存放数据的剪切板对象
                 *DragShadowBuilder shadowBuilder：拖拽时原控件的影子
                 * * 封装了拖拽中各种事件和控件的位置信息等
                 * * 默认情况下，该影子与被拖拽的图形尺寸相同
                 * * 影子图形的中心点=被拖拽对象的中心点
                 *Object myLocalState：被拖动对象与目标对象之间传递数据的轻量级结构对象
                 *int flags：用于控制拖拽操作的类型，当前 该标志尚未定义，设置0即可
                 * */
                view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
            }
            return false;
        });
        /**
         * View view：为当前操作对象（是影子进入的那个控件）
         * DragEvent event:
         * * event.getAction():当前拖拽状态
         * * event.getX()、getY()：拖拽对象的横纵坐标(区别于view.getX():被拖拽控件的坐标)
         * * event.getClipData()：在ACTION_DROP,拖拽放开后，才能返回startDrag()第一个参数对象
         * * event.getLocalState():获取startDrag()中的第三个参数
         * **********
         * 返回值：
         * * false：将不会接受后续的拖动事件
         * * true：会继续接受后续的拖动事件
         * */
        findViewById(id2).setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // 拖拽开始:startDrag()之后，获得拖拽影子后回调
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
