package com.jchanghong.appsearch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
    private static final String CREATE_APP_START_RECORD_TABLE = "create table "
            + Database.Table.APP_START_RECORD_TABLE
            + "("
            + Database.AppStartRecordColumns.PACKET_NAME + " text PRIMARY Key,"
            + Database.AppStartRecordColumns.START_TIME + " integer"
            + ")";
    private static SQLiteOpenHelper mInstance;

    private SQLiteOpenHelper(Context context) {
        super(context, Database.DB_NAME, null,
                Database.DB_VERSION);
    }

    static SQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new SQLiteOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APP_START_RECORD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Database.Table.APP_START_RECORD_TABLE);
        onCreate(db);
    }

}
