//package cos.mos.toolkit.media.image.glide;
//
//import android.content.Context;
//
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
//import com.bumptech.glide.module.AppGlideModule;
//
//import java.io.File;
//
//import androidx.annotation.NonNull;
//
//
///**
// * @Description: 缓存配置
// * @Author: Kosmos
// * @Date: 2018年08月14日 22:52
// * @Email: KosmoSakura@gmail.com
// */
//@GlideModule
//public final class GlideConfigModule extends AppGlideModule {
//    @Override
//    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
//        //内存缓存：100M
//        builder.setDiskCache(new DiskLruCacheFactory(getExternalCacheDir(context), 100 * 1024 * 1024));
//    }
//
//    /**
//     * 获取缓存目录
//     */
//    private String getExternalCacheDir(Context context) {
//        File externalFilesDir = context.getExternalCacheDir();
//        if (externalFilesDir != null) {
//            return externalFilesDir.getPath();
//        }
//        return "";
//    }
//}