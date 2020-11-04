package cos.mos.utils.widget

import android.graphics.*
import cos.mos.toolkit.ukotlin.toCos
import cos.mos.toolkit.ukotlin.toSin
import cos.mos.toolkit.ukotlin.toTan

/**
 * @Description 线条样式
 * @Author Kosmos
 * @Date 2020.10.15 13:02
 * @Email KosmoSakura@gmail.com
 * */
object Lines {
    val lineCorner10 = CornerPathEffect(10f)//圆角折现：path绘制，拐角度数

    //虚线
    val lineDottedNormal: PathEffect = DashPathEffect(floatArrayOf(10f, 10f), 1f) //虚线：标准虚线
    val lineDottedShort: PathEffect = DashPathEffect(floatArrayOf(5f, 5f), 1f)//虚线:短点虚线
    val lineDottedMiddle: PathEffect = DashPathEffect(floatArrayOf(20f, 20f), 1f)//虚线:短点虚线
    val lineDottedLong: PathEffect = DashPathEffect(floatArrayOf(30f, 30f), 1f)//虚线:短点虚线

    //点划线:线-点-线
    val linePointNormal: DashPathEffect = DashPathEffect(floatArrayOf(15f, 10f, 1f, 10f), 1f) //点划线:标准
    val linePointShort: DashPathEffect = DashPathEffect(floatArrayOf(10f, 5f, 1f, 5f), 1f) //点划线:短
    val linePointMiddle: DashPathEffect = DashPathEffect(floatArrayOf(20f, 10f, 1f, 10f), 1f) //点划线:中
    val linePointLong: DashPathEffect = DashPathEffect(floatArrayOf(40f, 20f, 1f, 20f), 1f) //点划线:长

    //线划线:长线-短点-长线
    val lineBreakNoamal: DashPathEffect = DashPathEffect(floatArrayOf(20f, 10f, 8f, 10f), 1f) //线划线:标准
    val lineBreakShort: DashPathEffect = DashPathEffect(floatArrayOf(10f, 5f, 4f, 5f), 1f) //线划线:短
    val lineBreakMiddle: DashPathEffect = DashPathEffect(floatArrayOf(30f, 20f, 10f, 20f), 1f) //线划线:中
    val lineBreakLong: DashPathEffect = DashPathEffect(floatArrayOf(40f, 20f, 16f, 20f), 1f) //点划线:长

    //折线
    val lineBrokenNormal = DiscretePathEffect(5f, 10f)//折线:标准
    val lineBrokenShort = DiscretePathEffect(2f, 10f)//折线:短折线
    val lineBrokenMiddle = DiscretePathEffect(10f, 10f)//折线:中折线
    val lineBrokenLong = DiscretePathEffect(20f, 10f)//折线:长折线

    private const val starRadius = 30f//星星边长(像素)
    val path: Path by lazy {
        val star = Path()
        val dis1 = starRadius * 0.5f / 54f.toTan()
        val dis2 = starRadius * 72f.toSin()
        val dis3 = starRadius * 72f.toCos()
        star.moveTo(starRadius * 0.5f, 0f)
        star.lineTo(starRadius * 0.5f - dis3, dis2)
        star.lineTo(starRadius, dis1)
        star.lineTo(0f, dis1)
        star.lineTo(starRadius * 0.5f + dis3, dis2)
        star.lineTo(starRadius * 0.5f, 0f)
        star
    }

    //星星线 精准 精确
    val lineStarPrecise = PathDashPathEffect(path, starRadius, 0f, PathDashPathEffect.Style.MORPH)//星星-精准线长：保持旋转角度，保证线条长度，末端可能绘制半个星星
    val lineStarRotate = PathDashPathEffect(path, starRadius, 0f, PathDashPathEffect.Style.ROTATE)//星星：保持旋转角度，保证星星完整，末端可能超出线条
    val lineStarAdjust = PathDashPathEffect(path, starRadius, 0f, PathDashPathEffect.Style.TRANSLATE)//星星：旋转复位，每个星星摆正，保证星星完
}