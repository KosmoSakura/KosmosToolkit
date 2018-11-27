package cos.mos.utils.tool;

import android.content.Context;
import android.widget.ImageView;

import cos.mos.library.utils.glide.GlideApp;
import cos.mos.utils.R;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.14 13:56
 * @Email: KosmoSakura@gmail.com
 */
public class TGlide {
    public static void loadStaggered(Context context, String url, ImageView tartgetIv) {
        GlideApp.with(context)
            .load(url)
            .placeholder(R.drawable.ic_image_place)
            .error(R.drawable.ic_image_error)
            .centerCrop()
            .into(tartgetIv);
//        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                float scale = resource.getIntrinsicWidth() / (float) resource.getIntrinsicHeight();
//                girlBean.setScale(scale);
//                notifyDataSetChanged();
//            }
//        });
    }
}
