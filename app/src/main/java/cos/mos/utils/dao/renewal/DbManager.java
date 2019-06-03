//package cos.mos.utils.dao.renewal;
//
//import android.database.sqlite.SQLiteDatabase;
//
//import cos.mos.toolkit.init.KtApp;
//import cos.mos.utils.dao.gen.DaoMaster;
//import cos.mos.utils.dao.gen.DaoSession;
//import cos.mos.utils.init.Constant;
//
///**
// * @Description: GreenDao依赖包在注释里
// * @Author: Kosmos
// * @Email: KosmoSakura@foxmail.com
// * * @eg: 最新修改日期：2018年12月10日 22:00
// */
//public class DbManager {
//    private static DbManager mDbManager;
//    private static DaoMaster.DevOpenHelper mDevOpenHelper;
//    private static DaoMaster mDaoMaster;
//    private static DaoSession mDaoSession;
//
//
//    private DbManager() {
//        mDevOpenHelper = new DaoMaster.DevOpenHelper(KtApp.instance(), Constant.DB_NAME);
//        getDaoMaster();
//        getDaoSession();
//    }
//
//    public static DbManager getInstance() {
//        if (null == mDbManager) {
//            synchronized (DbManager.class) {
//                if (null == mDbManager) {
//                    mDbManager = new DbManager();
//                }
//            }
//        }
//        return mDbManager;
//    }
//
//    /**
//     * 获取可读数据库
//     */
//    public static SQLiteDatabase getReadableDatabase() {
//        if (null == mDevOpenHelper) {
//            getInstance();
//        }
//        return mDevOpenHelper.getReadableDatabase();
//    }
//
//    /**
//     * 获取可写数据库
//     */
//    public static SQLiteDatabase getWritableDatabase() {
//        if (null == mDevOpenHelper) {
//            getInstance();
//        }
//        return mDevOpenHelper.getWritableDatabase();
//    }
//
//    /**
//     * 获取DaoMaster
//     * 判断是否存在数据库，如果没有则创建数据库
//     */
//    public static DaoMaster getDaoMaster() {
//        if (null == mDaoMaster) {
//            synchronized (DbManager.class) {
//                if (null == mDaoMaster) {
//                    RenewalHelper helper = new RenewalHelper(KtApp.instance(), Constant.DB_NAME, null);
//                    mDaoMaster = new DaoMaster(helper.getWritableDatabase());
//                }
//            }
//        }
//        return mDaoMaster;
//    }
//
//
//    /**
//     * 获取DaoSession
//     */
//    public static DaoSession getDaoSession() {
//        if (null == mDaoSession) {
//            synchronized (DbManager.class) {
//                mDaoSession = getDaoMaster().newSession();
//            }
//        }
//        return mDaoSession;
//    }
//}