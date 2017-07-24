package com.jchanghong.appsearch.application;

import android.app.Application;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.database.XDesktopHelperSQLiteOpenHelper;
import com.jchanghong.appsearch.helper.AppInfoHelper;


public class XDesktopHelperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInfoHelper.mInstance.mContext = this;
        AppStartRecordDataBaseHelper.mInstance.mXDesktopHelperSQLiteOpenHelper = XDesktopHelperSQLiteOpenHelper.getInstance(this);
    }
}
