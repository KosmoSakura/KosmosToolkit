package cos.mos.utils.kotlin_laboratory

import cos.mos.utils.widget.list.SideBean

/**
 * @Description 内联函数示例
 * @Author Kosmos
 * @Date 2019.10.17 17:36
 * @Email KosmoSakura@gmail.com
 * @Tip
 * */
class NotInline {
    /**
     * @param let 关键字示例
     * @Tip
     * 1.返回值:为函数块的最后一行 (闭包形式返回)
     * 2.指代当前对象：it
     * 3.适用
     * * 对一个可null对象作统一非空判断
     */
    private fun aboutLet(let: SideBean?) {
        let?.let {
            print("输处：ID=${it.imageId},\nUrl=${it.url}")
        }
    }

    /**
     * @param also 关键字示例
     * @Tip
     * 1.返回值:为传入对象本身(this)
     * 2.指代当前对象：it
     * 3.适用
     * * 3.1.和let很像，区别于返回对象
     * * 3.2.链式调用
     */
    private fun aboutAlso(also: SideBean?) {
        also?.also {
            print("输处：ID=${it.imageId},\nUrl=${it.url}")
        }
    }

    /**
     * @param with 关键字示例
     * @Tip
     * 1.返回值:为函数块的最后一行 (闭包形式返回)
     * 2.指代当前对象：this、或省略
     * 3.适用
     * * 3.1.同一类的多个方法 (如：RecyclerView中onBinderViewHolder)
     * * 3.2.数据model的属性映射到UI上
     */
    private fun aboutWith(with: SideBean) {
        with(with) {
            print("输处：ID=${imageId},\nUrl=${url}")
        }
    }

    /**
     * @param run 关键字示例
     * @Tip
     * 1.返回值:为函数块的最后一行 (闭包形式返回)
     * 2.指代当前对象：this、或省略
     * 3.适用
     * * let和with的任何场景
     */
    private fun aboutRun(run: SideBean?) {
        run?.run {
            print("输处：ID=${imageId},\nUrl=${url}")
        }
    }

    /**
     * @param apply 关键字示例
     * @Tip
     * 1.返回值：为传入对象本身 (this)
     * 2.指代当前对象：this、或省略
     * 3.适用
     * * 3.0.run、let、with的任何场景
     * * 3.1.操作对象属性，并返回这个对象(比如：findViewById之后属性设置再同一返回)
     * * 3.2.多层级判空问题 a?.apply{}.apply{}.apply{}
     * * 3.3.链式调用赋值
     */
    private fun aboutApply(apply: SideBean?) {
        apply?.apply {
            print("输处：ID=${imageId},\nUrl=${url}")
        }
    }


}