package cos.mos.library.utils.snackbar;

import cos.mos.library.R;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.lib_error, R.color.red),

    /**
     * 橙色,警告
     */
    WARNING(R.drawable.lib_warning, R.color.orange),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.lib_success, R.color.green);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
