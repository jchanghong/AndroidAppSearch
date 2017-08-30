package com.jchanghong.appsearch.database;

import android.provider.BaseColumns;

class Database {
    public static final String DB_NAME = "changhongappsearch.db";
    public static final int DB_VERSION = 1;
    public interface Table {
            String APP_START_RECORD_TABLE = "app_start_record";
    }
    public interface AppStartRecordColumns extends BaseColumns {
        String PACKET_NAME = "p_name";
        String START_TIME = "start_time";// start time(currentTimeMillis)
    }
}
