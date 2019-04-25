package cos.mos.utils.widget.single;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cos.mos.toolkit.java.UText;
import cos.mos.utils.R;


/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2018.11.06 9:58
 * @Email: KosmoSakura@foxmail.com
 */
public class SettingBar extends FrameLayout {
    private boolean state;
    private View cbk;
    private TextView tValue;

    public boolean stateGet() {
        return state;
    }

    public void stateSet(boolean state) {
        if (cbk == null) {
            return;
        }
        this.state = state;
        cbk.setSelected(state);
    }

    public void stateChange() {
        if (cbk == null) {
            return;
        }
        state = !state;
        cbk.setSelected(state);
    }

    public void setValue(String value) {
        if (UText.isEmpty(value) || tValue == null) {
            return;
        }
        tValue.setText(value);
    }

    public SettingBar(Context context) {
        this(context, null);
    }

    public SettingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingBar, defStyleAttr, 0);
        String name = ta.getString(R.styleable.SettingBar_name);
        String value = ta.getString(R.styleable.SettingBar_value);
        String describe = ta.getString(R.styleable.SettingBar_describe);
        state = ta.getBoolean(R.styleable.SettingBar_state, false);
        boolean onlyShow = ta.getBoolean(R.styleable.SettingBar_onlyShow, false);
        boolean canPick = ta.getBoolean(R.styleable.SettingBar_canPick, false);
        ta.recycle();
        if (onlyShow) {
            View view = inflate(context, R.layout.item_setting_title, this);
            TextView title = view.findViewById(R.id.setting_title);
            title.setText(name);
        } else {
            View view = inflate(context, R.layout.item_setting, this);
            TextView tName = view.findViewById(R.id.setting_name);
            tValue = view.findViewById(R.id.setting_value);
            TextView tDescribe = view.findViewById(R.id.setting_describe);
            cbk = view.findViewById(R.id.setting_check);
            tName.setText(name);
            if (UText.isEmpty(describe)) {
                tDescribe.setVisibility(View.GONE);
            } else {
                tDescribe.setVisibility(View.VISIBLE);
                tDescribe.setText(describe);
            }
            if (canPick) {
                cbk.setVisibility(View.VISIBLE);
                tValue.setVisibility(View.GONE);
                cbk.setSelected(state);
            } else {
                cbk.setVisibility(View.GONE);
                tValue.setVisibility(View.VISIBLE);
                tValue.setText(value);
            }
        }
    }
}
