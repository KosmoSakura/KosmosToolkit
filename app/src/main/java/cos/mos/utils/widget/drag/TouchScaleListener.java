package cos.mos.utils.widget.drag;

import android.view.MotionEvent;
import android.view.View;

/**
 * @Description: 缩放监听
 * @Author: Kosmos
 * @Date: 2019.03.25 16:31
 * @Email: KosmoSakura@gmail.com
 */
public interface TouchScaleListener {
    void scaleTouch(View view, MotionEvent event);
}
