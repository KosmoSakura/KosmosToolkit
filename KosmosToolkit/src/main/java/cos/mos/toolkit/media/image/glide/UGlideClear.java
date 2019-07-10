package cos.mos.toolkit.media.image.glide;

import android.content.Context;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.15 14:48
 * @Email: KosmoSakura@gmail.com
 */
public class UGlideClear {
    /**
     * @apiNote 清理内存缓存，在主线程调用：
     */
    public void clearMemory(Context context) {
        GlideApp.get(context).clearMemory();
    }

    /**
     * @apiNote 清理磁盘缓存，在子线程调用：
     */
    public void clearDisk(Context context) {
        GlideApp.get(context).clearDiskCache();
    }

}
