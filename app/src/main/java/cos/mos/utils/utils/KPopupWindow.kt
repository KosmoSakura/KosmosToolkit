package cos.mos.utils.utils

import android.app.Activity
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * @Description pop的一个简易封装
 * @Author Kosmos
 * @Date 2019.12.20 14:36
 * @Email KosmoSakura@gmail.com
 * @Tip 解决弹出时，变暗区域覆盖全屏
 * */
class KPopupWindow(context: Activity, layId: Int) : PopupWindow(context) {
    private var statusBarHeight = 0
    private var act = context
    private var rootView: View? = View.inflate(act, layId, null)
    fun popupInit(): View? {
        rootView?.setOnClickListener { dismiss() }
        this.contentView = rootView
        this.width = WindowManager.LayoutParams.MATCH_PARENT
        this.height = WindowManager.LayoutParams.MATCH_PARENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        this.update()
        this.setBackgroundDrawable(ColorDrawable())
        this.inputMethodMode = INPUT_METHOD_NEEDED
        return rootView
    }

    /**
     * @param anchor View 依附的控件
     * @Tip 解决弹出时，变暗区域覆盖全屏
     */
    fun popupShowDrop(anchor: View, xoff: Int = 0, yoff: Int = 0, gravity: Int = Gravity.NO_GRAVITY) {
        if (isShowing) return
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor.getGlobalVisibleRect(rect)
            if (statusBarHeight <= 0) {
                val res = act.resources
                val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    statusBarHeight = res.getDimensionPixelSize(resourceId)
                }
            }
            this.height = anchor.resources.displayMetrics.heightPixels - rect.bottom - yoff + statusBarHeight
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    fun popupShowAt(xoff: Int = 0, yoff: Int = 0, gravity: Int = Gravity.TOP) {
        if (isShowing) return
        showAtLocation(act.window.decorView, xoff, yoff, gravity)
    }
}