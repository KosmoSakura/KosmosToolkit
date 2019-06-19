package cos.mos.utils.initial

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @Description:
 * <p>
 * @Author: Kosmos
 * @Date: 2019.04.23 16:57
 * @Email: KosmoSakura@gmail.com
 */
abstract class KtFragment : Fragment() {
    private lateinit var contentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(layout(), container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        logic()
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T = contentView.findViewById(id)

    protected abstract fun layout(): Int
    protected abstract fun init()
    protected abstract fun logic()
}