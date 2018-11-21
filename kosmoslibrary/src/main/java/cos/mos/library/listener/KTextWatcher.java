package cos.mos.library.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @Description: 这里丢辅助工具，H打头
 * @Author: Kosmos
 * @Date: 2018年09月09日 19:54
 * @Email: KosmoSakura@foxmail.com
 */
public abstract class KTextWatcher implements TextWatcher {

    /**
     * @param s     改变之前的内容
     * @param start 开始的位置
     * @param count 被改变的旧内容数
     * @param after 改变后的内容数量
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * @param s      改变之后的内容
     * @param start  开始位置
     * @param before 改变前的内容数量
     * @param count  新增数
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * @param txtEnd 表示最终内容
     */
    @Override
    public abstract void afterTextChanged(Editable txtEnd);
}
