package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;
import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppStartRecord;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

//import android.util.Log;


public class AppStartRecordHelper {
    public static final AppStartRecordHelper mInstance = new AppStartRecordHelper();
    private final List<AppStartRecord> mAppStartRecords = new ArrayList<>();
    public LinkedList<String> mrecords = null;
    private AsyncTask<Object, Object, List<AppStartRecord>> mLoadAppStartRecordTask = null;

    private AppStartRecordHelper() {
        initAppStartRecordHelper();
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

    private void onAppStartRecordFailed() {

    }

    private void initAppStartRecordHelper() {

    }

    public boolean startLoadAppStartRecord() {

        mLoadAppStartRecordTask = new AsyncTask<Object, Object, List<AppStartRecord>>() {

            @Override
            protected List<AppStartRecord> doInBackground(Object... params) {
                // TODO Auto-generated method stub
                return loadAppStartRecord();
            }

            @Override
            protected void onPostExecute(List<AppStartRecord> result) {
                parseAppStartRecord(result);
                mLoadAppStartRecordTask = null;
                super.onPostExecute(result);

            }


        }.execute();

        return true;
    }

    private List<AppStartRecord> loadAppStartRecord() {
        return AppStartRecordDataBaseHelper.mInstance.queryAllStocks();
    }

    private void parseAppStartRecord(List<AppStartRecord> appStartRecords) {
        if (appStartRecords==null) {
            onAppStartRecordFailed();
            return;
        }
        mAppStartRecords.clear();
        mAppStartRecords.addAll(appStartRecords);
        onAppStartRecordSuccess();

    }
}
