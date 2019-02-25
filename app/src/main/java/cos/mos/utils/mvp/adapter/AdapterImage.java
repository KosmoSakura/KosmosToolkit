package cos.mos.utils.mvp.adapter;


import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import cos.mos.utils.R;
import cos.mos.utils.mvp.bean.ImageBean;

/**
 * @Description: 瀑布流适配器
 * @Author: Kosmos
 * @Date: 2018.11.14 10:27
 * @Email: KosmoSakura@gmail.com
 */
public class AdapterImage extends BaseQuickAdapter<ImageBean, BaseViewHolder> {
    private int width;

    public AdapterImage(@Nullable List<ImageBean> data, int width) {
        super(R.layout.item_image, data);
        this.width = width;
    }

    @Override
    protected void convert(BaseViewHolder holder, ImageBean item) {
        int w = item.getW();
        int h = item.getH();
        holder.setText(R.id.image_name, w + "X" + h);

        ImageView image = holder.getView(R.id.image_image);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = (int) (width * getDiff(w, h));
        //Glide太尼玛烦了，先注为敬
//        UGlideSimple.loadImage(holder.itemView.getContext(), item.getImg_url(), image);
    }

    /**
     * @apiNote 为了保证高>宽，这里要做点手段
     */
    private float getDiff(float w, float h) {
        if (w >= h) {
            return w / h;
        } else {
            return h / w;
        }
    }
}