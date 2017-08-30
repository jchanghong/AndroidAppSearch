package com.jchanghong.appsearch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
//    private static final String TAG = "SQLiteOpenHelper";
//    private static final String CREATE_APP_INFO_TABLE = "create table "
//            + Database.Table.AppSettingInfo.APP_INFO_TABLE
//            + "("
//            + Database.AppSettingInfoColumns.ID + " integer unique,"
//            + Database.AppSettingInfoColumns.KEY + " text,"
//            + Database.AppSettingInfoColumns.SET_TO_TOP + " integer"
//            + ")";
    private static final String CREATE_APP_START_RECORD_TABLE = "create table "
            + Database.Table.AppStartRecord.APP_START_RECORD_TABLE
            + "("
            + Database.AppStartRecordColumns.ID + " integer unique,"
            + Database.AppStartRecordColumns.KEY + " text,"
            + Database.AppStartRecordColumns.START_TIME + " integer"
            + ")";
    private static SQLiteOpenHelper mInstance;

    /**
     * Constructor should be private to prevent direct instantiation. make call to static method
     * "getInstance()" instead.
     */
    private SQLiteOpenHelper(Context context) {
        super(context, Database.DB_NAME, null,
                Database.DB_VERSION);
        // Log.i(TAG, "DB_NAME:"+OscillationWaveDatabase.DB_NAME);
    }

    public static synchronized SQLiteOpenHelper getInstance(Context context) {
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
//        db.execSQL(CREATE_APP_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Database.Table.AppStartRecord.APP_START_RECORD_TABLE);
//        db.execSQL("drop table if exists "+ Database.Table.AppSettingInfo.APP_INFO_TABLE);

        onCreate(db);
    }

}
