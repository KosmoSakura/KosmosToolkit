package cos.mos.toolkit.ui;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;

/**
 * @Description: 隐藏DatePicker滚轮的年、月、日
 * @Author: Kosmos
 * @Date: 2019.07.01 21:11
 * @Email: KosmoSakura@gmail.com
 */
public class UDatePicker {
    private void hideYear(DatePicker picker) {
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
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}
