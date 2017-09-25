package com.jchanghong.appsearch.helper;

import android.os.AsyncTask;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据库中的记录
 * 单例
 */
public class AppStartRecordHelper {
    public final Map<String, AppStartRecord> cache = new HashMap<>();
    private final AppStartRecordDataBaseHelper helper;
    public OnRecordLister lister;
    private volatile boolean mloading = false;

    public AppStartRecordHelper(AppService appService) {
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
                helper.insert(record);
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
                helper.delete(pcname);
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
