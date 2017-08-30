package com.jchanghong.appsearch.database;

import android.provider.BaseColumns;

class Database {
    static final String DB_NAME = "changhongappsearch.db";
    static final int DB_VERSION = 2;

    interface Table {
        String APP_START_RECORD_TABLE = "app_start_record";
    }

    interface AppStartRecordColumns extends BaseColumns {
        String PACKET_NAME = "p_name";
        String START_TIME = "start_time";// start time(currentTimeMillis)
    }
}
