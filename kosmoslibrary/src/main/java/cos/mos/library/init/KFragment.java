package cos.mos.library.init;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018年08月02日 15:01
 * @Email: KosmoSakura@gmail.com
 */
public abstract class KFragment extends Fragment {
    protected Context context;
    protected CompositeDisposable compositeDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(layout(), container, false);
        init();
        logic();
        return contentView;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
