package cos.mos.utils.ui;

import android.graphics.Bitmap;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import cos.mos.library.init.KActivity;
import cos.mos.utils.R;

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
            View view = View.inflate(context, R.layout.dialog_progress, null);
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
