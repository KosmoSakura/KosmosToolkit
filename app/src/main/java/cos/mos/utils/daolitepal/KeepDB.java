package cos.mos.utils.daolitepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.03.19 18:25
 * @Email: KosmoSakura@gmail.com
 */
public class KeepDB extends LitePalSupport {
    //自增id.这里为固定值
    private long id;
    private String name;
    private String appName;
    @Column(unique = true, defaultValue = "unknown")
    private String pkgName;//唯一的，且默认值为unknown
    private int size;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
}
