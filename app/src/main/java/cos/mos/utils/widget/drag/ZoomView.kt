package cos.mos.utils.widget.drag

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sign

/**
 * @Description 手势缩放
 * @Author Kosmos
 * @Date 2020.03.25 00:18
 * @Email KosmoSakura@gmail.com
 * @tip 2020.8.20 优化手势判断，小地图事件，和小地图显示时机
 * @tip 2020.12.29 缩放重置
 * */
class ZoomView : FrameLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    /**
     * 缩放监听
     */
    interface ZoomViewListener {
        fun onZoomStarted(zoom: Float, zoomx: Float, zoomy: Float)
    }

    private val zoomOriginal = 1.0f //原始状态倍率
    private val doubleZoom = 2f//双倍缩放比例
    private val tripleZoom = 3f//2.5倍缩放比例
    private val quadraZoom = 10f //4最大缩放比例
    private val maxZoom = doubleZoom * 16 //最大缩放比例
    private val minZoom = zoomOriginal //最小缩放比例,小于1即可在缩小

    private var zoom = zoomOriginal//放大倍数
    private var smoothZoom = zoomOriginal//当前放大倍数
    private var zoomX = 0f
    private var zoomY = 0f
    private var smoothZoomX = 0f//缩放中心点x
    private var smoothZoomY = 0f//缩放中心点y
    private var scrolling = false

    // 小地图变量
    private var showMinimap = true//小地图：总开关
    private var miniMapColor = Color.GRAY//小地图：颜色
    private var miniMapHeight = -1//小地图：高度
    private var miniMapCaption: String? = "航海日志"//小地图：标题
    private var miniMapCaptionSize = 32.0f//小地图：尺寸
    private var miniMapCaptionColor = Color.WHITE//小地图：标题颜色

    // 触摸变量
    private var lastTapTime: Long = 0
    private var touchStartX = 0f
    private var touchStartY = 0f
    private var touchLastX = 0f
    private var touchLastY = 0f
    private var startd = 0f
    private var pinching = false
    private var lastd = 0f
    private var lastdx1 = 0f
    private var lastdy1 = 0f
    private var lastdx2 = 0f
    private var lastdy2 = 0f

    // 绘制
    private val m = Matrix()
    private val p = Paint()

    // 监听
    private var listener: ZoomViewListener? = null
    private var ch: Bitmap? = null

    fun zoomTo(zoom: Float, x: Float, y: Float) {
        this.zoom = zoom.coerceAtMost(maxZoom)
        zoomX = x
        zoomY = y
        smoothZoomTo(this.zoom, x, y)
    }

    fun zoomReset() {
        zoomTo(zoomOriginal, width / 2.0f, height / 2.0f)
    }

    fun zoomResetSmooth() {
        smoothZoomTo(zoomOriginal, width / 2.0f, height / 2.0f)
    }

    private fun smoothZoomTo(zoom: Float, x: Float, y: Float) {
        smoothZoom = clamp(minZoom, zoom, maxZoom)
        smoothZoomX = x
        smoothZoomY = y
        if (listener != null) {
            listener!!.onZoomStarted(smoothZoom, x, y)
        }
    }

    fun setListner(listener: ZoomViewListener?) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount == 1) {
            processSingleTouchEvent(ev)//单点触控
        }
        if (ev.pointerCount == 2) {
            processDoubleTouchEvent(ev)//双点触控
        }
        rootView.invalidate() //重绘
        invalidate()
        return true
    }

    //单点触控
    private fun processSingleTouchEvent(ev: MotionEvent) {
        val x = ev.x
        val y = ev.y
        val w = miniMapHeight * width.toFloat() / height
        val h = miniMapHeight.toFloat()
        val touchingMiniMap = x >= 10.0f && x <= 10.0f + w && y >= 10.0f && y <= 10.0f + h
        if (showMinimap && smoothZoom > zoomOriginal && touchingMiniMap) {
            processSingleTouchOnMinimap(ev)
        } else processSingleTouchOutsideMinimap(ev)
    }

    //小地图区域的单点触控
    private fun processSingleTouchOnMinimap(ev: MotionEvent) {
        val x = ev.x
        val y = ev.y
        val w = miniMapHeight * width.toFloat() / height
        val h = miniMapHeight.toFloat()
        val zx = (x - 10.0f) / w * width
        val zy = (y - 10.0f) / h * height
        smoothZoomTo(smoothZoom, zx, zy)
    }

    //小地图以外区域的单点触控
    private fun processSingleTouchOutsideMinimap(ev: MotionEvent) {
        val x = ev.x
        val y = ev.y
        val lx = x - touchStartX
        val ly = y - touchStartY
        val l = hypot(lx.toDouble(), ly.toDouble()).toFloat()
        val dx = x - touchLastX
        val dy = y - touchLastY
        touchLastX = x
        touchLastY = y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = x
                touchStartY = y
                touchLastX = x
                touchLastY = y
                scrolling = false
            }
            MotionEvent.ACTION_MOVE -> if (scrolling || smoothZoom > zoomOriginal && l > 30.0f) {
                if (!scrolling) {
                    scrolling = true
                    ev.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(ev)
                }
                smoothZoomX -= dx / zoom
                smoothZoomY -= dy / zoom
                return
            }
            MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_UP -> if (l < 30.0f) {
                // 检查双击
                if (System.currentTimeMillis() - lastTapTime < 500) {
                    when (smoothZoom) {
                        zoomOriginal -> smoothZoomTo(doubleZoom, x, y)
                        doubleZoom -> smoothZoomTo(tripleZoom, x, y)
                        else -> smoothZoomTo(zoomOriginal, width / 2.0f, height / 2.0f)
                    }
                    lastTapTime = 0
                    ev.action = MotionEvent.ACTION_CANCEL
                    super.dispatchTouchEvent(ev)
                    return
                }
                lastTapTime = System.currentTimeMillis()
                performClick()
            }
        }
        ev.setLocation(zoomX + (x - 0.5f * width) / zoom, zoomY + (y - 0.5f * height) / zoom)
        ev.x
        ev.y
        super.dispatchTouchEvent(ev)
    }

    //双点触控
    private fun processDoubleTouchEvent(ev: MotionEvent) {
        val x1 = ev.getX(0)
        val dx1 = x1 - lastdx1
        lastdx1 = x1
        val y1 = ev.getY(0)
        val dy1 = y1 - lastdy1
        lastdy1 = y1
        val x2 = ev.getX(1)
        val dx2 = x2 - lastdx2
        lastdx2 = x2
        val y2 = ev.getY(1)
        val dy2 = y2 - lastdy2
        lastdy2 = y2
        // 指针距离
        val dist = hypot(x2 - x1.toDouble(), y2 - y1.toDouble()).toFloat()
        val distDiff = dist - lastd//上次距离差
        lastd = dist
        val ld = abs(dist - startd)
        atan2(y2 - y1.toDouble(), x2 - x1.toDouble())
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startd = dist
                pinching = false
            }
            MotionEvent.ACTION_MOVE -> if (pinching || ld > 30.0f) {
                pinching = true
                val dxk = 0.5f * (dx1 + dx2)
                val dyk = 0.5f * (dy1 + dy2)
                smoothZoomTo(minZoom.coerceAtLeast(zoom * dist / (dist - distDiff)), zoomX - dxk / zoom, zoomY - dyk / zoom)
            }
            MotionEvent.ACTION_UP -> pinching = false
            else -> pinching = false
        }
        ev.action = MotionEvent.ACTION_CANCEL
        super.dispatchTouchEvent(ev)
    }

    private fun clamp(min: Float, value: Float, max: Float) = min.coerceAtLeast(value.coerceAtMost(max))

    private fun lerp(a: Float, b: Float, k: Float) = a + (b - a) * k

    private fun bias(a: Float, b: Float, k: Float) = if (abs(b - a) >= k) a + k * sign(b - a) else b

    override fun dispatchDraw(canvas: Canvas) {
        // 开始缩放
        zoom = lerp(bias(zoom, smoothZoom, 0.05f), smoothZoom, 0.2f)
        smoothZoomX = clamp(0.5f * width / smoothZoom, smoothZoomX, width - 0.5f * width / smoothZoom)
        smoothZoomY = clamp(0.5f * height / smoothZoom, smoothZoomY, height - 0.5f * height / smoothZoom)

        zoomX = lerp(bias(zoomX, smoothZoomX, 0.1f), smoothZoomX, 0.35f)
        zoomY = lerp(bias(zoomY, smoothZoomY, 0.1f), smoothZoomY, 0.35f)

        val animating = abs(zoom - smoothZoom) > 0.0000001f || abs(zoomX - smoothZoomX) > 0.0000001f
                || abs(zoomY - smoothZoomY) > 0.0000001f
        // 不绘制
        if (childCount == 0) return
        // 构建矩阵
        m.setTranslate(0.5f * width, 0.5f * height)
        m.preScale(zoom, zoom)
        m.preTranslate(-clamp(0.5f * width / zoom, zoomX, width - 0.5f * width / zoom),
                -clamp(0.5f * height / zoom, zoomY, height - 0.5f * height / zoom))
        // 获取视图
        val v = getChildAt(0)
        m.preTranslate(v.left.toFloat(), v.top.toFloat())
        // 获取绘制缓存
        if (animating && ch == null && isAnimationCacheEnabled) {
            v.isDrawingCacheEnabled = true
            ch = v.drawingCache
        }
        // 动画缓存
        if (animating && isAnimationCacheEnabled && ch != null) {
            p.color = -0x1
            canvas.drawBitmap(ch!!, m, p)
        } else {
            // 缓存不可用
            ch = null
            canvas.save()
            canvas.concat(m)
            v.draw(canvas)
            canvas.restore()
        }
        // 绘制小地图
        if (showMinimap && smoothZoom > zoomOriginal) {
            if (miniMapHeight < 0) {
                miniMapHeight = height / 4
            }
            canvas.translate(10f, 10f)
            p.color = -0x80000000 or 0x00ffffff and miniMapColor
            val w = miniMapHeight * width.toFloat() / height
            val h = miniMapHeight.toFloat()
            canvas.drawRect(0.0f, 0.0f, w, h, p)//左上角
            if (miniMapCaption != null && miniMapCaption!!.isNotEmpty()) {
                p.textSize = miniMapCaptionSize
                p.color = miniMapCaptionColor
                p.isAntiAlias = true
                canvas.drawText(miniMapCaption!!, 10.0f, 10.0f + miniMapCaptionSize, p)//左上角
                p.isAntiAlias = false
            }
            p.color = -0x80000000 or 0x00ffffff and miniMapColor
            val dx = w * zoomX / width
            val dy = h * zoomY / height
            canvas.drawRect(dx - 0.5f * w / zoom, dy - 0.5f * h / zoom, dx + 0.5f * w / zoom, dy + 0.5f * h / zoom, p)//左上角
            canvas.translate(-10f, -10f)
        }
        // 重绘
        rootView.invalidate()
        invalidate()
    }
}