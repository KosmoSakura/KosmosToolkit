package cos.mos.utils.constant;

import android.graphics.drawable.Drawable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.12.06 21:29
 * @Email: KosmoSakura@gmail.com
 */
public class AppBean {
    private Drawable image;//app图标
    private String appName;//app名字
    private String pkgName;//包名
    private boolean locked;
    private boolean sysApp;//是系统应用

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public boolean isSysApp() {
        return sysApp;
    }

    public void setSysApp(boolean sysApp) {
        this.sysApp = sysApp;
    }
}
