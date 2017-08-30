package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.util.Log;

/**
 * 数据库中的记录
 */
public class AppStartRecordHelper {
    public Map<String, AppStartRecord> cache = new HashMap<>();
    public OnRecordLister lister;
    private AppStartRecordDataBaseHelper helper;
    private AppService service;
    private volatile boolean mloading = false;

    public AppStartRecordHelper(AppService appService) {
        service = appService;
        helper = new AppStartRecordDataBaseHelper(appService);
    }

    public void insert(final AppStartRecord record) {
        cache.put(record.packet_name, record);
        if (lister != null) {
            lister.onUpdate();
        }
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
        cache.remove(pcname);
        if (lister != null) {
            lister.onUpdate();
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final String record1 = pcname;
                helper.delete(record1);
                return null;
            }
        }.execute();
    }

    /*只调用一次,通知数据*/
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
        for (AppStartRecord record : appStartRecords) {
            cache.put(record.packet_name, record);
        }
        if (lister != null) {
            lister.onUpdate();
        }
    }

    public interface OnRecordLister {
        void onUpdate();
    }
}
