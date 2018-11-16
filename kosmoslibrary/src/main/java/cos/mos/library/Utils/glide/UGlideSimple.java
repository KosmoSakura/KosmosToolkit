package cos.mos.library.Utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import cos.mos.library.R;
import cos.mos.library.Utils.UText;

/**
 * @Description: Glide普通加载
 * @Author: Kosmos
 * @Date: 2018年08月02日 23:59
 * @Email: KosmoSakura@gmail.com
 * @eg: 最新修改日期：2018年09月26日 14:24
 */
public class UGlideSimple {

    public static void loadImage(Context context, String url, ImageView tartgetIv) {
        loadImage(context, 0, url, tartgetIv, R.color.gray, R.color.gray_light);
    }

    public static void loadImage(Context context, int res, ImageView tartgetIv) {
        loadImage(context, res, "", tartgetIv, R.color.gray, R.color.gray);
    }

    public static void loadImagesRes(Context context, String url, ImageView tartgetIv) {
        int res;
        try {
            res = Integer.parseInt(url);
        } catch (NumberFormatException e) {
            res = 0;
        }
        loadImage(context, res, "", tartgetIv, R.color.gray, R.color.gray);
    }

    public static void loadImage(Context context, int res, String url, ImageView tartgetIv, int holder, int error) {
        GlideApp.with(context)
            .load(UText.isEmpty(url) ? res : url)
            .placeholder(holder)
            .error(error)
            .centerCrop()
            .into(tartgetIv);
    }

    public static void loadPop(Context context, String url, ImageView tartgetIv) {
        GlideApp.with(context)
            .load(url)
            .placeholder(R.color.gray)
            .error(R.color.gray_light)
            .dontAnimate()
            .into(tartgetIv);
    }

    public static void loadBitmap(Context context, Bitmap bmp, ImageView tartgetIv) {
        GlideApp.with(context)
            .load(bmp)
            .placeholder(R.color.gray_light)
            .error(R.color.gray_light)
            .centerCrop()
            .into(tartgetIv);
    }

}
