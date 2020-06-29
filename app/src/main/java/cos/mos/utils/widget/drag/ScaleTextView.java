package cos.mos.utils.widget.drag;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Description 缩放
 * @Author Kosmos
 * @Date 2019.01.23 18:26
 * @Email KosmoSakura@gmail.com
 */
public class ScaleTextView extends AppCompatTextView implements View.OnTouchListener {
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

    public ScaleTextView(Context context) {
        super(context);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
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
