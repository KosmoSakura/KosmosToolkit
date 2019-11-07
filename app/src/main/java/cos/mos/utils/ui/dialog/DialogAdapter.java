package cos.mos.utils.ui.dialog;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cos.mos.utils.R;

public class DialogAdapter extends BaseQuickAdapter<DialogBean, BaseViewHolder> {
    public DialogAdapter(@Nullable List<DialogBean> data) {
        super(R.layout.dia_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DialogBean item) {
        TextView textView = helper.getView(R.id.item_dialog_name);
        textView.setText(item.text);
        textView.setSelected(item.select);
    }
}
