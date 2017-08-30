package com.jchanghong.appsearch.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.helper.AppStartRecordHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;

import java.util.List;

public class AppService extends Service {
    private static final String ACTION_X_DESKTOP_HELPER_SERVICE = "com.jchanghong.appsearch.service.X_DESKTOP_HELPER_SERVICE";

    private static String LOG = AppService.class.getSimpleName();
    public AppInfoHelper appInfoHelper;
    public AppStartRecordHelper recordHelper;
    public static void startService(Context context,Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        String action = intent.getAction();
        Intent intent1 = new Intent(context, AppService.class);
        intent1.putExtra("name", packageName);
        intent1.putExtra("action1", action);
        Log.i(LOG, action + ":" + packageName);
        intent1.setAction(AppService.ACTION_X_DESKTOP_HELPER_SERVICE);
        context.startService(intent1);

    }

    public List<AppInfo> appInfoList;
    public Ondata ondata;
    public interface Ondata {
        void onAppinfo(List<AppInfo> list);

        void onAppinfoChanged();
        void onrecode(List<AppStartRecord> list);
    }

    public class MYBinder extends Binder {
      public   AppService getserver() {
            return AppService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new MYBinder();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        appInfoHelper = new AppInfoHelper(this);
        recordHelper = new AppStartRecordHelper(this);
        appInfoHelper.mOnAppInfoLoad = new AppInfoHelper.OnAppInfoLoad() {
            @Override
            public void onAppInfoLoadSuccess(List<AppInfo> list) {
                AppService.this.appInfoList = list;
                if (AppService.this.ondata != null) {
                    AppService.this.ondata.onAppinfo(list);
                }
            }

            @Override
            public void onAppInfoLoadFailed() {

            }
        };
        recordHelper.lister =new AppStartRecordHelper.OnRecordLister() {
            @Override
            public void oncomplete(List<AppStartRecord> list) {
                if (AppService.this.ondata != null) {
                    AppService.this.ondata.onrecode(list);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            appInfoHelper.startLoadAppInfo();
            return START_STICKY;
        }
        String action = intent.getStringExtra("action1");
        String packageName = intent.getStringExtra("name");
        if (action == null||packageName==null) {
            appInfoHelper.startLoadAppInfo();
        }
        else {
            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                if (!appInfoHelper.isAppExist(packageName)) {
                    appInfoHelper.add(packageName);
                    if (ondata != null) {
                        ondata.onAppinfoChanged();
                    }
                }
            } else if (action.equals(Intent.ACTION_PACKAGE_CHANGED)) {
           appInfoHelper.startLoadAppInfo();
                if (ondata != null) {
                    ondata.onAppinfoChanged();
                }
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                appInfoHelper.remove(packageName);
                if (ondata != null) {
                    ondata.onAppinfoChanged();
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}