package cos.mos.utils.ui.qr_code;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cos.mos.library.Utils.UIO;
import cos.mos.library.Utils.UText;
import cos.mos.library.Utils.toast.UToast;
import cos.mos.library.init.KFragment;
import cos.mos.library.listener.KTextWatcher;
import cos.mos.utils.R;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @Description: 二维码生成
 * @Author: Kosmos
 * @Date: 2018.11.20 15:43
 * @Email: KosmoSakura@gmail.com
 */
public class QRGenerateFragment extends KFragment implements View.OnClickListener {
    private EditText edtInput;
    private ImageView iSwitch, iShow;
    private Bitmap bmp;//保存到sd卡用的bmp
    /**
     * 延时执行
     */
    private PublishSubject<String> mSubject = PublishSubject.create();

    @Override
    protected int layout() {
        return R.layout.frag_qr_generate;
    }

    @Override
    protected void init() {
        edtInput = findViewById(R.id.gen_input);
        iSwitch = findViewById(R.id.gen_switch);
        iShow = findViewById(R.id.gen_show);
        findViewById(R.id.gen_save).setOnClickListener(this);
        iSwitch.setOnClickListener(this);
        iSwitch.setSelected(false);//false：二维码
        edtInput.setText(null);
        showEmptyStyle();
    }

    @Override
    protected void logic() {
        edtInput.addTextChangedListener(new KTextWatcher() {
            @Override
            public void afterTextChanged(Editable txtEnd) {
                String text = txtEnd.toString();
                if (UText.isEmpty(text)) {
                    showEmptyStyle();
                } else {
                    //发送数据源
                    mSubject.onNext(text);
                }
            }
        });
        mSubject.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .doOnNext(this::creat).subscribe();
    }

    private void creat(String txt) {
        Disposable subscribe = Observable
            .create((ObservableOnSubscribe<Bitmap>) emitter -> {
                if (iSwitch.isSelected()) {
                    emitter.onNext(QRCodeEncoder.syncEncodeBarcode(txt, 200, 200, 24));
                } else {
                    emitter.onNext(QRCodeEncoder.syncEncodeQRCode(txt, 200));
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(bitmap -> {
                //因为多线程关系，这里如果检测到输入框为空，这置空展示图
                if (UText.isEmpty(edtInput) || bitmap == null) {
                    showEmptyStyle();
                } else {
                    bmp = bitmap;
                    iShow.setImageBitmap(bmp);
                }
            });
        rxDisposable(subscribe);
    }

    private void showEmptyStyle() {
        bmp = null;
        if (iSwitch.isSelected()) {
            edtInput.setHint("Generate barcode");
            //digits属性在代码中的实现
            edtInput.setKeyListener(DigitsKeyListener.getInstance("0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"));
            iShow.setImageResource(R.drawable.ic_bar_light);
        } else {
            edtInput.setHint("Generate QR code");
            edtInput.setInputType(InputType.TYPE_CLASS_TEXT);
            iShow.setImageResource(R.drawable.ic_qr_light);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gen_switch://切换条码&二维码
                iSwitch.setSelected(!iSwitch.isSelected());
                edtInput.setText(null);
                showEmptyStyle();
                break;
            case R.id.gen_save:
                if (bmp != null) {
                    UToast.show("Saving...");
                    UIO.saveBitmap(bmp);
                    UToast.show("Saved tunder the 'QR_Code' folder!");//保存在QR_Code文件夹下
                }
                break;
        }
    }
}
