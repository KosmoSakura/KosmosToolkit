package cos.mos.utils.widget.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Description: 缩放
 * @Author: Kosmos
 * @Date: 2019.01.23 18:26
 * @Email: KosmoSakura@gmail.com
 */
public class ScaleImageView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {
    private TouchScaleListener listener;
    private float scale = 0.9f;

    public void setOFF() {
        scale = 1f;
    }

    public void setScaleL() {
        this.scale = 0.9f;
    }

    public void setScaleM() {
        this.scale = 0.94f;
    }

    public void setScaleS() {
        this.scale = 0.99f;
    }

    public void setTouchScaleListener(TouchScaleListener listener) {
        this.listener = listener;
    }

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setScaleX(scale);
                setScaleY(scale);
                break;
            case MotionEvent.ACTION_UP:
                setScaleX(1);
                setScaleY(1);
                break;
        }
        if (listener != null) {
            //返回false，事件传递下去onClick会把ACTION_UP消耗掉
            listener.scaleTouch(v, event);
            return true;
        }
        return false;
    }
}
