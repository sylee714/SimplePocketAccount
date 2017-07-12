package com.example.mingkie.simplepocketaccount.Data;

import android.provider.BaseColumns;

/**
 * Created by MingKie on 7/12/2017.
 */

public class TransactionContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TransactionContract() {

    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TransactionEntry.TABLE_NAME + " (" +
                    TransactionEntry._ID + " INTEGER PRIMARY KEY," +
                    TransactionEntry.COLUMN_NAME_TRANSACTIONTYPE + " TEXT," +
                    TransactionEntry.COLUMN_NAME_DATE + " TEXT)" +
                    TransactionEntry.COLUMN_NAME_TYPE + " TEXT)" +
                    TransactionEntry.COLUMN_NAME_AMOUNT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TransactionEntry.TABLE_NAME;

    public static class TransactionEntry implements BaseColumns {
        public static final String TABLE_NAME = "transaction";
        public static final String COLUMN_NAME_TRANSACTIONTYPE = "transactionType";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }

    public static String getSqlDeleteEntries() {
        return SQL_DELETE_ENTRIES;
    }

}
