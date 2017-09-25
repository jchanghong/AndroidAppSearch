package com.jchanghong.appsearch.model;

public class AppStartRecord implements Comparable<AppStartRecord>{
    public final String packet_name;
    public final long mStartTime;

    public AppStartRecord(String key, long startTime) {
        packet_name = key;
        mStartTime = startTime;

    }

    @Override
    public int compareTo(AppStartRecord record) {
        if (mStartTime == record.mStartTime) {
            return 0;
        }
        return (record.mStartTime - mStartTime)>0?1:-1;
    }

}
