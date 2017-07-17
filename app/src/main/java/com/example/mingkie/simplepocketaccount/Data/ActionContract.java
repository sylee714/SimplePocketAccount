package com.example.mingkie.simplepocketaccount.Data;

import android.provider.BaseColumns;

/**
 * Created by MingKie on 7/12/2017.
 */

public class ActionContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ActionContract() {

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ActionEntry.TABLE_NAME + " (" +
                    ActionEntry._ID + " INTEGER PRIMARY KEY," +
                    ActionEntry.COLUMN_NAME_ACTIONTYPE + " TEXT," +
                    ActionEntry.COLUMN_NAME_YEAR + " INTEGER," +
                    ActionEntry.COLUMN_NAME_MONTH + " INTEGER," +
                    ActionEntry.COLUMN_NAME_DAYOFMONTH + " INTEGER," +
                    ActionEntry.COLUMN_NAME_DAYOFWEEK + " INTEGER," +
                    ActionEntry.COLUMN_NAME_WEEKOFMONTH + " INTEGER," +
                    ActionEntry.COLUMN_NAME_WEEKOFYEAR + " INTEGER," +
                    ActionEntry.COLUMN_NAME_CATEGORY + " TEXT," +
                    ActionEntry.COLUMN_NAME_PAYMENT + " TEXT," +
                    ActionEntry.COLUMN_NAME_NOTES + " TEXT," +
                    ActionEntry.COLUMN_NAME_AMOUNT + " REAL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ActionEntry.TABLE_NAME;

    public static class ActionEntry implements BaseColumns {
        public static final String TABLE_NAME = "actionHistory";
        public static final String COLUMN_NAME_ACTIONTYPE = "actionType";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAYOFMONTH = "dayOfMonth";
        public static final String COLUMN_NAME_DAYOFWEEK = "dayOfWeek";
        public static final String COLUMN_NAME_WEEKOFMONTH = "weekOfMonth";
        public static final String COLUMN_NAME_WEEKOFYEAR = "weekOfYear";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_PAYMENT = "payment";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }

    public static String getSqlDeleteEntries() {
        return SQL_DELETE_ENTRIES;
    }
}
