package cos.mos.utils.ui;

import android.graphics.Bitmap;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import cos.mos.utils.R;
import cos.mos.utils.init.k.KActivity;

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

    private void showDialog(Bitmap bmp) {
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
}
