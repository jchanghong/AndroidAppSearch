package com.jchanghong.appsearch.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

import java.util.ArrayList;
import java.util.List;


public class AppStartRecordDataBaseHelper {
    static private String[] appStartRecordColumns = {
            Database.AppStartRecordColumns.PACKET_NAME,
            Database.AppStartRecordColumns.START_TIME,

    };
    private SQLiteOpenHelper sqLiteOpenHelper;

    public AppStartRecordDataBaseHelper(AppService service) {
        sqLiteOpenHelper = SQLiteOpenHelper.getInstance(service.getApplicationContext());
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.close();
    }

    public boolean insert(AppStartRecord appStartRecord) {
        boolean insertSuccess = false;
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if (null != db) {
            String whereClause = Database.AppStartRecordColumns.PACKET_NAME + " =?";
            String[] whereArgs = new String[]{appStartRecord.packet_name};
            db.delete(Database.Table.APP_START_RECORD_TABLE, whereClause, whereArgs);
            ContentValues conferenceMemberValues = new ContentValues();
            conferenceMemberValues.put(Database.AppStartRecordColumns.PACKET_NAME, appStartRecord.packet_name);
            conferenceMemberValues.put(Database.AppStartRecordColumns.START_TIME, appStartRecord.mStartTime);
            db.insert(Database.Table.APP_START_RECORD_TABLE, null, conferenceMemberValues);
            db.close();
            insertSuccess = true;
        }
        return insertSuccess;
    }

    /*start: delete*/
    public boolean delete(String packet_neme) {
        boolean deleteSuccess = false;
        if (TextUtils.isEmpty(packet_neme)) {
            deleteSuccess = false;
            return deleteSuccess;
        }
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        if (null != db) {
            String whereClause = Database.AppStartRecordColumns.PACKET_NAME + " =?";
            String[] whereArgs = new String[]{packet_neme};
            db.delete(Database.Table.APP_START_RECORD_TABLE, whereClause, whereArgs);
            db.close();
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * 只执行一次 linlist
     * start: query
     */
    public List<AppStartRecord> queryALL() {

        List<AppStartRecord> cache = new ArrayList<>();
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        if (null == db) {
            return cache;
        }

//            String appStartRecordOrderBy=Database.AppStartRecordColumns.KEY+" ASC";//" DESC";
        String appStartRecordOrderBy = Database.AppStartRecordColumns.START_TIME + " DESC";//" DESC";
        Cursor appStartRecordCursor = db.query(Database.Table.APP_START_RECORD_TABLE, appStartRecordColumns, null, null, null, null, appStartRecordOrderBy);
        if (null != appStartRecordCursor) {
            int keyColumnIndex = appStartRecordCursor.getColumnIndex(appStartRecordColumns[0]);
            int startTimeColumnIndex = appStartRecordCursor.getColumnIndex(appStartRecordColumns[1]);

            while (appStartRecordCursor.moveToNext()) {
                String key = appStartRecordCursor.getString(keyColumnIndex);
                long startTime = appStartRecordCursor.getLong(startTimeColumnIndex);
                AppStartRecord appStartRecord = new AppStartRecord(key, startTime);
                cache.add(appStartRecord);
            }
            appStartRecordCursor.close();
        }
        db.close();
        return cache;
    }
    /*end: delete*/

}
