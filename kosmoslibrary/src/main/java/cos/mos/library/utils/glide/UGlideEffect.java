package cos.mos.library.utils.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import cos.mos.library.R;
import cos.mos.library.utils.glide.transform.TransformBlur;
import cos.mos.library.utils.glide.transform.TransformCircle;
import cos.mos.library.utils.glide.transform.TransformRound;

/**
 * @Description: Glide特效方面
 * @Author: Kosmos
 * @Date: 2018.11.16 15:03
 * @Email: KosmoSakura@gmail.com
 */
public class UGlideEffect {
    //默认弧度:2
    public static void loadRound(Context context, String url, ImageView tartgetIv) {
        loadRound(context, 2, url, tartgetIv);
    }

    public static void loadCirle(Context context, String url, ImageView targetIv) {
        loadCirle(context, url, R.color.gray, R.color.gray_light, targetIv);
    }

    public static void loadRound(Context context, int radian, String url, ImageView tartgetIv) {
        GlideApp.with(context)
            .load(url)
            .placeholder(R.color.gray)
            .error(R.color.gray_light)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(new TransformRound(radian)))
            .into(tartgetIv);
    }

    public static void loadCirle(Context context, String url, int placeholderResId, int errorResId, ImageView targetIv) {
        GlideApp.with(context)
            .load(url)
            .placeholder(placeholderResId)
            .error(errorResId)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(new TransformCircle()))
            .into(targetIv);
    }

    public static void loadBlurPic(Context context, String string, ImageView tartgetIv) {
        GlideApp.with(context)
            .load(string)
            .placeholder(R.color.white_bg)
            .error(R.color.white_bg)
            .transform(new TransformBlur(context, 12))
            .into(tartgetIv);
    }

    /**
     * @apiNote 过渡动画执行时机:
     * 1.图片在磁盘缓存
     * 2.图片在本地
     * 3.图片在远程
     * 通常的用法如下
     */
    public static void loadAnim(Context context, String url, ImageView view) {
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view);
    }

    /**
     * @param animRes :R.anim.fade_in
     * @apiNote 过渡动画执行时机:
     * 1.图片在磁盘缓存
     * 2.图片在本地
     * 3.图片在远程
     * 如果图片在内存缓存上是不会执行过渡动画的
     */
    public static void loadAnim(Context context, String url, int animRes, ImageView view) {
        GlideApp.with(context)
            .load(url)
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (dataSource == DataSource.MEMORY_CACHE) {
                        //当图片位于内存缓存时，glide默认不会加载动画
                        view.startAnimation(AnimationUtils.loadAnimation(context, animRes));
                    }
                    return false;
                }

            }).fitCenter()
            .transition(GenericTransitionOptions.with(animRes))
            .into(view);
    }
}
