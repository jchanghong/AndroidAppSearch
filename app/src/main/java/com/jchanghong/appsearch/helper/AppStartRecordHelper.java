package com.jchanghong.appsearch.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
//import android.util.Log;

import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.model.LoadStatus;
import com.jchanghong.appsearch.util.AppCommonWeightsUtil;



public class AppStartRecordHelper {
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
    
    public LoadStatus getAppStartRecordsLoadStatus() {
        return mAppStartRecordsLoadStatus;
    }

    public void setAppStartRecordsLoadStatus(LoadStatus appStartRecordsLoadStatus) {
        mAppStartRecordsLoadStatus = appStartRecordsLoadStatus;
    }

    public OnAppStartRecordLoad getOnAppStartRecordLoad() {
        return mOnAppStartRecordLoad;
    }

    public void setOnAppStartRecordLoad(OnAppStartRecordLoad onAppStartRecordLoad) {
        mOnAppStartRecordLoad = onAppStartRecordLoad;
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
    
    public boolean parseAppStartRecord(){
        boolean parseSuccess=false;
        
        do{
            AppInfo appInfo=null;
            long currentTimeMs=System.currentTimeMillis();
            if(mAppStartRecords.size()<=0){
                parseSuccess=false;
                break;
            }
            
            for(AppStartRecord asr:mAppStartRecords){
                if(true==AppInfoHelper.mInstance.mBaseAllAppInfosHashMap.containsKey(asr.getKey())){
                    appInfo= AppInfoHelper.mInstance.mBaseAllAppInfosHashMap.get(asr.getKey());
                    appInfo.setCommonWeights(appInfo.getCommonWeights()+AppCommonWeightsUtil.getCommonWeights(currentTimeMs, asr.getStartTime()));
                }
            }
            Collections.sort(AppInfoHelper.mInstance.mBaseAllAppInfos, AppInfo.mSortByDefault);
           /* for(int i=0; i<AppInfoHelper.mInstance.getBaseAllAppInfos().size() ; i++){
                Log.i(TAG, AppInfoHelper.mInstance.getBaseAllAppInfos().get(i).getLabel()+":"+AppInfoHelper.mInstance.getBaseAllAppInfos().get(i).getCommonWeights());
            }*/
            parseSuccess=true;
        }while(false);
        
        return parseSuccess;
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
        
      
    
    }
}
