package cos.mos.utils.dao;

import java.util.List;

import cos.mos.library.utils.UText;
import cos.mos.utils.dao.gen.UserBeanDao;
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
    public static void insert(String url, String thumb, long id) {
        if (UText.isEmpty(url) || id < 0) {
            return;
        }
        UserBean bean = new UserBean();
        bean.setImageId(id);
        bean.setUrl(url);
        bean.setThumb(thumb);
        DbManager.getDaoSession().getUserBeanDao().insertOrReplace(bean);
    }

    /**
     * 删
     */
    public static void deleteById(long imageId) {
        if (imageId < 0) {
            return;
        }
        DbManager.getDaoSession().getUserBeanDao().deleteByKey(imageId);
    }

    public static void deleteByBean(UserBean bean) {
        DbManager.getDaoSession().getUserBeanDao().delete(bean);
    }

    public static void deleteAll() {
        DbManager.getDaoSession().getUserBeanDao().deleteAll();
    }

    /**
     * 改
     */
    public static void update(UserBean bean) {
        DbManager.getDaoSession().getUserBeanDao().update(bean);
    }

    /**
     * 查
     */
    public static UserBean SearchById(long userid) {
        return DbManager.getDaoSession().getUserBeanDao().load(userid);
    }

    public static List<UserBean> SearchAll() {
        return DbManager.getDaoSession().getUserBeanDao().loadAll();
    }

    /**
     * @return 按照某一个字段排序
     */
    public static List<UserBean> SearchAllOrder() {
        return DbManager.getDaoSession().getUserBeanDao()
            .queryBuilder().orderAsc(UserBeanDao.Properties.ImageId).list();
    }
}
