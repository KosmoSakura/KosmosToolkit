package cos.mos.utils.laboratory.java_laboratory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cos.mos.utils.laboratory.justfortest.HttpListener;
import cos.mos.utils.laboratory.justfortest.ImageBackBean;
import cos.mos.utils.laboratory.justfortest.RunApi;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description fromIterable操作符示例
 * @Author Kosmos
 * @Date 2019.10.18 16:16
 * @Email KosmoSakura@gmail.com
 * @Tip
 */
public class RxFromIterable {
    /**
     * @param imgList 待传输列表
     * @Tip 假如场景
     * 1.待传输图片N张
     * 2.传输接口只能单张传
     * 3.需要按传入顺序返回
     */
    public void manyImageWithSinglePush(List<String> imgList) {
        ArrayList<String> outList = new ArrayList<>();
        Disposable subscribe = Observable
            // 依次发送list中的数据
            .fromIterable(imgList)
            // 依次发送list中的数据(flatMap代替concatMap是为了确保图片顺序)
            .concatMap(new Function<String, ObservableSource<String>>() {
                @Override
                public ObservableSource<String> apply(String s) throws Exception {
                    //这里返回一个Observable对象，恰好可以用来请求数据
                    return Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            //假如这里在请求数据
                            RunApi.imageUpload(s, new HttpListener<ImageBackBean>() {
                                @Override
                                public void httpFail(@NotNull String describe) {
                                    emitter.onError(new IOException("挂了"));
                                }

                                @Override
                                public void httpSucceed(ImageBackBean out) {
                                    emitter.onNext(out.getSrc());
                                    emitter.onComplete();
                                }
                            });
                        }
                    });
                }
            })
            // 线程切换
            .compose(observableIoSchedulers(0))
//            .toList() //toList可以把结果打包输出，加上的话，这里为：Consumer<List<String>>
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    outList.add(s);
                    if (outList.size() == imgList.size()) {
                        System.out.println("传输完成");
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    System.out.println("有图片传输失败");
                }
            });
    }

    private <T> ObservableTransformer<T, T> observableIoSchedulers(long time) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .retry(time);
            }
        };
    }
}
