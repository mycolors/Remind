package com.fengniao.remind.data.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fengniao.remind.data.Location;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_ACITVATE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_ADDRESS;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_CITY;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_LATITUDE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_LONGITUDE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_NAME;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.COLUMN_NAME_POST_CODE;
import static com.fengniao.remind.data.local.LocationPersistenceContract.LocationEntry.TABLE_NAME;

public class LocalDataSource {
    private static LocalDataSource INSTANCE;

    private DataBaseHelper mDbHelper;

    private LocalDataSource(Context context) {
        mDbHelper = new DataBaseHelper(context.getApplicationContext());
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    public void saveLocation(Location location) {
        if (location == null) return;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, System.currentTimeMillis());
        values.put(COLUMN_NAME_ACITVATE, location.getActivate());
        values.put(COLUMN_NAME_NAME, location.getName());
        values.put(COLUMN_NAME_ADDRESS, location.getAddress());
        values.put(COLUMN_NAME_CITY, location.getCity());
        values.put(COLUMN_NAME_POST_CODE, location.getPostCode());
        values.put(COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, location.getLongitude());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    //获取所有的地点
    public List<Location> getAllLocation() {
        List<Location> list = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {_ID, COLUMN_NAME_ACITVATE, COLUMN_NAME_NAME, COLUMN_NAME_ADDRESS,
                COLUMN_NAME_CITY, COLUMN_NAME_POST_CODE, COLUMN_NAME_LATITUDE, COLUMN_NAME_LONGITUDE};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Location location = new Location();
                location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                location.setActivate(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_ACITVATE)) == 1);
                location.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME)));
                location.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ADDRESS)));
                location.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_CITY)));
                location.setPostCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_POST_CODE)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGITUDE)));
                list.add(location);
            }
        }
        if (cursor != null) cursor.close();
        db.close();
        return list;

    }


    public List<Location> getActivateLocation() {
        List<Location> list = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Location WHERE _ID = ?", new String[]{1 + ""});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Location location = new Location();
                location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                location.setActivate(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_ACITVATE)) == 1);
                location.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME)));
                location.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ADDRESS)));
                location.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_CITY)));
                location.setPostCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_POST_CODE)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGITUDE)));
                list.add(location);
            }
        }
        if (cursor != null) cursor.close();
        db.close();
        return list;
    }


    public void arrivedLocation(Location location) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ACITVATE, false);
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = {location.getId() + ""};
        Log.i("test", "id    " + location.getId());
        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }


    public void activateLocation(Location location) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ACITVATE, true);
        String selection = _ID + "LIKE ?";
        String[] selectionArgs = {location.getId() + ""};
        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }


}
