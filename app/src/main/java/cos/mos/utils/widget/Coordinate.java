package cos.mos.utils.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description: 四向坐标, Parcelable示例
 * @Author: Kosmos
 * @Date: 2019.01.25 17:12
 * @Email: KosmoSakura@gmail.com
 */
public class Coordinate implements Parcelable {
    private int left, top, right, bottom;

    public Coordinate() {
    }

    public Coordinate(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public int describeContents() {
        return 0;//这里默认返回0即可
    }

    /**
     * 这里的读的顺序必须
     * 与writeToParcel(Parcel dest, int flags)方法中写的顺序一致.
     * 否则数据会错乱
     */
    protected Coordinate(Parcel in) {
        left = in.readInt();
        top = in.readInt();
        right = in.readInt();
        bottom = in.readInt();
    }

    /**
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 序列化过程：必须按成员变量声明的顺序进行封装
        dest.writeInt(left);
        dest.writeInt(top);
        dest.writeInt(right);
        dest.writeInt(bottom);
    }

    /**
     * 反序列过程：必须实现Parcelable.Creator接口，并且对象名必须为CREATOR
     * 读取Parcel里面数据时必须按照成员变量声明的顺序，
     * Parcel数据来源上面writeToParcel方法，读出来的数据供逻辑层使用
     */
    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        /**
         * 从序列化后的对象中创建原始对象
         * 供外部类反序列化本类数组使用
         */
        @Override
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        /**
         * 从Parcel中读取数据
         * 创建指定长度的原始对象数组
         */
        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };
}
