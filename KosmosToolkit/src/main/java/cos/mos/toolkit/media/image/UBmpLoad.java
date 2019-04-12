package cos.mos.toolkit.media.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cos.mos.toolkit.system.UScreen;

/**
 * @Description: 关于位图处理
 * @Author: Kosmos
 * @Date: 2019.01.23 17:29
 * @Email: KosmoSakura@gmail.com
 */
public class UBmpLoad {
    private static UBmpLoad load;
    private BitmapFactory.Options opt;

    private UBmpLoad() {
    }

    public static UBmpLoad getInstance() {
        if (load == null) {
            load = new UBmpLoad();
        }
        return load;
    }

    public BitmapFactory.Options getOption() {
        if (opt == null) {
            opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        return opt;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * 仅限于：
     * 图片：1080*1920
     * 控件：1080*1920
     */
    public Bitmap loadBG(Resources res, int resId) {
        getOption().inPurgeable = true;
        getOption().inInputShareable = true;
        //decodeStream是直接调用jni完成decode
        return BitmapFactory.decodeStream(res.openRawResource(resId), null, opt);
    }

    public Bitmap decodeBmpDP(Resources res, int resId, int withDp, int heightDp) {
        return decodeBmp(res, resId, UScreen.dp2px(withDp), UScreen.dp2px(heightDp));
    }

    public Bitmap decodeBmp(Resources res, int resId, float with, float height) {
        //inJustDecodeBounds设置为true，不获取图片，不分配内存，但会返回图片的高宽度信息
        getOption().inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, getOption());
        // 计算inSampleSize值
        calculateSize(with, height);
        // 使用获取到的inSampleSize值再次解析图片
        getOption().inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, getOption());
    }

    /**
     * 计算尺寸
     */
    private void calculateSize(float reqWidth, float reqHeight) {
        // 源图片的高度和宽度
        final float height = getOption().outHeight;
        final float width = getOption().outWidth;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round(height / reqHeight);
            final int widthRatio = Math.round(width / reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            getOption().inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        } else {
            getOption().inSampleSize = 1;
        }
    }
}
