package cos.mos.utils.mvp.bean;

import java.util.List;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 21:34
 * @Email: KosmoSakura@gmail.com
 */
public class ResultBean<T> {
    private int code;
    private String msg;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
