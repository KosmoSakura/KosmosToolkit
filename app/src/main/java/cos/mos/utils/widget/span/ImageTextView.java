package cos.mos.utils.widget.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

/**
 * Description:
 * <p>
 * Author: Kosmos
 * Time: 2017/8/16 001613:30
 * Email:ZeroProject@foxmail.com
 * Events:
 */
public class ImageTextView extends android.support.v7.widget.AppCompatTextView {
    private String flag = "image";

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageTextView(Context context) {
        super(context);
    }

    public void inputImageText(int id) {
        insertDrawable(id);
    }

    public void addSpace(int size) {
        if (size == 1) {
            append("\t");
        } else {
            append("\t\t");
        }
    }

    private void insertDrawable(int id) {
        final SpannableString spannableString = new SpannableString(flag);
        KosImageSpan imageSpan = new KosImageSpan(getContext(), id);
        spannableString.setSpan(imageSpan, 0, flag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        append(spannableString);
    }

    private void insertDrawableOrangal(int id) {
        final SpannableString spannableString = new SpannableString(flag);
        //ALIGN_BASELINE（顶部对齐）、ALIGN_BOTTOM （底部对齐）
        spannableString.setSpan(new ImageSpan(getContext(), id), 0, flag.length(), ImageSpan.ALIGN_BOTTOM);
        append(spannableString);
    }

    private void insertDrawableBitmap(int id, Context context) {
        final SpannableString spannableString = new SpannableString(flag);
        spannableString.setSpan(new ImageSpan(getContext(), id), 0, flag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
        spannableString.setSpan(new ImageSpan(context, bmp), 0, flag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        append(spannableString);
    }

    private void insertDrawable(int id, int r, int b) {
        final SpannableString spannableString = new SpannableString(flag);
        //得到drawable对象，即所要插入的图片
        Drawable drawable = getResources().getDrawable(id);
        //用这个drawable对象代替字符串ic_launcher
        drawable.setBounds(0, 0, r, b);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"image".length()即：5。[0,5)。值得注意的是当我们复制这个图片的时候，实际是复制了"image"这个字符串。
        spannableString.setSpan(span, 0, flag.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(spannableString);
    }

}
