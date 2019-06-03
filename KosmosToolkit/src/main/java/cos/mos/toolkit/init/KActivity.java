package cos.mos.toolkit.init;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import cos.mos.toolkit.media.image.UBmpLoad;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月02日 13:17
 * @Email: KosmoSakura@gmail.com
 */
public abstract class KActivity extends AppCompatActivity {
    protected Context context;
    protected CompositeDisposable compositeDisposable;
    /**
     * 在哪里接收,在哪里注册
     */
    protected boolean initEventBus;//是否注册EventBus

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initEventBus = false;
        int layoutId = layout();
        if (layoutId != 0) {
            setContentView(layoutId);
        }
        init();
        logic();
        if (initEventBus) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * @return 返回布局
     */
    protected abstract int layout();

    /**
     * 初始化基础信息
     */
    protected abstract void init();

    /**
     * 填充逻辑部分 include
     */
    protected abstract void logic();

    protected void setBackgrounds(ImageView iBg, int id) {
        iBg.setImageBitmap(UBmpLoad.getInstance().loadBG(getResources(), id));
    }

    private BitmapFactory.Options opt;

    protected void setBackground(ImageView iBg, int id) {
        if (opt == null) {
            opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        iBg.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(id), null, opt));
    }

    protected void rxJava(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (initEventBus) {
            EventBus.getDefault().register(this);
        }
    }
}
