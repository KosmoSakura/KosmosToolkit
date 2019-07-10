package cos.mos.toolkit.media.image.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;


/**
 * @Description: 缓存配置
 * @Author: Kosmos
 * @Date: 2018年08月14日 22:52
 * @Email: KosmoSakura@gmail.com
 * **************************
 * 1.引入库
 * implementation 'com.github.bumptech.glide:glide:4.8.0'
 * annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
 * 2.权限
 * <uses-permission android:name="android.permission.INTERNET" />
 */
@GlideModule
public final class GlideConfigModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        int diskCacheSizeBytes = 1024 * 1024 * 100;  //100 MB
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes))
            .setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
//        builder.setDiskCache(new DiskLruCacheFactory(getExternalCacheDir(context), 100 * 1024 * 1024));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * 获取缓存目录
     */
    private String getExternalCacheDir(Context context) {
        File externalFilesDir = context.getExternalCacheDir();
        if (externalFilesDir != null) {
            return externalFilesDir.getPath();
        }
        return "";
    }
}