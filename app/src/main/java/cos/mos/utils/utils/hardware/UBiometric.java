package cos.mos.utils.utils.hardware;

import android.os.Build;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import cos.mos.utils.init.k.KApp;


/**
 * @Description: 生物识别工具
 * @Author: Kosmos
 * @Date: 2018.12.18 15:56
 * @Email: KosmoSakura@gmail.com
 * 权限（不用动态申请）： <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
 */
public class UBiometric {
    private static UBiometric instance;
    private final FingerprintManagerCompat compat;
    private static CancellationSignal cancelSignal;//取消

    private UBiometric() {
        compat = FingerprintManagerCompat.from(KApp.instance());
    }

    public static UBiometric instance() {
        if (instance == null) {
            synchronized (UBiometric.class) {
                if (instance == null) {
                    instance = new UBiometric();
                }
            }
        }
        return instance;
    }

    /**
     * @return 是否有指纹硬件
     * 1.检查手机硬件（有没有指纹感应区）
     * 2.系统版本是否大于Android6.0
     */
    public boolean hasFingerHard() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (compat == null) {
            return false;
        }
        return compat.isHardwareDetected();
    }

    /**
     * @return 是否有录入指纹
     */
    public boolean hasFingerInput() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (compat == null) {
            return false;
        }
        return compat.hasEnrolledFingerprints();
    }

    /**
     * 创建指纹验证
     */
    public void authenticate(FingerListener listener) {
        //必须重新实例化。cancel过，资源会被释放，
        cancelSignal = new CancellationSignal();
        compat.authenticate(null,//用于通过指纹验证取出AndroidKeyStore中key的值
            0,//系统建议为0,其他值，谷狗占位用于其他生物验证
            cancelSignal,//强制取消指纹验证
            new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                    if (listener != null) {
                        listener.onResult(false, "指纹不匹配");
                    }

                }

                @Override
                public void onAuthenticationFailed() {
                    //多次指纹验证错误后，回调此方法；
                    //并且，（第一次错误）由系统锁定30s
                    if (listener != null) {
                        listener.onResult(false, "多次错误，暂时锁定");
                    }
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    //指纹验证成功
                    //result这里的result是指纹数据，需要相应的key才能拿出来用
                    if (listener != null) {
                        listener.onResult(true, "验证成功");
                    }
                }
            }, null);// 内部验证消息通过Handler传递，不需要，传空
    }

    /**
     * 验证取消，感觉没什么卵用
     */
    public static void cancel() {
        if (cancelSignal != null) {
            cancelSignal.cancel();
        }
    }

    public interface FingerListener {
        void onResult(boolean success, String msg);
    }
}
