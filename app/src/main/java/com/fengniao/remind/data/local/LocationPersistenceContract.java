package com.fengniao.remind.data.local;


import android.provider.BaseColumns;

public class LocationPersistenceContract {
    private LocationPersistenceContract() {
    }

    public static interface LocationEntry extends BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_ACITVATE = "activate";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_POST_CODE = "postCode";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
//        public static final String COLUMN_NAME_CREATE_TIME = "createTime";
    }

}
