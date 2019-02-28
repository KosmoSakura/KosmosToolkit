package cos.mos.utils.init.k;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月02日 15:01
 * @Email: KosmoSakura@gmail.com
 */
public abstract class KFragment extends Fragment {
    protected Context context;
    protected CompositeDisposable compositeDisposable;
    private View contentView;
    /**
     * 在哪里接收,在哪里注册
     */
    protected boolean initEventBus;//是否注册EventBus

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(layout(), container, false);
        initEventBus = false;
        init();
        logic();
        if (initEventBus) {
            EventBus.getDefault().register(this);
        }
        return contentView;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return contentView.findViewById(id);
    }

    /**
     * @return true:返回键逻辑由Frag拦截处理，false：逻辑交由Activity处理
     * 栗子：对应activity的onBackPressed中，frags[0]为目标Fragment
     * if (frags[0] == null || !frags[0].onBackPressed()) {
     * super.onBackPressed();
     * }
     */
    public boolean onBackPressed() {
        return false;
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

    protected void rxDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (initEventBus) {
            EventBus.getDefault().register(this);
        }
    }
}
