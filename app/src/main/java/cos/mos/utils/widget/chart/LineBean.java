package cos.mos.utils.widget.chart;

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.04.28 10:15
 * @Email: KosmoSakura@gmail.com
 */
public class LineBean {
    private int key;
    private float value;

    public LineBean() {
    }

    public LineBean(int key, float value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
