package cos.mos.utils.widget.chart;

/**
 * @Description:
 * @Author: Kosmos lbing
 * @Date: 2019.06.28 15:44
 * @Email: KosmoSakura@gmail.com
 * @link https://github.com/yunzheyue/honrizontalBar
 */
public class ColumnarChartScrollerBean {
    private int count;
    private String bottomText;

    public ColumnarChartScrollerBean() {
    }

    public ColumnarChartScrollerBean(int count, String bottomText) {
        this.count = count;
        this.bottomText = bottomText;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBottomText() {
        return bottomText == null ? "" : bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }
}
