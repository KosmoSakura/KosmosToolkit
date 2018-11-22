package cos.mos.utils.ui.qr_code;

import android.app.Dialog;
import android.hardware.Camera;
import android.os.Vibrator;
import android.view.View;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cos.mos.library.Utils.UDialog;
import cos.mos.library.Utils.ULog;
import cos.mos.library.Utils.UText;
import cos.mos.library.init.KFragment;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * @Description: 二维码扫描
 * @Author: Kosmos
 * @Date: 2018.11.20 15:43
 * @Email: KosmoSakura@gmail.com
 */
public class QRScanFragment extends KFragment implements QRCodeView.Delegate, View.OnClickListener {
    private ZXingView zxing;
    private View light;//闪光灯按钮
    private int camera;//前后摄像头值
    private boolean isVisible;//Fragment是否可见

    @Override
    protected int layout() {
        return R.layout.frag_qr_scan;
    }

    @Override
    protected void init() {
        zxing = findViewById(R.id.qr_zxing);
        light = findViewById(R.id.qr_light);
        light.setOnClickListener(this);
        findViewById(R.id.qr_focus).setOnClickListener(this);
        findViewById(R.id.qr_cut).setOnClickListener(this);
        light.setSelected(false);
    }

    @Override
    protected void logic() {
        //设置二维码的代理
        zxing.setDelegate(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //默认后置摄像头
        camera = Camera.CameraInfo.CAMERA_FACING_BACK;
        //打开后置摄像头预览,但并没有开始扫描
        zxing.startCamera(camera);
        //开启扫描框
        zxing.showScanRect();
        //延迟1.5秒后开始识别
        zxing.startSpot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;//Fragment可见回调
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStop() {
        zxing.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        zxing.onDestroy();
        super.onDestroy();
    }

    /**
     * @param result 扫描成功解析二维码成功后的结果
     * @apiNote 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。
     * 解析本地图片或 Bitmap 时 result 可能为 null
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        //扫描成功后调用震动器
        vibrator();
        //显示扫描结果
        if (UText.isEmpty(result)) {
            return;
        }
        UDialog.getInstance(getActivity(), false, false)
            .showNoticeWithOnebtn(result, new UDialog.SureClick() {
                @Override
                public void OnSureClick(String result, Dialog dia) {
                    //保存数据库
                    zxing.startSpot();
                    dia.dismiss();
                }
            });
    }


    /**
     * @param isDark 是否变暗
     * @apiNote 摄像头环境亮度发生变化
     */
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        //变暗且可见状态
        if (isDark && isVisible) {
            light.setSelected(true);
            zxing.openFlashlight();
        }
        //不可见但是灯亮
        if (!isVisible && light.isSelected()) {
            light.setSelected(false);
            zxing.closeFlashlight();
        }
    }

    /**
     * @apiNote 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        ULog.commonD("打开相机出错");
    }

    private void vibrator() {
        //获取系统震动服务
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onClick(View view) {
//        zxing.changeToScanBarcodeStyle();//切换到条形码扫描
//        zxing.changeToScanQRCodeStyle();//切换到二维码扫描
        switch (view.getId()) {
            case R.id.qr_light://开关闪光灯:
                changeLight();
                break;
            case R.id.qr_focus://聚焦
                zxing.stopCamera();
                zxing.startCamera(camera);
                zxing.showScanRect();
                zxing.startSpot();
                break;
            case R.id.qr_cut://切换前置摄像头:
                zxing.stopCamera();
                if (camera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    camera = Camera.CameraInfo.CAMERA_FACING_BACK;
                } else {
                    camera = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                zxing.startCamera(camera);
                zxing.showScanRect();
                zxing.startSpot();
                break;
        }
    }

    private void changeLight() {
        if (light.isSelected()) {
            light.setSelected(false);
            zxing.closeFlashlight();
        } else {
            light.setSelected(true);
            zxing.openFlashlight();
        }
    }
}
