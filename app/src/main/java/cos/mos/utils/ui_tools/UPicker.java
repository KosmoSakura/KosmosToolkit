package cos.mos.utils.ui_tools;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

/**
 * @Description: Picker滚轮样式修改
 * @Author: Kosmos
 * @Date: 2019.07.01 21:11
 * @Email: KosmoSakura@gmail.com
 */
public class UPicker {
    public void setTimePicker(TimePicker picker) {
        picker.setIs24HourView(true);
        //1.反射获取TimePicker源码里hour和minute的id
        Resources systemResources = Resources.getSystem();
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        int ampmNumberpickerId = systemResources.getIdentifier("amPm", "id", "android");
        //2.转换成hour和minute对应的NumberPicker
        NumberPicker hourNumberPicker = picker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = picker.findViewById(minuteNumberPickerId);
        NumberPicker ampmNumberPicker = picker.findViewById(ampmNumberpickerId);
        //3.为所欲为
        hourNumberPicker.setMinValue(8);//设置最小hour
        hourNumberPicker.setMaxValue(12);//设置最大hour
        hourNumberPicker.setBackgroundColor(Color.parseColor("#008ced"));//条条背景色
        //4.反射分割线
        setNumberPickerDivider(hourNumberPicker, Color.parseColor("#008ced"));
        setNumberPickerDivider(minuteNumberPicker, Color.parseColor("#008ced"));
        setNumberPickerDivider(ampmNumberPicker, Color.parseColor("#008ced"));
        //5.1.修改显示文字颜色
        //直接xml里设置android:theme="@style/Theme.picker"
        //5.2.修改显示文字颜色
        setNumberPickerTextColor(hourNumberPicker, Color.parseColor("#008ced"));
        setNumberPickerTextColor(minuteNumberPicker, Color.parseColor("#008ced"));
        setNumberPickerTextColor(ampmNumberPicker, Color.parseColor("#008ced"));
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        //这里就是要设置的颜色，修改一下作为参数传入会更好
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            try {
                Field wheelpaint_field = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);
                ((Paint) wheelpaint_field.get(numberPicker)).setColor(color);
                ((EditText) child).setTextColor(color);
                numberPicker.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setNumberPickerDivider(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            //设置颜色
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                //选择自己喜欢的颜色
                ColorDrawable colorDrawable = new ColorDrawable(color);
                try {
                    pf.set(numberPicker, colorDrawable);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //设置高度
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    int result = 3;  //要设置的高度
                    pf.set(numberPicker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            numberPicker.invalidate();
        }
    }

    /**
     * @param picker 隐藏DatePicker滚轮的年、月、日
     */
    public void hideDatePicker(DatePicker picker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //year month day
            int spinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            if (spinnerId != 0) {
                View daySpinner = picker.findViewById(spinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } else {
            try {
                Field[] datePickerfFields = picker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    //mYearSpinner mMonthSpinner mDaySpinner
                    if ("mYearSpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(picker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
