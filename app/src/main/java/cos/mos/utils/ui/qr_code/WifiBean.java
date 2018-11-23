package cos.mos.utils.ui.qr_code;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.22 21:06
 * @Email: KosmoSakura@gmail.com
 */
public class WifiBean {
    private String ssid;
    private String password;

    public WifiBean(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
