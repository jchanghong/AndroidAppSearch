package com.jchanghong.appsearch.helper;

import java.util.*;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
//import android.util.Log;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.model.LoadStatus;


public class AppStartRecordHelper {
    public  LinkedList<String> mrecords = null;
    public static AppStartRecordHelper mInstance=new AppStartRecordHelper();
    public List<AppStartRecord> mAppStartRecords;
    public LoadStatus mAppStartRecordsLoadStatus;
    public AsyncTask<Object, Object, List<AppStartRecord>> mLoadAppStartRecordTask=null;
    public OnAppStartRecordLoad mOnAppStartRecordLoad;
    
   
    public interface OnAppStartRecordLoad{
        void onAppStartRecordSuccess();
        void onAppStartRecordFailed();
    }


    
    public AppStartRecordHelper(){
        initAppStartRecordHelper();
    }
    

    public void setAppStartRecordsLoadStatus(LoadStatus appStartRecordsLoadStatus) {
        mAppStartRecordsLoadStatus = appStartRecordsLoadStatus;
    }


    
    public void initAppStartRecordHelper(){
       if(null==mAppStartRecords){
           mAppStartRecords=new ArrayList<AppStartRecord>();
       }else{
           mAppStartRecords.clear();
       }
       setAppStartRecordsLoadStatus(LoadStatus.NOT_LOADED);
    }
    
    public boolean startLoadAppStartRecord(){
        if(true==isAppStartRecordLoading()){
            return false;
        }
        
        mLoadAppStartRecordTask=new AsyncTask<Object, Object, List<AppStartRecord>>(){

            @Override
            protected List<AppStartRecord> doInBackground(Object... params) {
                // TODO Auto-generated method stub
                return loadAppStartRecord();
            }

            @Override
            protected void onPostExecute(List<AppStartRecord> result) {
                parseAppStartRecord(result);
                mLoadAppStartRecordTask=null;
                super.onPostExecute(result);
               
            }
            
           
        }.execute();
        
        return true;
    }

    public boolean isAppStartRecordLoading(){
        return ((null!=mLoadAppStartRecordTask)&&(mLoadAppStartRecordTask.getStatus()==Status.RUNNING));

    }
    
    public List<AppStartRecord> loadAppStartRecord(){
        setAppStartRecordsLoadStatus(LoadStatus.LOADING);
    return     AppStartRecordDataBaseHelper.mInstance.queryAllStocks();
    }
    
    public void parseAppStartRecord(List<AppStartRecord> appStartRecords){
        if(null==appStartRecords){
            setAppStartRecordsLoadStatus(LoadStatus.NOT_LOADED);
            if(null!=mOnAppStartRecordLoad){
                mOnAppStartRecordLoad.onAppStartRecordFailed();
            }
            return;
        }
        mAppStartRecords.clear();
        mAppStartRecords.addAll(appStartRecords);
        
        setAppStartRecordsLoadStatus(LoadStatus.LOAD_FINISH);
        if(null!=mOnAppStartRecordLoad){
            mOnAppStartRecordLoad.onAppStartRecordSuccess();
        }
        mrecords = new LinkedList<>();
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (AppStartRecord mAppStartRecord : mAppStartRecords) {
            set.add(mAppStartRecord.getKey());
        }
        for (String s : set) {
            mrecords.addLast(s);
        }
    }
}
