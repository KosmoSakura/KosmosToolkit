package cos.mos.utils.zkosmoslibrary.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description: Glide内存磁盘方面
 * @Author: Kosmos
 * @Date: 2018.11.16 15:06
 * @Email: KosmoSakura@gmail.com
 */
public class UGlideDisk {
    /**
     * @apiNote 跳过内存缓存
     */
    public static void loadSkipMemoryCache(Context context, String url, ImageView view) {
        GlideApp.with(context)
            .load(url)
            .skipMemoryCache(true)//跳过内存缓存
            .dontAnimate()
            .centerCrop()
            .into(view);
    }

    /**
     * @apiNote 设置磁盘缓存策略
     * 默认的策略是DiskCacheStrategy.AUTOMATIC
     * DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
     * DiskCacheStrategy.NONE 不使用磁盘缓存
     * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
     * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
     * DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
     */
    public static void loadDiskCache(Context context, String url, ImageView view) {
        GlideApp.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop()
            .into(view);
    }
}
