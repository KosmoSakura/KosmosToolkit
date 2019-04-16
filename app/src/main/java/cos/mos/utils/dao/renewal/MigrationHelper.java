//package cos.mos.utils.dao.renewal;
//
//import android.database.Cursor;
//import android.text.TextUtils;
//import android.util.Log;
//
//import org.greenrobot.greendao.AbstractDao;
//import org.greenrobot.greendao.database.Database;
//import org.greenrobot.greendao.internal.DaoConfig;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import cos.mos.utils.dao.gen.DaoMaster;
//
//
///**
// * GreenDao依赖包在注释里
// */
//public class MigrationHelper {
//
//    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
//    private static MigrationHelper instance;
//
//    public static MigrationHelper getInstance() {
//        if (instance == null) {
//            instance = new MigrationHelper();
//        }
//        return instance;
//    }
//
//    @SafeVarargs
//    final void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        generateTempTables(db, daoClasses);
//        DaoMaster.dropAllTables(db, true);
//        DaoMaster.createAllTables(db, false);
//        restoreData(db, daoClasses);
//    }
//
//    /**
//     * 生成临时列表
//     */
//    @SafeVarargs
//    private final void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
//            DaoConfig daoConfig = new DaoConfig(db, daoClass);
//
//            String divider = "";
//            String tableName = daoConfig.tablename;
//            String tempTableName = daoConfig.tablename.concat("_TEMP");
//            ArrayList<String> properties = new ArrayList<>();
//
//            StringBuilder createTableStringBuilder = new StringBuilder();
//
//            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");
//
//            for (int j = 0; j < daoConfig.properties.length; j++) {
//                String columnName = daoConfig.properties[j].columnName;
//
//                if (getColumns(db, tableName).contains(columnName)) {
//                    properties.add(columnName);
//
//                    String type = null;
//
//                    try {
//                        type = getTypeByClass(daoConfig.properties[j].type);
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//
//                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
//
//                    if (daoConfig.properties[j].primaryKey) {
//                        createTableStringBuilder.append(" PRIMARY KEY");
//                    }
//
//                    divider = ",";
//                }
//            }
//            createTableStringBuilder.append(");");
//
//            db.execSQL(createTableStringBuilder.toString());
//
//            String insertTableStringBuilder = "INSERT INTO " + tempTableName + " (" +
//                TextUtils.join(",", properties) +
//                ") SELECT " +
//                TextUtils.join(",", properties) +
//                " FROM " + tableName + ";";
//            db.execSQL(insertTableStringBuilder);
//
//        }
//    }
//
//    /**
//     * 存储新的数据库表 以及数据
//     */
//    @SafeVarargs
//    private final void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
//        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
//            DaoConfig daoConfig = new DaoConfig(db, daoClass);
//            String tableName = daoConfig.tablename;
//            String tempTableName = daoConfig.tablename.concat("_TEMP");
//            ArrayList<String> properties = new ArrayList<>();
//
//            for (int j = 0; j < daoConfig.properties.length; j++) {
//                String columnName = daoConfig.properties[j].columnName;
//
//                if (getColumns(db, tempTableName).contains(columnName)) {
//                    properties.add(columnName);
//                }
//            }
//
//            String insertTableStringBuilder = "INSERT INTO " + tableName + " (" +
//                TextUtils.join(",", properties) +
//                ") SELECT " +
//                TextUtils.join(",", properties) +
//                " FROM " + tempTableName + ";";
//            db.execSQL(insertTableStringBuilder);
//            db.execSQL("DROP TABLE " + tempTableName);
//        }
//    }
//
//    private String getTypeByClass(Class<?> type) throws Exception {
//        if (type.equals(String.class)) {
//            return "TEXT";
//        }
//        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
//            return "INTEGER";
//        }
//        if (type.equals(Boolean.class)) {
//            return "BOOLEAN";
//        }
//
//        Exception exception = new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
//        exception.printStackTrace();
//        throw exception;
//    }
//
//    private List<String> getColumns(Database db, String tableName) {
//        List<String> columns = new ArrayList<>();
//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
//            if (cursor != null) {
//                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
//            }
//        } catch (Exception e) {
//            Log.v(tableName, e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return columns;
//    }
//}