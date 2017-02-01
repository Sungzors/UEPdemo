package com.example.sungwon.usaepaysandbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by SungWon on 1/13/2017.
 */

public class SQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "customer_db";

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    private static SQLHelper INSTANCE;

    public static synchronized SQLHelper getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SQLHelper(context.getApplicationContext());
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(SQL_CREATE_ENTRIES_EVERYTHING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(SQL_DELETE_ENTRIES_EVERYTHING);
        onCreate(db);
    }

    /**
     * Inner class which represents the columns in everything table.
     */
    public static abstract class everythingTable implements BaseColumns {
        public static final String TABLE_NAME = "customer_table";
        public static final String COLUMN_EVERYTHING = "customer_number";
        public static final String COLUMN_TAGSTHING = "billing_address";
        public static final String COLUMN_RATINGS = "date_created";
        public static final String COLUMN_CATEGORY_ID = "note";
        public static final String COLUMN_REVIEW = "review";
        public static final String COLUMN_PICTURE = "picture";
    }


    /**
     * SQL command to create everything table.
     */
    private static final String SQL_CREATE_ENTRIES_EVERYTHING = "CREATE TABLE " +
            everythingTable.TABLE_NAME + " (" +
            everythingTable._ID + " INTEGER PRIMARY KEY," +
            everythingTable.COLUMN_EVERYTHING + " TEXT," +
            everythingTable.COLUMN_TAGSTHING + " TEXT," +
            everythingTable.COLUMN_RATINGS + " INTEGER," +
            everythingTable.COLUMN_CATEGORY_ID + " INTEGER," +
            everythingTable.COLUMN_REVIEW + " TEXT,"+
            everythingTable.COLUMN_PICTURE + " TEXT" + ")";

    /**
     * SQL command to delete our everything table.
     */
    private static final String SQL_DELETE_ENTRIES_EVERYTHING = "DROP TABLE IF EXISTS " +
            everythingTable.TABLE_NAME;

}
