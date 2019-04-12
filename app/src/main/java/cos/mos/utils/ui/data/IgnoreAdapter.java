package cos.mos.utils.ui.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cos.mos.utils.R;
import cos.mos.utils.constant.AppBean;
import cos.mos.toolkit.java.UText;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.03.19 16:51
 * @Email: KosmoSakura@gmail.com
 */
public class IgnoreAdapter extends BaseAdapter {
    private List<AppBean> list;
    private Context context;

    public IgnoreAdapter(List<AppBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setData(List<AppBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public AppBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_app_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        AppBean app = list.get(position);
        if (app != null) {
            if (app.getImage() != null) {
                holder.icon.setImageDrawable(app.getImage());
            }
            holder.tName.setText(UText.isNull(app.getAppName()));
        }
        return view;
    }

    private class ViewHolder {
        final TextView tName;
        final ImageView icon;

        ViewHolder(View root) {
            icon = root.findViewById(R.id.ignore_icon);
            tName = root.findViewById(R.id.ignore_name);
        }
    }
}
