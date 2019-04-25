package cos.mos.utils.widget.chart;

import java.io.Serializable;

/**
 * Description:饼状图数据实体
 * <p>
 * Author: Kosmos
 * Time: 2017/3/14 001413:40
 * Email:ZeroProject@foxmail.com
 * Events:
 */
public class ColunmnarDto implements Serializable {
    private float humidity;//
    private int color = -1;//
    private int txt_color = -1;
    private String name;//
    private float l, t, r, b;
    private int id;


    public ColunmnarDto(int id, float humidity, int color, int txt_color, String name) {
        this.id = id;
        this.humidity = humidity;
        this.color = color;
        this.txt_color = txt_color;
        this.name = name;
    }

    public ColunmnarDto() {
    }

    public ColunmnarDto(float humidity, int color, String name) {
        this.humidity = humidity;
        this.color = color;
        this.name = name;
    }

    public void setRect(float l, float t, float r, float b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getL() {
        return l;
    }

    public float getTop() {
        return t;
    }

    public float getR() {
        return r;
    }

    public float getB() {
        return b;
    }

    public int getTxt_color() {
        return txt_color;
    }

    public void setTxt_color(int txt_color) {
        this.txt_color = txt_color;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
