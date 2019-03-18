package cos.mos.utils.utils.hardware;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

import cos.mos.utils.init.k.KApp;


/**
 * @Description: 过气指纹工具类
 * @Author: Kosmos
 * @Date: 2018.12.17 18:09
 * @Email: KosmoSakura@gmail.com
 * 权限（不用动态申请）： <uses-permission android:name="android.permission.USE_FINGERPRINT"/>
 */
@Deprecated
public class UFinger {
    private FingerprintManager fingerMgr;
    private static UFinger instance;
    private FingerListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private UFinger() {
        fingerMgr = (FingerprintManager) KApp.instance().getSystemService(Context.FINGERPRINT_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static UFinger instance() {
        if (instance == null) {
            synchronized (UFinger.class) {
                if (instance == null) {
                    instance = new UFinger();
                }
            }
        }
        return instance;
    }

    public void setOnFingerListener(FingerListener listener) {
        this.listener = listener;
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
        if (fingerMgr == null) {
            return false;
        }
        return fingerMgr.isHardwareDetected();
    }

    /**
     * @return 是否有录入指纹
     */
    public boolean hasFingerInput() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (fingerMgr == null) {
            return false;
        }
        return fingerMgr.hasEnrolledFingerprints();
    }

    /**
     * 创建指纹验证
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate() {
        fingerMgr.authenticate(null,// 用于通过指纹验证取出AndroidKeyStore中key的值
            new CancellationSignal(), 0,//系统建议为0
            new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                //指纹验证失败
                if (listener != null) {
                    listener.onResult(false, "指纹不匹配");
                }
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                //指纹验证成功
                //result这里的result是指纹数据，需要相应的key才能拿出来用
                if (listener != null) {
                    listener.onResult(false, "验证成功");
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
        },null);
    }

    /**
     * api小于Android6，系统不支持
     */
    public static boolean isSysM() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    private static boolean isSysP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public interface FingerListener {
        void onResult(boolean success, String msg);
    }
}
