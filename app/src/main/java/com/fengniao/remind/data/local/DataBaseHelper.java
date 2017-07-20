package com.fengniao.remind.data.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_ACITVATE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_ADDRESS;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_CITY;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_LATITUDE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_LONGITUDE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_NAME;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_POST_CODE;


public class DataBaseHelper extends SQLiteOpenHelper {
    //数据库版本号
    public static final int DATABASE_VERSION = 1;
    //数据库名称
    public static final String DATABASE_NAME = "Remind.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String DOUBLE_TYPE = " REAL";

    private static final String LONG_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String CREATE_LOCATION =
            "CREATE TABLE " + LocationPersistenceContract.LocationEntry.TABLE_NAME + " (" +
                    LocationPersistenceContract.LocationEntry._ID + LONG_TYPE + " PRIMARY KEY," +
                    COLUMN_NAME_ACITVATE + BOOLEAN_TYPE + COMMA_SEP +
                    COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_POST_CODE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    COLUMN_NAME_LONGITUDE + DOUBLE_TYPE + " )";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
