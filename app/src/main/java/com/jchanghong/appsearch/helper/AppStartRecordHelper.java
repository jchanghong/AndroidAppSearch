package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

import java.util.ArrayList;
import java.util.List;

//import android.util.Log;

/**
 * 数据库中的记录*/
public class AppStartRecordHelper {
    private  List<AppStartRecord> mAppStartRecords = new ArrayList<>();
    private AppStartRecordDataBaseHelper helper;
    public OnRecordLister lister;
    public interface OnRecordLister {
        void oncomplete(List<AppStartRecord> list);
    }
    private AppService service;
    public AppStartRecordHelper(AppService appService) {
        service = appService;
        helper = new AppStartRecordDataBaseHelper(appService);
    }

    public void indert(final AppStartRecord record) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                final AppStartRecord record1 = record;
                helper.insert(record1);
                return null;
            }
        }.execute();

    }

    public void remove(final String pcname) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final String record1 = pcname;
                helper.delete(record1);
                return null;
            }
        }.execute();
    }
    private volatile boolean mloading = false;
    public void startLoadAppStartRecord() {
        if (mloading) {
            return;
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
    }

    private List<AppStartRecord> loadAppStartRecord() {
        return helper.queryALL();
    }

    private void parseAppStartRecord(List<AppStartRecord> appStartRecords) {
        mAppStartRecords = appStartRecords;
        if (lister != null) {
            lister.oncomplete(appStartRecords);
        }
    }
}
