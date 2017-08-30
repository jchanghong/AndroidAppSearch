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
    public final List<AppInfo> mT9SearchAppInfos = new ArrayList<>();
    public final List<AppInfo> mBaseAllAppInfos = new ArrayList<>();
    private final HashMap<String, AppInfo> mBaseAllAppInfosHashMap = new HashMap<>();
    private final AppService mContext;
    private final OnAppInfoLoad mOnAppInfoLoad;//回调
    private final StringBuilder mFirstNoT9SearchResultInput = new StringBuilder();
    private volatile boolean mloading = false;

    public AppInfoHelper(AppService mContext, OnAppInfoLoad load) {
        this.mContext = mContext;
        mOnAppInfoLoad = load;
    }

    public void startLoadAppInfo() {
        if (mloading) {
            return;
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
     * 后台加载-------------------
     */
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
                if (appInfo.mPackageName.equals(context.getPackageName())) {
                    break;
                }
                if (!TextUtils.isEmpty(appInfo.mLabel)) {
                    PinyinUtil.parse(appInfo.mLabelPinyinSearchUnit);
                    appInfos.add(appInfo);
                }
            }
        }
        return appInfos;
    }

    /**
     * 设置t9list数据，view自己更新
     */
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
                baseAppInfo.mSearchByType = SearchByType.SearchByLabel;
                baseAppInfo.setMatchKeywords(labelPinyinSearchUnit.getMatchKeyWord().toString());
                baseAppInfo.mMatchStartIndex = (baseAppInfo.mLabel.indexOf(baseAppInfo.mMatchKeywords.toString()));
                baseAppInfo.mMatchLength = (baseAppInfo.mMatchKeywords.length());
                mT9SearchAppInfos.add(baseAppInfo);
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

    public void add(String packageName) {
        boolean canLaunchTheMainActivity = AppUtil.appCanLaunchTheMainActivity(mContext, packageName);

        if (canLaunchTheMainActivity) {
            PackageManager pm = mContext.getPackageManager();
            Intent intent = new Intent();
            intent.setPackage(packageName);
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

            if (null != resolveInfo) {
                AppInfo appInfo = getAppInfo(pm, resolveInfo);
                if (TextUtils.isEmpty(appInfo.mLabel)) {
                    return;
                }
                mBaseAllAppInfosHashMap.put(appInfo.mPackageName, appInfo);
                mBaseAllAppInfos.add(appInfo);
                Collections.sort(mBaseAllAppInfos, AppInfo.mSortByTime);
            }
        }

    }


    public void remove(String packageName) {
        AppInfo v = mBaseAllAppInfosHashMap.remove(packageName);
        if (v != null) {
            mBaseAllAppInfos.remove(v);
        }
        mContext.recordHelper.remove(packageName);
    }


    private AppInfo getAppInfo(PackageManager pm, ResolveInfo resolveInfo) {
        return new AppInfo(resolveInfo.loadLabel(pm).toString(), resolveInfo.loadIcon(pm), resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);

    }

    private void parseAppInfo(List<AppInfo> appInfos) {
//		Log.i(TAG, "parseAppInfo");
        if (null == appInfos || appInfos.size() < 1) {
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

    }
}
