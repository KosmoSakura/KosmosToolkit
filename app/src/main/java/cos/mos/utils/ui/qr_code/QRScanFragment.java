package cos.mos.utils.ui.qr_code;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cos.mos.utils.R;
import cos.mos.toolkit.init.KFragment;
import cos.mos.utils.from_blankj.ULogBj;
import cos.mos.toolkit.json.UGson;
import cos.mos.toolkit.java.UText;
import cos.mos.utils.ui_tools.dialog.UDialog;

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

    private int scanFlag;
    private WifiBean bean;

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
        String title;
        if (UText.isEmpty(result)) {
            title = "It's nothing any more";
            scanFlag = 0;
        } else if (Patterns.PHONE.matcher(result).matches()) {
            scanFlag = 1;
            title = "It like a phone number";
        } else if (Patterns.EMAIL_ADDRESS.matcher(result).matches()) {
            scanFlag = 2;
            title = "It like an email address";
        } else if (Patterns.WEB_URL.matcher(result).matches() || URLUtil.isValidUrl(result)) {
            title = "It looks like a link";
            scanFlag = 3;
        } else {
            try {
                bean = UGson.toBean(result, WifiBean.class);
                if (bean == null) {
                    title = "It's just plain text";
                    scanFlag = 4;
                } else {
                    title = "It's a wifi link";
                    scanFlag = 5;
                }
            } catch (Exception e) {
                title = "It's just plain text";
                scanFlag = 4;
            }
        }
        UDialog.builder(getActivity(), false)
            .title(title)
            .msg(result)
            .cancelClick(dia -> {
                dia.dismiss();
                zxing.startSpot();
            })
            .build((result1, dia) -> {
                switch (scanFlag) {
                    case 1://phone
                        Intent phone = new Intent(Intent.ACTION_DIAL);
                        phone.setData(Uri.parse("tel:" + result1));
                        phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(phone);
                        getActivity().finish();
                        break;
                    case 2://email
                        Intent data = new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse("mailto:way.ping.li@gmail.com"));
                        data.putExtra(Intent.EXTRA_SUBJECT, "This is a title");
                        data.putExtra(Intent.EXTRA_TEXT, "This is the content");
                        startActivity(data);
                        getActivity().finish();
                        break;
                    case 3://url
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri content_url = Uri.parse(result1);
                        intent.setData(content_url);
                        startActivity(Intent.createChooser(intent, "Please select browser"));
                        getActivity().finish();
                        break;
                    case 4://text
                        //do something
                        zxing.startSpot();
                        break;
                    case 5://wifi
                        Intent wifis = new Intent();
                        wifis.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                        wifis.putExtra("extra_prefs_show_button_bar", true);
                        wifis.putExtra("wifi_enable_next_on_connect", true);
                        startActivity(wifis);
                        getActivity().finish();
                        break;
                }
                dia.dismiss();
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
        ULogBj.commonD("打开相机出错");
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
