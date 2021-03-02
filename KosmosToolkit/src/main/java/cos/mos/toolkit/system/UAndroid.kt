package cos.mos.toolkit.system

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cos.mos.toolkit.init.App

/**
 * @Description API统一调用
 * @Author Kosmos
 * @Date 2021.03.02 10:32
 * @Email KosmoSakura@gmail.com
 * @tip 避免SDK不同API兼容问题
 * @tip 2021.3.2 常用api封装
 * */
object UAndroid {
    //获取drawable
    fun drawable(ctx: Context, @DrawableRes resId: Int, @Nullable theme: Resources.Theme?): Drawable? = ResourcesCompat.getDrawable(ctx.resources, resId, theme)
    fun drawable(ctx: Context, @DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(ctx, resId)

    //设置TextView图标，不显示-传入0或null
    fun textIcons(text: TextView, @Nullable left: Drawable?, @Nullable top: Drawable?, @Nullable right: Drawable?, @Nullable bottom: Drawable?) {
        text.setCompoundDrawables(left, top, right, bottom)
    }

    fun textIcons(text: TextView, @DrawableRes left: Int, @DrawableRes top: Int, @DrawableRes right: Int, @DrawableRes bottom: Int) {
        text.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
    }

    //布局载入
    fun inflater(ctx: Context): LayoutInflater = LayoutInflater.from(ctx) //布局载入器
    fun inflater(ctx: Context, @LayoutRes ids: Int, @Nullable root: ViewGroup? = null): View = inflater(ctx).inflate(ids, root)

    //字符串
    fun str(ctx: Context?, @StringRes resId: Int): String = ctx?.getString(resId) ?: App.instance.getString(resId)

    //颜色
    fun color(ctx: Context?, @ColorRes ids: Int): Int = if (ctx == null) ContextCompat.getColor(App.instance, ids) else ContextCompat.getColor(ctx, ids)
}