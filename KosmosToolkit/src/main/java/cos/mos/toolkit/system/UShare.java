package cos.mos.toolkit.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

import cos.mos.toolkit.java.UText;
import cos.mos.toolkit.ui.toast.ToastUtil;

/**
 * @Description: 分享工具
 * @Author: Kosmos
 * @Date: 2019.06.06 15:23
 * @Email: KosmoSakura@gmail.com
 */
public class UShare {
    /**
     * @param title   分享标题
     * @param content 分享内容
     * @apiNote 分享文字
     */
    public static void shareText(String title, String content, Context context) {
        if (UText.isEmpty(title) || UText.isEmpty(content)) {
            ToastUtil.show("Data abnormity !");
            return;
        }
        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);
        share_intent.putExtra(Intent.EXTRA_TEXT, content);
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, title);
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(share_intent);
    }

    /**
     * @param title   分享标题
     * @param content 分享内容
     * @param imgPath 图片绝对地址
     * @apiNote 图文分享路径
     * UBmpCreate.createByCache()
     * imgPath= UBmpCreate.bmpSave()
     */
    public static void shareImage(String title, String content, String imgPath, Context context) throws IOException {
        String imageUri = MediaStore.Images.Media
            .insertImage(context.getContentResolver(), imgPath,
                "share_image", "share_image");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, title));
//        context.startActivity(shareIntent);//不带标题
    }
}
