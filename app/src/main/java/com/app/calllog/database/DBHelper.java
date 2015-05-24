package com.app.calllog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.calllog.model.ASModel;
import com.app.calllog.model.BaseModel;
import com.app.calllog.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.app.calllog.constants.DBConstants;

/**
 * Created by Akash on 24-May-15.
 */
public class DBHelper extends SQLiteOpenHelper {

/* fields and Tables Contacts Are imported from DBContants Class*/

    protected static final String KEY_ID = "id";
    /* table names */
    public static final String TABLE_CONTACT = "contacts";


    /* field Name for Contact Table */
    public static final String KEY_CONATCT_NAME = "name";
    public static final String KEY_CONATCT_NO = "number";
    public static final String KEY_CONATCT_EMAIL_ID = "emailId";





    protected static final String TAG = DBHelper.class.getSimpleName();
    protected static final String DB_NAME = "CallLog.db";
    protected static final int DB_VERSION = 1;

    protected static SQLiteDatabase mDb;

    protected static DBHelper mDBHelper;

    public static DBHelper getInstance(Context ctx) {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(ctx);
        }
        return mDBHelper;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        openDataBase();
    }

    public void openDataBase() throws SQLiteException {
        // Open the database
        if (mDb != null && mDb.isOpen())
            mDb.close();
        mDb = getWritableDatabase();
    }

    public SQLiteDatabase getOpenDataBase() {
        openDataBase();
        return mDb;
    }

    @Override
    public synchronized void close() {
        if (mDb != null)
            mDb.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        truncateAllTable(sqLiteDatabase);
    }



    protected void createTables(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CreateUserTable());
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    private String CreateUserTable() {
        return "CREATE TABLE " + TABLE_CONTACT + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_CONATCT_NAME + " TEXT, " + KEY_CONATCT_NO + " TEXT, " + KEY_CONATCT_EMAIL_ID + " TEXT);";
    }
    /**
     * insert data into table
     *
     * @param model
     * @return
     */
    public static boolean insert(Object model) {
        String query = getInsertQuery(model);
        Log.d(TAG, "Insert :" + query);
        if (query != null && query.length() > 0) {
            try {
                mDb.execSQL(query);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * create insert query from the model
     *
     * @param model
     * @return
     */
    public static String getInsertQuery(Object model) {
        if (model == null)
            return null;
        String query = null;
        String className = model.getClass().getSimpleName();
        Field[] fields = model.getClass().getFields();

        String fieldsStr = "(";
        String fieldValuesStr = "(";

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                boolean isAdded = false;

                if (field.getType() == Integer.TYPE) {
                    int value = field.getInt(model);
                    if (value != 0) {
                        isAdded = true;
                        fieldValuesStr += value;
                    }
                } else if (field.getType().getName().contains("String")) {
                    String value = field.get(model) + "";
                    if (value != null && !value.equalsIgnoreCase("null")
                            && value.length() > 0) {
                        isAdded = true;
                        fieldValuesStr += "'" + value + "'";
                    }
                } else if (field.getType() == Long.TYPE) {
                    long value = field.getLong(model);
                    if (value != 0) {
                        isAdded = true;
                        fieldValuesStr += value;
                    }
                } else if (field.getType() == Double.TYPE) {
                    double value = field.getDouble(model);
                    if (value != 0) {
                        isAdded = true;
                        fieldValuesStr += value;
                    }
                } else if (field.getType() == Boolean.TYPE) {
                    boolean value = field.getBoolean(model);
                    isAdded = true;
                    fieldValuesStr += value;
                } else {
                    String value = field.get(model) + "";
                    if (value != null && !value.equalsIgnoreCase("null")
                            && value.length() > 0) {
                        isAdded = true;
                        fieldValuesStr += "'" + value + "'";
                    }
                }

                if (isAdded) {
                    fieldsStr += field.getName();
                    fieldsStr += ",";
                    fieldValuesStr += ",";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // remove last appended comma
        fieldsStr = fieldsStr.substring(0, fieldsStr.length() - 1);
        fieldValuesStr = fieldValuesStr.substring(0,
                fieldValuesStr.length() - 1);

        fieldsStr += ")";
        fieldValuesStr += ")";

        // make a query
        if (fieldsStr.length() > 2 && fieldValuesStr.length() > 2) {
            StringBuffer sbQuery = new StringBuffer();
            sbQuery.append("insert into ");
            sbQuery.append("[" + className + "] ");
            sbQuery.append(fieldsStr.toString() + " ");
            sbQuery.append("values ");
            sbQuery.append(fieldValuesStr.toString());
            query = sbQuery.toString();
        }
        Log.i(TAG, "insert : " + query);
        return query;
    }


    /**
     * update model into database
     *
     * @param model
     * @return
     */
    public static boolean update(Object model) {
        String query = getUpdateQuery(model);
        Log.d(TAG, "update :" + query);
        if (query != null && query.length() > 0) {
            try {
                mDb.execSQL(query);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * create model query for the table
     *
     * @param model
     * @return
     */
    public static String getUpdateQuery(Object model) {
        if (model == null)
            return null;
        String query = null;
        String className = model.getClass().getSimpleName();
        // String id = className.toLowerCase() + "_id";
        String id = "id";
        Field[] fields = model.getClass().getFields();

        String values = "";
        String condition = "";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                boolean isAdded = false;
                String value = "";
                if (field.getType() == Integer.TYPE) {

                    int fieldValue = field.getInt(model);
                    if (fieldValue != 0) {
                        isAdded = true;
                        value = String.valueOf(fieldValue);
                        if (field.getName().equalsIgnoreCase(id)) {
                            condition = id + "=" + fieldValue;
                            // don't add primary key to update
                            isAdded = false;
                        }
                    }

                } else if (field.getType().getName().contains("String")) {
                    String fieldValue = field.get(model) + "";
                    isAdded = true;
                    if (fieldValue != null
                            && !fieldValue.equalsIgnoreCase("null")
                            && fieldValue.length() > 0) {
                        value = "'" + fieldValue + "'";
                    } else
                        value = null;
                    Log.d(TAG, "name:" + field.getName() + " = " + value + "");
                } else if (field.getType() == Long.TYPE) {
                    long fieldValue = field.getLong(model);
                    if (fieldValue != 0) {
                        isAdded = true;
                        value = String.valueOf(fieldValue);
                    }
                } else if (field.getType() == Double.TYPE) {
                    double fieldValue = field.getDouble(model);
                    if (fieldValue != 0) {
                        isAdded = true;
                        value = String.valueOf(fieldValue);
                    }
                } else if (field.getType() == Boolean.TYPE) {
                    boolean fieldValue = field.getBoolean(model);
                    isAdded = true;
                    value = String.valueOf(fieldValue);
                } else {
                    String fieldValue = field.get(model).toString();
                    if (fieldValue != null
                            && !fieldValue.equalsIgnoreCase("null")
                            && fieldValue.length() > 0) {
                        isAdded = true;
                        value += "'" + fieldValue + "'";
                    }
                }
                if (isAdded) {
                    values += field.getName() + " = " + value;
                    values += ",";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // remove last appended comma
        values = values.substring(0, values.length() - 1);

        // make a query
        if (values.length() > 2 && condition.length() > 0) {
            StringBuffer sbQuery = new StringBuffer();
            sbQuery.append("update ");
            sbQuery.append("[" + className + "] ");
            sbQuery.append("set ");
            sbQuery.append(values + " ");
            sbQuery.append("where ");
            sbQuery.append(condition);
            query = sbQuery.toString();
        }
        Log.i(TAG, "update : " + query);
        return query;
    }

    public static boolean isAvailable(Context ctx, ASModel model) {
        try {
            String query = String.format("SELECT * FROM [%s] WHERE %s = '%s'",
                    model.getModelName(), model.getASColumnName(),
                    model.getASColumnValue());
            Log.i("query:%s", query);
            DBHelper helper = DBHelper.getInstance(ctx);
            List<ASModel> models = fetchAllRows(query, model);
            return models.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAvailable(Context ctx, ASModel model,
                                      String condition) {
        try {
            List<ASModel> models = getAllRow(ctx, model.getModelName(), model,
                    condition);
            return models.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertOrUpdate(Context ctx, ASModel model) {
        boolean isAvailable = isAvailable(ctx, model);
        Log.d("isAvailable:%s", isAvailable + "");
        if (!isAvailable) {
            return insert(ctx, model);
        } else {
            return update(ctx, model);
        }
    }

    /**
     * delete model into database
     *
     * @param model
     * @return
     */
    public boolean delete(Object model) {
        String query = getDeleteQuery(model);
        Log.d(TAG, "Delete :" + query);
        if (query != null && query.length() > 0) {
            try {
                mDb.execSQL(query);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getDeleteQuery(Object model) {
        if (model != null) {
            String className = model.getClass().getSimpleName();
            // String id = className.toLowerCase() + "_id";
            String id = "id";
            Field[] fields = model.getClass().getFields();
            String condition = "";
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                try {
                    if (field.getType() == Integer.TYPE) {
                        int fieldValue = field.getInt(model);
                        if (fieldValue != 0) {
                            if (field.getName().equalsIgnoreCase(id)) {
                                condition = id + "="
                                        + String.valueOf(fieldValue);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String query = "Delete from [" + className + "] where " + condition;
            Log.i(TAG, "delete: " + query);
            return query;
        }
        return null;
    }


    public interface Failure {
        public void onFailed(int errNo, String errMessage);
    }

    public interface OnExecuteNonQueryListener /* extends Failure */ {
        public void onSuccess(boolean success);
    }

    public interface OnExecuteQueryListener<T> extends Failure {
        public <T extends BaseModel> void onSuccess(List<T> list);
    }

    public static void insert(final Context ctx, final BaseModel model,
                              final OnExecuteNonQueryListener listener) {
        new Thread() {
            public void run() {
                boolean success = insert(ctx, model);
                if (listener != null) {
                    listener.onSuccess(success);
                }
            }
        }.start();
    }

    public static boolean insert(Context ctx, BaseModel model) {
        return insert(model);
    }

    public boolean insert(Context ctx, String tableName,
                          Map<String, Object> columnMaps) {
        return insertByMap(tableName, columnMaps);
    }

    public long insert(String tblName, Object obj) {
        ContentValues initialValues;
        long result = 0;

        initialValues = new ContentValues();
        Field[] fields = obj.getClass().getFields();

        try {
            for (Field field : fields) {
                if (field.getType().getName().contains("int")) {
                    initialValues
                            .put(field.getName(), (Integer) field.get(obj));
                } else if (field.getType().getName().contains("String")) {
                    initialValues.put(field.getName(), (String) field.get(obj));
                } else if (field.getType().getName().contains("double")) {
                    initialValues.put(field.getName(), (Float) field.get(obj));
                } else if (field.getType().getName().contains("long")) {
                    initialValues.put(field.getName(), (Long) field.get(obj));
                } else {
                    initialValues.put(field.getName(), field.get(obj)
                            .toString());
                }
            }
            if (mDb.insertOrThrow("[" + tblName + "]", null, initialValues) != -1)
                result++;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    protected ContentValues getContentValue(Map<String, Object> columnMaps) {
        ContentValues initialValues = new ContentValues();

        try {
            for (Map.Entry<String, Object> entry : columnMaps.entrySet()) {
                if (entry.getValue().getClass().getName().contains("int")) {
                    initialValues.put(entry.getKey(),
                            (Integer) entry.getValue());
                } else if (entry.getValue().getClass().getName()
                        .contains("String")) {
                    initialValues
                            .put(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue().getClass().getName()
                        .contains("double")) {
                    initialValues.put(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue().getClass().getName()
                        .contains("long")) {
                    initialValues.put(entry.getKey(), (Long) entry.getValue());
                } else {
                    initialValues.put(entry.getKey(), entry.getValue()
                            .toString());
                }
            }
            return initialValues;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertByMap(String tableName, Map<String, Object> columnMaps) {
        ContentValues initialValues = getContentValue(columnMaps);
        try {
            if (initialValues != null) {
                if (mDb.insertOrThrow("[" + tableName + "]", null,
                        initialValues) != -1)
                    return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insert(final Context ctx, final String tableName,
                       final Map<String, Object> columnMaps,
                       final OnExecuteNonQueryListener listener) {
        new Thread() {
            public void run() {
                boolean success = insert(ctx, tableName, columnMaps);
                if (listener != null) {
                    listener.onSuccess(success);
                }
            }
        }.start();
    }

    public void update(final Context ctx, final BaseModel model,
                       final OnExecuteNonQueryListener listener) {
        new Thread() {
            public void run() {
                boolean success = update(ctx, model);
                if (listener != null) {
                    listener.onSuccess(success);
                }
            }
        }.start();
    }

    public static boolean update(Context ctx, BaseModel model) {
        return update(model);
    }

    public void delete(final Context ctx, final BaseModel model,
                       final OnExecuteNonQueryListener listener) {
        new Thread() {
            public void run() {
                boolean success = delete(ctx, model);
                if (listener != null) {
                    listener.onSuccess(success);
                }
            }
        }.start();
    }

    public boolean delete(Context ctx, BaseModel model) {
        return delete(model);
    }

    public static <T extends ASModel> void getAllRow(final Context ctx,
                                                     final T model, final OnExecuteQueryListener listener) {
        new Thread() {
            public void run() {
                try {
                    List<T> list = getAllRow(ctx, model);
                    if (listener != null) {
                        listener.onSuccess(list);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFailed(1,
                                "Could not getAllRow - " + e.getMessage());
                    }
                }
            }
        }.start();
    }

    public static <T extends ASModel> List<T> getAllRow(Context ctx, T model)
            throws Exception {
        return getAllRow(ctx, model.getModelName(), model, "");
    }

    public static <T extends ASModel> List<T> getAllRow(Context ctx,
                                                        String tableName, T model, String condition) throws Exception {
        DBHelper helper = DBHelper.getInstance(ctx);
        return fetchAllRows("select * from [" + tableName + "] "
                + condition, model);
    }

    public static <T extends ASModel> void getAllRow(final Context ctx,
                                                     final String tableName, final T model, final String condition,
                                                     final OnExecuteQueryListener listener) {
        new Thread() {
            public void run() {
                try {
                    List<T> list = getAllRow(ctx, tableName, model, condition);
                    if (listener != null) {
                        listener.onSuccess(list);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onFailed(1,
                                "Could not getAllRow - " + e.getMessage());
                    }
                }
            }
        }.start();
    }

    /**
     * @param <T>
     * @param <T>
     * @param <T>
     * @param sqlQuery SQL query to be fired
     * @param myObj    Object to be fetched
     * @return Returns a Vector object containing raws fetched by the sqlQuery
     */

    public static <T> List<T> fetchAllRows(String sqlQuery, T myObj) throws Exception {
        ArrayList<T> records = new ArrayList<T>();
        Cursor cursor = execQuery(sqlQuery);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                records.add(getRow(cursor, myObj));
            }
            cursor.close();
        }
        return records;
    }

    public <T> T getRow(String sqlQuery, T myObj) throws Exception {
        Cursor cursor = execQuery(sqlQuery);
        if (cursor != null) {
            cursor.moveToFirst();
            return getRow(cursor, myObj);
        }
        return null;
    }

    protected static <T> T getRow(Cursor cursor, T obj) {
        T newObj = newObject(obj);
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String key = cursor.getColumnName(i);
            objectMapping(newObj, key, cursor, i);
        }
        return newObj;
    }

    public synchronized Cursor execQuery(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery(sql, selectionArgs);
            // Log.d("SQL", sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // closeDb();
        }
        return cursor;
    }

    public static synchronized Cursor execQuery(String sql) throws Exception {
        Cursor cursor = null;
        cursor = mDb.rawQuery(sql, null);
        return cursor;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newObject(T myObj) {
        Class<?> theClass = myObj.getClass();
        Object newObject = null;
        try {
            newObject = theClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) newObject;
    }

    @SuppressWarnings("unchecked")
    public static void objectMapping(Object mainObj, String key, Cursor cursor,
                                     int index) {
        Field[] fields = mainObj.getClass().getFields();

        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(key)) {
                try {

                    if (field.getType().getName().contains("int")) {
                        field.setInt(mainObj, cursor.getInt(index));
                    } else if (field.getType().getName().contains("String")) {
                        field.set(mainObj, cursor.getString(index));
                    } else if (field.getType().getName().contains("double")) {
                        field.setDouble(mainObj, cursor.getFloat(index));
                    } else if (field.getType().getName().contains("long")) {
                        field.setLong(mainObj, cursor.getLong(index));
                    } else if (field.getType().isEnum()) {
                        field.set(mainObj, Enum.valueOf(field.getType()
                                .asSubclass(Enum.class), cursor
                                .getString(index)));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public List<String> getTables(SQLiteDatabase sqLiteDatabase) {
        String sqlTables = "SELECT name FROM sqlite_master WHERE type='table'";
        List<String> tables = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sqlTables, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String tableName = cursor.getString(0);
                    tables.add(tableName);
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            cursor = null;
            e.printStackTrace();
        }
        return tables;
    }

    public void truncateAllTable(SQLiteDatabase sqLiteDatabase) {
        List<String> tables = getTables(sqLiteDatabase);
        // Truncate All Tables
        for (String tableName : tables) {
            truncateTable(tableName, sqLiteDatabase);
        }
    }

    public void truncateTable(String tableName, SQLiteDatabase sqLiteDatabase) {
        try {
            String sqlTruncate = String.format("Delete from %s", tableName);
            sqLiteDatabase.execSQL(sqlTruncate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
