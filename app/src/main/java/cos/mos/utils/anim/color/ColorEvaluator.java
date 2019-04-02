package cos.mos.utils.anim.color;

import android.animation.ArgbEvaluator;

/**
 * @Description: 颜色估计器
 * @Author: Kosmos
 * @Date: 2019.04.02 15:12
 * @Email: KosmoSakura@gmail.com
 */
public class ColorEvaluator extends ArgbEvaluator {
    /**
     * @param fraction   动画过渡时间因子，决定动画变化的速率，值为0-1之间
     * @param startValue 动画起始颜色
     * @param endValue   动画结束颜色
     * @apiNote 颜色演变核心算法
     * 这里直接copy父类ArgbEvaluator算法，笔记用
     */
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        //起始颜色ARGB颜色通道拆分
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >> 8) & 0xff) / 255.0f;
        float startB = (startInt & 0xff) / 255.0f;//透明度

        //结束颜色ARGB颜色通道拆分
        int endInt = (Integer) endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >> 8) & 0xff) / 255.0f;
        float endB = (endInt & 0xff) / 255.0f;//透明度


        // 颜色数值线性增加
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);

        // 根据时间因子，计算出过渡的颜色插值
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // 再将颜色转换回ARGB[0，255]
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;
        //将分离ARGB颜色通道打包装车
        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }
}
