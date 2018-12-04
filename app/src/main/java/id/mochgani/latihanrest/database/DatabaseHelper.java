package id.mochgani.latihanrest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import id.mochgani.latihanrest.entity.User;
import id.mochgani.latihanrest.rest.ApiClient;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String SETUP_TABLE = "t_setup";
    private static final String USER_TABLE = "t_user";
    private static final String DATABASE_NAME = "db_latihanrest";

    public static final String KEY_ID = "_id";

    public static final String KEY_KEY = "key";
    public static final String KEY_VAL = "value";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_IDUSER = "id_user";
    public static final String KEY_NAME = "nama";
    public static final String KEY_TELP = "telepon";

    private static final String SETUP_TABLE_CREATE =
            "CREATE TABLE " + SETUP_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_KEY + " TEXT, " +
                    KEY_VAL + " TEXT );";

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_IDUSER + " INTEGER, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_NAME + " TEXT, " +
                    KEY_TELP + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Database Terbuat");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SETUP_TABLE_CREATE);
        Log.d(TAG, "Tabel Setup Terbuat");

        db.execSQL(USER_TABLE_CREATE);
        Log.d(TAG, "Tabel User Terbuat");

        fillSetup(db);
    }

    public void fillSetup(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(KEY_KEY, "base_url");
        values.put(KEY_VAL, "http://192.168.1.3/latihan_rest_ci/index.php/");
        db.insert(SETUP_TABLE, null, values);

        Log.d(TAG, "Data Base URL Masuk");
    }

    public String getDataSingle(String namaTabel, String fieldWhere, String valueWhere, String fieldOutput){
        String query = "SELECT  * FROM " + namaTabel + " WHERE " + fieldWhere + "='" + valueWhere + "'";

        Cursor cursor = null;
        String word = null;

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            word = cursor.getString(cursor.getColumnIndex(fieldOutput));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return word;
        }
    }

    public String getBaseUrl(){
        String query = "SELECT  * FROM " + SETUP_TABLE + " WHERE " + KEY_KEY + "='base_url'";

        Cursor cursor = null;
        String word = null;

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            word = cursor.getString(cursor.getColumnIndex(KEY_VAL));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return word;
        }
    }

    public int cekSession(){
        String query = "SELECT  count(*) as jml FROM " + USER_TABLE;

        Cursor cursor = null;
        int jml = 0;

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            jml = Integer.parseInt(cursor.getString(cursor.getColumnIndex("jml")));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return jml;
        }
    }

    public User getSessionUser(){
        String query = "SELECT  * FROM " + USER_TABLE;

        Cursor cursor = null;
        User dataUser = null;

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            dataUser = new User(cursor.getString(cursor.getColumnIndex(KEY_IDUSER)),
                                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                                cursor.getString(cursor.getColumnIndex(KEY_TELP)),
                                cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return dataUser;
        }
    }

    public long insertUser(User dataUser) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_IDUSER, dataUser.getIdUser());
        values.put(KEY_USERNAME, dataUser.getUserName());
        values.put(KEY_NAME, dataUser.getNama());
        values.put(KEY_TELP, dataUser.getTelepon());
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            deleteUser();
            newId = mWritableDB.insert(USER_TABLE, null, values);
            Log.d(TAG, "Session Terbuat");
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    public int updateSetup(String updateKey, String updateValue) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            ContentValues values = new ContentValues();
            values.put(KEY_VAL, updateValue);

            mNumberOfRowsUpdated = mWritableDB.update(SETUP_TABLE, //table to change
                    values, // new values to insert
                    KEY_KEY + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(updateKey)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    public void deleteUser() {
        if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
        mWritableDB.execSQL("DELETE FROM " + USER_TABLE);
        Log.d(TAG, "USER Clear");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETUP_TABLE);
        onCreate(db);
    }
}
