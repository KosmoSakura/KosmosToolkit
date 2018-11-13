package cos.mos.library.Utils.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import androidx.annotation.NonNull;
import cos.mos.library.Utils.UIO;


/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月14日 22:52
 * @Email: KosmoSakura@gmail.com
 */
@GlideModule
public final class GlideConfigModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(UIO.getExternalCacheDir(context), 100 * 1024 * 1024));
    }
}