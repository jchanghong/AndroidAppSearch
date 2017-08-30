package com.jchanghong.appsearch.model;

public class AppStartRecord {
    public String packet_name;
    public long mStartTime;

    public AppStartRecord(String key, long startTime) {
        packet_name = key;
        mStartTime = startTime;

    }
}
