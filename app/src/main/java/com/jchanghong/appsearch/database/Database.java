package com.jchanghong.appsearch.database;

import android.provider.BaseColumns;

class Database {
    public static final String DB_NAME = "xdesktophelper.db";
    public static final int DB_VERSION = 2;

    public interface Table {

        interface AppStartRecord {
            String APP_START_RECORD_TABLE = "app_start_record";
        }

        interface AppSettingInfo {
            String APP_INFO_TABLE = "app_setting_info";
        }

    }

    public interface AppSettingInfoColumns extends BaseColumns {
        String ID = "id";
        String KEY = "key";
        String SET_TO_TOP = "set_to_top";//set set_to_top time(currentTimeMillis)
    }

    public interface AppStartRecordColumns extends BaseColumns {
        String ID = "id";
        String KEY = "key";
        String START_TIME = "start_time";// start time(currentTimeMillis)
        // public final String SET_TO_TOP="set_to_top";//set set_to_top time(currentTimeMillis)
    }
}
