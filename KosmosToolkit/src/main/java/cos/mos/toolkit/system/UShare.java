package cos.mos.toolkit.system;

import android.content.Intent;
import android.net.Uri;

import cos.mos.toolkit.init.KApp;
import cos.mos.toolkit.java.UText;
import cos.mos.toolkit.ui.toast.ToastUtil;

/**
 * @Description: 分享工具
 * @Author: Kosmos
 * @Date: 2019.06.06 15:23
 * @Email: KosmoSakura@gmail.com
 */
public class UShare {
    private static void start(Intent intent) {
        KApp.instance().startActivity(intent);
    }

    /**
     * @param title   分享标题
     * @param content 分享内容
     * @apiNote 分享文字
     */
    public static void shareText(String title, String content) {
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
        start(share_intent);
    }

    /**
     * @param title 分享标题
     * @param imgPath 图片地址
     */
    public static void shareImage(String title, String imgPath) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/*");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgPath));
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, title);
        start(share_intent);
    }
}
