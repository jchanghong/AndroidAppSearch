package com.jchanghong.appsearch.model;

public class AppStartRecord {
    public final String packet_name;
    public final long mStartTime;

    public AppStartRecord(String key, long startTime) {
        packet_name = key;
        mStartTime = startTime;

    }
}
