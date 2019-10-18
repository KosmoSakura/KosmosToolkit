package cos.mos.utils.laboratory.kotlin_laboratory

import cos.mos.utils.laboratory.justfortest.HttpListener
import cos.mos.utils.laboratory.justfortest.ImageBackBean
import cos.mos.utils.laboratory.justfortest.RunApi
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*

/**
 * @Description fromIterable操作符示例
 * @Author Kosmos
 * @Date 2019.10.18 16:42
 * @Email KosmoSakura@gmail.com
 * */
class RxFromIterable {
    /**
     * @param imgList 待传输列表
     * @Tip 假如场景
     * 1.待传输图片N张
     * 2.传输接口只能单张传
     * 3.需要按传入顺序返回
     */
    fun manyImageWithSinglePush(imgList: List<String>) {
        val outList = ArrayList<String>()
        var subscribe = Observable
            // 依次发送list中的数据
            .fromIterable(imgList)
            // 依次发送list中的数据(flatMap代替concatMap是为了确保图片顺序)
            .concatMap(Function<String, ObservableSource<String>> { s ->
                //这里返回一个Observable对象，恰好可以用来请求数据
                Observable.create { emitter ->
                    //假如这里在请求数据
                    RunApi.imageUpload(s, object : HttpListener<ImageBackBean> {
                        override fun httpSucceed(bean: ImageBackBean) {
                            emitter.onNext(bean.name)
                            emitter.onComplete()
                        }

                        override fun httpFail(describe: String) {
                            emitter.onError(IOException("挂了"))
                        }
                    })
                }
            })
            // 线程切换
            .compose(observableIoSchedulers())
//            .toList() //toList可以把结果打包输出，加上的话，这里为：Consumer<List<String>>
            .subscribe(object : Consumer<String> {
                override fun accept(t: String?) {
                    outList.add(t!!)
                    if (outList.size == imgList.size) {
                        println("传输完成")
                    }
                }
            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {
                    println("有图片传输失败")
                }

            })
    }

    private fun <T> observableIoSchedulers(times: Long = 0): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .retry(times)
        }
    }
}