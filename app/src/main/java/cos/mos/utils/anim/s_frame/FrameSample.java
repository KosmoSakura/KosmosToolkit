package cos.mos.utils.anim.s_frame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import cos.mos.utils.R;

/**
 * @Description 帧动画示例
 * @Author Kosmos
 * @Date 2019.04.08 17:37
 * @Email KosmoSakura@gmail.com
 */
public class FrameSample {
    private ImageView view;//动画的目标控件
    private Resources resources;
    private String pkgName;

    public FrameSample(Context context) {
        resources = context.getResources();
        pkgName = context.getPackageName();
    }

    private void run(AnimationDrawable animation) {
        view.setImageDrawable(animation);
        // 设置是否播放只播放一次:false代表循环播放；true代表只播放一次
        // 默认是播放一次的
        animation.setOneShot(false);
        // start方法开始播放
        animation.start();
    }

    /**
     * 载入资源动画
     */
    public void incluld() {
        AnimationDrawable animation = (AnimationDrawable) resources.getDrawable(R.drawable.anim_frame);
        run(animation);
    }

    /**
     * 纯代码
     */
    public void forJava() {
        AnimationDrawable animation = new AnimationDrawable();

        for (int i = 1; i < 9; i++) {
            // 拼接id名字
            int id = resources.getIdentifier("a" + i, "drawable", pkgName);
            animation.addFrame(resources.getDrawable(id), 150);
        }
        run(animation);
    }

    /**
     * 停止
     */
    private void stop() {
        AnimationDrawable animation = (AnimationDrawable) view.getDrawable();
        animation.stop();
    }
}
