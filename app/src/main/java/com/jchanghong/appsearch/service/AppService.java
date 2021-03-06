package com.jchanghong.appsearch.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.helper.AppStartRecordHelper;
import com.jchanghong.appsearch.model.AppInfo;

import java.util.List;

public class AppService extends Service {
    private static final String ACTION_X_DESKTOP_HELPER_SERVICE = "com.jchanghong.appsearch.service.X_DESKTOP_HELPER_SERVICE";

    private static final String LOG = AppService.class.getSimpleName();
    public AppInfoHelper appInfoHelper;
    public AppStartRecordHelper recordHelper;
    public Ondata ondata;

    public static void startService(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        String action = intent.getAction();
        Intent intent1 = new Intent(context, AppService.class);
        intent1.putExtra("name", packageName);
        intent1.putExtra("action1", action);
//        Log.i(LOG, action + ":" + packageName);
        intent1.setAction(AppService.ACTION_X_DESKTOP_HELPER_SERVICE);
        context.startService(intent1);

    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.i(LOG, "onbind---------");
        // TODO Auto-generated method stub
        return new MYBinder();
    }

    @Override
    public void onCreate() {
//        Log.i(LOG, "oncreate------");
        super.onCreate();
        AppInfoHelper.OnAppInfoLoad onAppInfoLoad = new AppInfoHelper.OnAppInfoLoad() {
            @Override
            public void onAppInfoLoadSuccess(List<AppInfo> list) {
                if (AppService.this.ondata != null) {
                    AppService.this.ondata.onAppinfo(list);
                }
            }

        };
        appInfoHelper = new AppInfoHelper(this, onAppInfoLoad);
        recordHelper = new AppStartRecordHelper(this);
        recordHelper.lister = new AppStartRecordHelper.OnRecordLister() {
            @Override
            public void onUpdate() {
                if (AppService.this.ondata != null) {
                    AppService.this.ondata.onrecodeUpdate();
                }
            }
        };
    }

    /**
     * 异步得到数据
     */
    public void initDataSyn() {
        appInfoHelper.startLoadAppInfo();
        recordHelper.startLoadAppStartRecord();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(LOG, "onStartCommand");
        if (intent == null) {
            return START_STICKY;
        }
        String action = intent.getStringExtra("action1");
        String packageName = intent.getStringExtra("name");
        if (action != null && packageName != null) {
            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    if (!appInfoHelper.isAppExist(packageName)) {
                        appInfoHelper.add(packageName);
                        if (ondata != null) {
                            ondata.onAppinfoChanged();
                        }
                    }
                    break;
                case Intent.ACTION_PACKAGE_CHANGED:
                    if (ondata != null) {
                        ondata.onAppinfoChanged();
                    }
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    appInfoHelper.remove(packageName);
                    if (ondata != null) {
                        ondata.onAppinfoChanged();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    /**
     * 只作为通知，具体数据保存在各种的helper类里面
     */
    public interface Ondata {
        void onAppinfo(List<AppInfo> list);

        void onAppinfoChanged();

        void onrecodeUpdate();
    }

    public class MYBinder extends Binder {
        public AppService getserver() {
            return AppService.this;
        }
    }
}
