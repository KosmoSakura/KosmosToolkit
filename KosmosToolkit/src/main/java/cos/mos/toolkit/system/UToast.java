package cos.mos.toolkit.system;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cos.mos.toolkit.R;
import cos.mos.toolkit.init.App;


/**
 * @Description 可以连续弹出，没有冷却时间
 * @Author Kosmos
 * @Date 2019.03.21 11:34
 * @Email KosmoSakura@gmail.com
 */
public class UToast {
    public static void show(CharSequence sequence) {
        builder().build(App.instance, sequence, Toast.LENGTH_SHORT);
    }

    private static UToast uToast;

    private UToast() { }

    private static UToast builder() {
        if (uToast == null) {
            uToast = new UToast();
        }
        return uToast;
    }

    private volatile Toast toast;
    private volatile View rootView;
    private volatile TextView text;

    public void build(Context context, CharSequence sequence, int duration) {
        if (text == null) {
            if (rootView == null) {
                rootView = LayoutInflater.from(context).inflate(R.layout.lay_toast, null);
            }
            text = rootView.findViewById(R.id.tv_toast);
        }
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setView(rootView);
            toast.setDuration(duration);
        }
        text.setText(sequence);
        toast.show();
    }
}
