package cos.mos.utils.dao;

import android.content.Context;

import java.util.List;

import cos.mos.library.utils.UText;
import cos.mos.utils.dao.renewal.DbManager;


/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.08 11:36
 * @Email: KosmoSakura@foxmail.com
 */
public class DbHelper {
    /**
     * 增
     */
    public static void insert(Context context, String url, String thumb, long id) {
        if (context == null || UText.isEmpty(url) || id < 0) {
            return;
        }
        UserBean bean = new UserBean();
        bean.setImageId(id);
        bean.setUrl(url);
        bean.setThumb(thumb);
        DbManager.getDaoSession(context).getUserBeanDao().insertOrReplace(bean);
    }

    /**
     * 删
     */
    public static void deleteById(Context context, long imageId) {
        if (context == null || imageId < 0) {
            return;
        }
        DbManager.getDaoSession(context).getUserBeanDao().deleteByKey(imageId);
    }

    public static void deleteByBean(Context context, UserBean bean) {
        if (context == null) {
            return;
        }
        DbManager.getDaoSession(context).getUserBeanDao().delete(bean);
    }

    public static void deleteAll(Context context) {
        if (context == null) {
            return;
        }
        DbManager.getDaoSession(context).getUserBeanDao().deleteAll();
    }

    /**
     * 改
     */
    public static void update(Context context, UserBean bean) {
        if (context == null) {
            return;
        }
        DbManager.getDaoSession(context).getUserBeanDao().update(bean);
    }

    /**
     * 查
     */
    public static UserBean SearchById(Context context, long userid) {
        return DbManager.getDaoSession(context).getUserBeanDao().load(userid);
    }

    public static List<UserBean> SearchAll(Context context) {
        return DbManager.getDaoSession(context).getUserBeanDao().loadAll();
    }
}
