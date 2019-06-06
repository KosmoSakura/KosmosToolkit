package cos.mos.toolkit.media.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * @Description: 创建bmp,页面截图
 * @Author: Kosmos
 * @Date: 2019.05.08 18:33
 * @Email: KosmoSakura@gmail.com
 */
public class UBmpCreate {

    /**
     * @param view view必须已经显示在界面上
     */
    public static Bitmap createByView(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        view.draw(c);
        return bmp;
    }

    /**
     * @param view view必须已经显示在界面上
     */
    public static Bitmap createByCache(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bmp;
    }

    /**
     * @param view view没有显示到界面上
     * @apiNote eg
     * View view = View.inflate(this, R.layout.share_out_layout, null);
     * View.MeasureSpec.UNSPECIFIED:父类没有任何约束，子类可能为任意大小（可能报：空指针）
     * View.MeasureSpec.EXACTLY：父类约束大小，子类大小不能大于父类
     */
    public static Bitmap createNoDisplay(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bmp;
    }
}
