package com.jchanghong.appsearch.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppInfo.SearchByType;
import com.jchanghong.appsearch.service.AppService;
import com.jchanghong.appsearch.util.AppUtil;
import com.pinyinsearch.model.PinyinSearchUnit;
import com.pinyinsearch.util.PinyinUtil;
import com.pinyinsearch.util.T9Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AppInfoHelper {
    private AppService mContext;
    public List<AppInfo> mT9SearchAppInfos=new ArrayList<>();
    private OnAppInfoLoad mOnAppInfoLoad;//回调
    public List<AppInfo> mBaseAllAppInfos=new ArrayList<>();
    public HashMap<String, AppInfo> mBaseAllAppInfosHashMap = new HashMap<>();
    private StringBuilder mFirstNoT9SearchResultInput = new StringBuilder();
    private volatile boolean mloading = false;
    public AppInfoHelper(AppService mContext,OnAppInfoLoad load) {
        this.mContext = mContext;
        mOnAppInfoLoad = load;
    }

    public void startLoadAppInfo() {
        if (mloading) {
            return ;
        }
       new AsyncTask<Object, Object, List<AppInfo>>() {
            @Override
            protected List<AppInfo> doInBackground(Object... params) {
                // TODO Auto-generated method stub
                mloading = true;
                return loadAppInfo(mContext);
            }
            @Override
            protected void onPostExecute(List<AppInfo> result) {
                super.onPostExecute(result);
                parseAppInfo(result);
                //setAppInfoChanged(false);
                mloading = false;
            }

        }.execute();
    }

    /**
     * 后台加载-------------------*/
    @SuppressLint("DefaultLocale")
    private List<AppInfo> loadAppInfo(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(it, 0);
        for (ResolveInfo ri : resolveInfos) {
            boolean canLaunchTheMainActivity = AppUtil.appCanLaunchTheMainActivity(mContext, ri.activityInfo.packageName);
            if (canLaunchTheMainActivity) {
                AppInfo appInfo = getAppInfo(pm, ri);
                if (TextUtils.isEmpty(appInfo.mLabel)) {
                    continue;
                }
                else {
                    PinyinUtil.parse(appInfo.mLabelPinyinSearchUnit);
                    appInfos.add(appInfo);
                }
            }
        }
        return appInfos;
    }

    /**
     * 设置t9list数据，view自己更新*/
    public void t9Search(String search) {
        List<AppInfo> baseAppInfos = mBaseAllAppInfos;
        if (TextUtils.isEmpty(search)) {
            searchEmpty(baseAppInfos);
            return;
        }
        if (mFirstNoT9SearchResultInput.length() > 0) {
            if (search.contains(mFirstNoT9SearchResultInput.toString())) {
                return;
            } else {
                mFirstNoT9SearchResultInput.delete(0, mFirstNoT9SearchResultInput.length());
            }
        }
        mT9SearchAppInfos.clear();
        for (AppInfo baseAppInfo : baseAppInfos) {
            PinyinSearchUnit labelPinyinSearchUnit = baseAppInfo.mLabelPinyinSearchUnit;
            boolean match = T9Util.match(labelPinyinSearchUnit, search);
            if (match) {// search by LabelPinyinUnits;
                AppInfo appInfo = baseAppInfo;
                appInfo.mSearchByType = SearchByType.SearchByLabel;
                appInfo.setMatchKeywords(labelPinyinSearchUnit.getMatchKeyWord().toString());
                appInfo.mMatchStartIndex=(appInfo.mLabel.indexOf(appInfo.mMatchKeywords.toString()));
                appInfo.mMatchLength=(appInfo.mMatchKeywords.length());
                mT9SearchAppInfos.add(appInfo);
            }
        }

        if (mT9SearchAppInfos.size() <= 0) {
            if (mFirstNoT9SearchResultInput.length() <= 0) {
                mFirstNoT9SearchResultInput.append(search);
            }
        } else {
                Collections.sort(mT9SearchAppInfos, AppInfo.mSortBySearch);
        }
    }

    //最新记录
    private void searchEmpty(List<AppInfo> baseAppInfos) {
        for (AppInfo ai : baseAppInfos) {
            ai.mSearchByType = (SearchByType.SearchByTIME);
            ai.clearMatchKeywords();
            ai.mMatchStartIndex = (-1);
            ai.mMatchLength = (0);
        }
        Collections.sort(baseAppInfos, AppInfo.mSortByTime);
    }

    public boolean isAppExist(String packageName) {
        boolean appExist = false;
            for (AppInfo ai : mBaseAllAppInfos) {
                if (ai.mPackageName.equals(packageName)) {
                    appExist = true;
                    break;
                }
            }
        return appExist;
    }

    public boolean add(String packageName) {
        boolean addSuccess = false;
            boolean canLaunchTheMainActivity = AppUtil.appCanLaunchTheMainActivity(mContext, packageName);

            if (canLaunchTheMainActivity) {
                PackageManager pm = mContext.getPackageManager();
                Intent intent = new Intent();
                intent.setPackage(packageName);
                ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

                if (null != resolveInfo) {
                    AppInfo appInfo = getAppInfo(pm, resolveInfo);
                    if (TextUtils.isEmpty(appInfo.mLabel)) {
                        addSuccess = false;
                        return addSuccess;
                    }
                    mBaseAllAppInfosHashMap.put(appInfo.mPackageName, appInfo);
                    mBaseAllAppInfos.add(appInfo);
                    Collections.sort(mBaseAllAppInfos, AppInfo.mSortByTime);
                    addSuccess = true;
                }
            }

        return addSuccess;
    }


    public boolean remove(String packageName) {
      AppInfo v=  mBaseAllAppInfosHashMap.remove(packageName);
        if (v != null) {
            mBaseAllAppInfos.remove(v);
        }
        mContext.recordHelper.remove(packageName);
        return true;
    }

/**
 * 加载过*/
    public boolean loaded() {
        return mBaseAllAppInfos != null && mBaseAllAppInfos.size() > 0;
    }

    private AppInfo getAppInfo(PackageManager pm, ResolveInfo resolveInfo) {
        AppInfo appInfo = new AppInfo(resolveInfo.loadLabel(pm).toString(), resolveInfo.loadIcon(pm), resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        return appInfo;

    }

    private void parseAppInfo(List<AppInfo> appInfos) {
//		Log.i(TAG, "parseAppInfo");
        if (null == appInfos || appInfos.size() < 1) {
            if (null != mOnAppInfoLoad) {
                mOnAppInfoLoad.onAppInfoLoadFailed();
            }
            return;
        }

//		Log.i(TAG, "before appInfos.size()"+ appInfos.size());
        mBaseAllAppInfos.clear();
        mBaseAllAppInfos.addAll(appInfos);

        mBaseAllAppInfosHashMap.clear();
        for (AppInfo ai : mBaseAllAppInfos) {
            mBaseAllAppInfosHashMap.put(ai.mkey, ai);
        }
//		Log.i(TAG, "after appInfos.size()"+ appInfos.size());
        if (null != mOnAppInfoLoad) {
            mOnAppInfoLoad.onAppInfoLoadSuccess(mBaseAllAppInfos);
        }

    }

    public interface OnAppInfoLoad {
        void onAppInfoLoadSuccess(List<AppInfo> list);
        void onAppInfoLoadFailed();
    }
}
