package cos.mos.toolkit.io;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import java.io.ByteArrayOutputStream;

import cos.mos.toolkit.init.KApp;


/**
 * @Description: Bitmap转换
 * @Author: Kosmos
 * @Date: 2019.03.19 11:18
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.5.8:新增几个bmp处理
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
     * @return resId->bmp
     */
    public static Bitmap resId2Bmp(int resId) {
        return BitmapFactory.decodeResource(getRes(), resId);
    }

    /**
     * @param resId     资源id
     * @param maxWidth  最大宽
     * @param maxHeight 最大高
     * @return resId->bmp
     */
    public static Bitmap resId2Bmp(@DrawableRes final int resId, final int maxWidth, final int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getRes(), resId, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getRes(), resId, options);
    }

    /**
     * @param resId resId R.mipmap.ic_launcher
     * @return resId->Drawable
     */
    public static Drawable resId2Drawable(int resId) {
        return getRes().getDrawable(resId);

    }

    /**
     * @return bitmap->Drawable
     */
    public static Drawable bmp2Drawable(Bitmap bitmap) {
        if (bitmap != null) {
            new BitmapDrawable(getRes(), bitmap);
        }
        return null;
    }

    /**
     * @return Drawable->bmp
     */
    public static Bitmap drawable2Bmp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                drawable.getOpacity() != PixelFormat.OPAQUE
                    ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE
                    ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //设置绘画的边界，此处表示完整绘制
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @param bitmap 待转换图
     * @param format 转换格式
     * @return bmp->byte数组
     */
    public static byte[] bmp2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * @param bytes 图像的byte数组
     * @return byte数组->bmp
     */
    public static Bitmap bytes2bmp(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
