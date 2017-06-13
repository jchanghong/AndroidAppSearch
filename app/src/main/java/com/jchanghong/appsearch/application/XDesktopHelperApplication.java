package com.jchanghong.appsearch.application;

import android.app.Application;
import android.content.Context;
import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.database.XDesktopHelperSQLiteOpenHelper;
import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.helper.AppStartRecordHelper;


public class XDesktopHelperApplication extends Application {

    @Override
    public void onCreate() {
        AppInfoHelper.mInstance.mContext = this;
        AppStartRecordDataBaseHelper.mInstance.mXDesktopHelperSQLiteOpenHelper = XDesktopHelperSQLiteOpenHelper.getInstance(this);
    }
}
