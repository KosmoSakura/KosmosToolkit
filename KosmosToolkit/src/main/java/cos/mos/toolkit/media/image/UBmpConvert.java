package cos.mos.toolkit.media.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import cos.mos.utils.init.k.KApp;

/**
 * @Description: Bitmap转换
 * @Author: Kosmos
 * @Date: 2019.03.19 11:18
 * @Email: KosmoSakura@gmail.com
 */
public class UBmpConvert {
    private static Resources res;

    private static Resources getRes() {
        if (res == null) {
            res = KApp.instance().getResources();
        }
        return res;
    }

    /**
     * @param resId R.mipmap.ic_launcher
     * @return bmp
     */
    public static Bitmap getBmpByResources(int resId) {
        return BitmapFactory.decodeResource(getRes(), resId);
    }

    /**
     * @param resId resId R.mipmap.ic_launcher
     * @return bmp
     */
    public static Drawable getDrawableByResources(int resId) {
        return getRes().getDrawable(resId);

    }

    public static Drawable getDrawbleByBmp(Bitmap bitmap) {
        return new BitmapDrawable(getRes(), bitmap);
    }

    public static Bitmap getBmpByDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
            drawable.getIntrinsicHeight(),
            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //设置绘画的边界，此处表示完整绘制
        drawable.draw(canvas);
        return bitmap;
    }
}
