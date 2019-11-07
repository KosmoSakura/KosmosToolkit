package cos.mos.utils.ui.dialog;

/**
 * @Description
 * @Author Kosmos
 * @Date 2019.11.07 17:17
 * @Email KosmoSakura@gmail.com
 * @Tip
 */
public class DialogBean {
    public String text;
    public boolean select;
    public int id;

    public DialogBean() {
    }

    public DialogBean(int id, boolean select, String text) {
        this.text = text;
        this.select = select;
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public DialogBean setSelect(boolean select) {
        this.select = select;
        return this;
    }
}
