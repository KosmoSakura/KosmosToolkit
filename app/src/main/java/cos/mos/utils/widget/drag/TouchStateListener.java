package cos.mos.utils.widget.drag;

import android.view.MotionEvent;

/**
 * @Description 触摸状态监听
 * @Author Kosmos
 * @Date 2019.03.25 18:27
 * @Email KosmoSakura@gmail.com
 */
public interface TouchStateListener<T> {
    void state(T view, MotionEvent event);
}
