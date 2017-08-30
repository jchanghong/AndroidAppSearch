package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.database.SQLiteOpenHelper;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

//import android.util.Log;

/**
 * 数据库中的记录*/
public class AppStartRecordHelper {
    private  List<AppStartRecord> mAppStartRecords = new ArrayList<>();
    public LinkedList<String> mrecords = null;
    public static AppStartRecordDataBaseHelper helper;
    public OnRecordLister lister;
    public interface OnRecordLister {
        void oncomplete(List<AppStartRecord> list);
    }

    private AppService service;
    public AppStartRecordHelper(AppService appService) {
        service = appService;
        helper = new AppStartRecordDataBaseHelper();
        helper.sqLiteOpenHelper = SQLiteOpenHelper.getInstance(service);
    }
    private void onAppStartRecordSuccess() {
        mrecords = new LinkedList<>();
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (AppStartRecord mAppStartRecord : mAppStartRecords) {
            set.add(mAppStartRecord.getKey());
        }
        for (String s : set) {
            mrecords.addLast(s);
        }
    }

    private volatile boolean mloading = false;
    public boolean startLoadAppStartRecord() {
        if (mloading) {
            return false;
        }
            new AsyncTask<Object, Object, List<AppStartRecord>>() {
                @Override
                protected List<AppStartRecord> doInBackground(Object... params) {
                    mloading = true;
                    // TODO Auto-generated method stub
                    return loadAppStartRecord();
                }
                @Override
                protected void onPostExecute(List<AppStartRecord> result) {
                    super.onPostExecute(result);
                    parseAppStartRecord(result);
                    mloading = false;
                }
            }.execute();

        return true;
    }

    private List<AppStartRecord> loadAppStartRecord() {
        return helper.queryALL();
    }

    private void parseAppStartRecord(List<AppStartRecord> appStartRecords) {
        mAppStartRecords = appStartRecords;
        if (lister != null) {
            onAppStartRecordSuccess();
            lister.oncomplete(appStartRecords);
        }

    }
}
