package cos.mos.utils.ui.sideslip;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cos.mos.utils.R;
import cos.mos.utils.dao.UserBean;
import cos.mos.utils.widget.EasySwipeMenuLayout;
import io.reactivex.annotations.Nullable;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.22 11:15
 * @Email: KosmoSakura@gmail.com
 */
public class SideLipAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {
    public SideLipAdapter(@Nullable List<UserBean> data) {
        super(R.layout.item_history_qr, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, UserBean item) {
        EasySwipeMenuLayout esl = holder.getView(R.id.item_qr_esl);
        holder.getView(R.id.item_qr_content).setOnClickListener(v -> esl.resetStatus());
        holder.addOnClickListener(R.id.item_qr_right);

        holder.setText(R.id.item_qr_text, item.getThumb());
    }
}
