package cos.mos.utils.ui_tools.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cos.mos.toolkit.R;
import cos.mos.utils.ui_tools.toast.ToastUtil;

/**
 * @Description
 * @Author Kosmos
 * @Date 2019.11.07 18:43
 * @Email KosmoSakura@gmail.com
 * @Tip
 */
public class DialogSimpleActivity {

    /**
     * 列表对话框使用示例
     */
    private void simple(Activity activity) {
        List<DialogBean> list = new ArrayList<>();
        list.add(new DialogBean(1, false, "协商一致取消订单"));
        list.add(new DialogBean(1, false, "缺货"));
        list.add(new DialogBean(1, false, "其他"));
        list.add(new DialogBean(1, false, "划算出来"));


        UDialogList dialog = UDialogList.builder(activity);

        dialog.title("取消理由")
            .msg("请选择您取消订单的理由以告知卖家")
            .buttonStyleConfirm("提交", Color.parseColor("#ffffff"),
                R.drawable.arc_full_blue_r4_cor_2be)
            .initList(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, list.get(i).setSelect(i == position));
                    }
                    dialog.notify(list);
                }
            });
        dialog.notify(list);
        dialog.build(new UDialogList.ConfirmClick() {
            @Override
            public void onConfirmClick(String result, Dialog dia) {
                String string = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        string = list.get(i).text;
                    }
                }
                ToastUtil.show("提交:" + string);
            }
        });
    }
}
