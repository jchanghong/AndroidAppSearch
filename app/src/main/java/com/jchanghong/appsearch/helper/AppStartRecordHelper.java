package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;
import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.model.LoadStatus;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

//import android.util.Log;


public class AppStartRecordHelper {
    public static final AppStartRecordHelper mInstance = new AppStartRecordHelper();
    private final List<AppStartRecord> mAppStartRecords = new ArrayList<>();
    public LinkedList<String> mrecords = null;
    private LoadStatus mAppStartRecordsLoadStatus;
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

    private void setAppStartRecordsLoadStatus(LoadStatus appStartRecordsLoadStatus) {
        mAppStartRecordsLoadStatus = appStartRecordsLoadStatus;
    }

    private void initAppStartRecordHelper() {
        setAppStartRecordsLoadStatus(LoadStatus.NOT_LOADED);
    }

    public boolean startLoadAppStartRecord() {
//        if(true==isAppStartRecordLoading()){
//            return false;
//        }
//
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

//    public boolean isAppStartRecordLoading(){
//        return ((null!=mLoadAppStartRecordTask)&&(mLoadAppStartRecordTask.getStatus()==Status.RUNNING));
//
//    }

    private List<AppStartRecord> loadAppStartRecord() {
        setAppStartRecordsLoadStatus(LoadStatus.LOADING);
        return AppStartRecordDataBaseHelper.mInstance.queryAllStocks();
    }

    private void parseAppStartRecord(List<AppStartRecord> appStartRecords) {
        if (appStartRecords.size() < 1) {
            setAppStartRecordsLoadStatus(LoadStatus.NOT_LOADED);
            onAppStartRecordFailed();
            return;
        }
        mAppStartRecords.clear();
        mAppStartRecords.addAll(appStartRecords);

        setAppStartRecordsLoadStatus(LoadStatus.LOAD_FINISH);
        onAppStartRecordSuccess();

    }
}
