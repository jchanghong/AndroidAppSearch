package com.jchanghong.appsearch.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import com.jchanghong.appsearch.activity.MainActivity;
import com.jchanghong.appsearch.database.AppStartRecordDataBaseHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppInfo.SearchByType;
import com.jchanghong.appsearch.model.Constant;
import com.jchanghong.appsearch.model.LoadStatus;
import com.jchanghong.appsearch.util.AppCommonWeightsUtil;
import com.jchanghong.appsearch.util.AppUtil;
import com.jchanghong.appsearch.util.StringUtil;
import com.pinyinsearch.model.PinyinSearchUnit;
import com.pinyinsearch.util.PinyinUtil;
import com.pinyinsearch.util.QwertyUtil;
import com.pinyinsearch.util.T9Util;

import java.util.*;

public class AppInfoHelper {
    public static final AppInfoHelper mInstance = new AppInfoHelper();
    private static final Character THE_LAST_ALPHABET = Constant.z;
    public Context mContext;
    public List<AppInfo> mT9SearchAppInfos;
    public OnAppInfoLoad mOnAppInfoLoad;
//    private AppType mCurrentAppType;
    private List<AppInfo> mBaseAllAppInfos;
    private HashMap<String, AppInfo> mBaseAllAppInfosHashMap = null;
    private List<AppInfo> mQwertySearchAppInfos;
    private StringBuffer mFirstNoQwertySearchResultInput = null;
    private StringBuffer mFirstNoT9SearchResultInput = null;
    private AsyncTask<Object, Object, List<AppInfo>> mLoadAppInfoTask = null;
    private boolean mAppInfoChanged = true;

    private AppInfoHelper() {
        initAppInfoHelper();
    }

    private void initAppInfoHelper() {
//        mCurrentAppType = AppType.ALL_APP;
        clearAppInfoData();
    }

    public boolean startLoadAppInfo() {
        if (isAppInfoLoading()) {
            return false;
        }
        if (!mAppInfoChanged) {
            return false;
        }
        clearAppInfoData();
        mLoadAppInfoTask = new AsyncTask<Object, Object, List<AppInfo>>() {

            @Override
            protected List<AppInfo> doInBackground(Object... params) {
                // TODO Auto-generated method stub
                return loadAppInfo(mContext);
            }

            @Override
            protected void onPostExecute(List<AppInfo> result) {
                super.onPostExecute(result);
                parseAppInfo(result);
                //setAppInfoChanged(false);
                mLoadAppInfoTask = null;
            }

        }.execute();
        mAppInfoChanged = false;
        return true;

    }

    @SuppressLint("DefaultLocale")
    private List<AppInfo> loadAppInfo(Context context) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        List<AppInfo> kanjiStartAppInfos = new ArrayList<>();
        List<AppInfo> nonKanjiStartAppInfos = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(it, 0);

        for (ResolveInfo ri : resolveInfos) {
            boolean canLaunchTheMainActivity = AppUtil.appCanLaunchTheMainActivity(mContext, ri.activityInfo.packageName);
            if (canLaunchTheMainActivity) {
                AppInfo appInfo = getAppInfo(pm, ri);
                if (null == appInfo) {
                    continue;
                }

                if (TextUtils.isEmpty(appInfo.getLabel())) {
                    continue;
                }
                appInfo.getLabelPinyinSearchUnit().setBaseData(appInfo.getLabel());
                PinyinUtil.parse(appInfo.getLabelPinyinSearchUnit());
                String sortKey = PinyinUtil.getSortKey(appInfo.getLabelPinyinSearchUnit()).toUpperCase();
                appInfo.setSortKey(StringUtil.praseSortKey(sortKey));
                boolean isKanji = PinyinUtil.isKanji(appInfo.getLabel().charAt(0));
                if (isKanji) {
                    kanjiStartAppInfos.add(appInfo);
                } else {
                    nonKanjiStartAppInfos.add(appInfo);
                }

            }
        }
        Collections.sort(kanjiStartAppInfos, AppInfo.mSortBySortKeyAsc);
        Collections.sort(nonKanjiStartAppInfos, AppInfo.mSortBySortKeyAsc);

        appInfos.addAll(kanjiStartAppInfos);

		/*Start: merge nonKanjiStartAppInfos and kanjiStartAppInfos*/
        int lastIndex = 0;
        boolean shouldBeAdd = false;
        for (AppInfo nonKanjiStartAppInfo : nonKanjiStartAppInfos) {
            String nonKanfirstLetter = PinyinUtil.getFirstLetter(nonKanjiStartAppInfo.getLabelPinyinSearchUnit());
            //Log.i(TAG, "nonKanfirstLetter=["+nonKanfirstLetter+"]["+nonKanjiStartAppInfos.get(i).getLabel()+"]["+Integer.valueOf(nonKanjiStartAppInfos.get(i).getLabel().charAt(0))+"]");
            int j;
            for (j = lastIndex; j < appInfos.size(); j++) {
                String firstLetter = PinyinUtil.getFirstLetter(appInfos.get(j).getLabelPinyinSearchUnit());
                lastIndex++;
                if (nonKanfirstLetter.charAt(0) < firstLetter.charAt(0) || nonKanfirstLetter.charAt(0) > THE_LAST_ALPHABET) {
                    shouldBeAdd = true;
                    break;
                } else {
                    shouldBeAdd = false;
                }
            }

            if (lastIndex >= appInfos.size()) {
                lastIndex++;
                shouldBeAdd = true;
                //Log.i(TAG, "lastIndex="+lastIndex);
            }

            if (shouldBeAdd) {
                appInfos.add(j, nonKanjiStartAppInfo);
                shouldBeAdd = false;
            }
        }
        return appInfos;
    }

    public void t9Search(String search, boolean voiceSearch) {
        List<AppInfo> baseAppInfos = getBaseAppInfo();
        if (null != mT9SearchAppInfos) {
            mT9SearchAppInfos.clear();
        } else {
            mT9SearchAppInfos = new LinkedList<>();
        }

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
//        int baseAppInfosCount = baseAppInfos.size();
        for (AppInfo baseAppInfo : baseAppInfos) {
            PinyinSearchUnit labelPinyinSearchUnit = baseAppInfo.getLabelPinyinSearchUnit();

            boolean match;
            if (voiceSearch) {
                match = QwertyUtil.match(labelPinyinSearchUnit, search);
            } else {
                match = T9Util.match(labelPinyinSearchUnit, search);
            }
            if (match) {// search by LabelPinyinUnits;
                AppInfo appInfo = baseAppInfo;
                appInfo.setSearchByType(SearchByType.SearchByLabel);
                appInfo.setMatchKeywords(labelPinyinSearchUnit.getMatchKeyWord().toString());
                appInfo.setMatchStartIndex(appInfo.getLabel().indexOf(appInfo.getMatchKeywords().toString()));
                appInfo.setMatchLength(appInfo.getMatchKeywords().length());
                mT9SearchAppInfos.add(appInfo);
            }
        }

        if (mT9SearchAppInfos.size() <= 0) {
            if (mFirstNoT9SearchResultInput.length() <= 0) {
                mFirstNoT9SearchResultInput.append(search);
            }
        } else {
            if (TextUtils.isEmpty(search)) {
                Collections.sort(mT9SearchAppInfos, AppInfo.mSortByDefault);
            } else {
                Collections.sort(mT9SearchAppInfos, AppInfo.mSortBySearch);
            }

        }
    }

    //最新记录
    private void searchEmpty(List<AppInfo> baseAppInfos) {
        for (AppInfo ai : baseAppInfos) {
            ai.setSearchByType(SearchByType.SearchByNull);
            ai.clearMatchKeywords();
            ai.setMatchStartIndex(-1);
            ai.setMatchLength(0);
        }
        if (AppStartRecordHelper.mInstance.mrecords == null) {
            mT9SearchAppInfos.addAll(baseAppInfos);
            mFirstNoT9SearchResultInput.delete(0, mFirstNoT9SearchResultInput.length());
            Collections.sort(mT9SearchAppInfos, AppInfo.mSortByDefault);
            return;
        }
        LinkedHashSet set = new LinkedHashSet();
        LinkedList<String> mrecords = AppStartRecordHelper.mInstance.mrecords;
        for (String mrecord : mrecords) {
            AppInfo o = mBaseAllAppInfosHashMap.get(mrecord);
            set.add(o);
        }
        set.addAll(mBaseAllAppInfos);
        MainActivity.mAppInfoAdapter.setmAppInfos(set.toArray());
    }

    public boolean isAppExist(String packageName) {
        boolean appExist = false;
            if (TextUtils.isEmpty(packageName)) {
                appExist = false;
                return appExist;
            }

            for (AppInfo ai : mBaseAllAppInfos) {
                if (ai.getPackageName().equals(packageName)) {
                    appExist = true;
                    break;
                }
            }
            /*if(mBaseAllAppInfosHashMap.containsKey(packageName+name)){
			    appExist=true;
			    break;
			}*/

        return appExist;
    }

    public boolean add(String packageName) {
        boolean addSuccess = false;
            if (TextUtils.isEmpty(packageName)) {
                addSuccess = false;
                return addSuccess;
            }


            boolean canLaunchTheMainActivity = AppUtil.appCanLaunchTheMainActivity(mContext, packageName);

            if (canLaunchTheMainActivity) {
                PackageManager pm = mContext.getPackageManager();
                Intent intent = new Intent();
                intent.setPackage(packageName);
                ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

                if (null != resolveInfo) {
                    AppInfo appInfo = getAppInfo(pm, resolveInfo);
                    if (TextUtils.isEmpty(appInfo.getLabel())) {
                        addSuccess = false;
                        return addSuccess;
                    }

                    appInfo.getLabelPinyinSearchUnit().setBaseData(appInfo.getLabel());
                    PinyinUtil.parse(appInfo.getLabelPinyinSearchUnit());
                    String sortKey = PinyinUtil.getSortKey(appInfo.getLabelPinyinSearchUnit()).toUpperCase();
                    appInfo.setSortKey(StringUtil.praseSortKey(sortKey));

                    mBaseAllAppInfosHashMap.put(appInfo.getKey(), appInfo);
                    mBaseAllAppInfos.add(appInfo);
                    Collections.sort(mBaseAllAppInfos, AppInfo.mSortByDefault);
                    addSuccess = true;
                }
            }

        return addSuccess;
    }

    public boolean resetSequence(AppInfo appInfo) {
        boolean resetSequenceSuccess = false;
            if (null == appInfo) {
                resetSequenceSuccess = false;
                return resetSequenceSuccess;

            }
            if (TextUtils.isEmpty(appInfo.getKey())) {
                resetSequenceSuccess = false;
                return resetSequenceSuccess;
            }

            AppStartRecordDataBaseHelper.mInstance.delete(appInfo.getKey());

            if (mBaseAllAppInfosHashMap.containsKey(appInfo.getKey())) {
                mBaseAllAppInfosHashMap.get(appInfo.getKey()).setCommonWeights(AppCommonWeightsUtil.COMMON_WEIGHTS_DEFAULT);
                Collections.sort(mBaseAllAppInfos, AppInfo.mSortByDefault);
            }

            resetSequenceSuccess = true;

        return resetSequenceSuccess;
    }

    public boolean remove(String packageName) {
        boolean removeSuccess;

            if (TextUtils.isEmpty(packageName)) {
                removeSuccess = false;
                return removeSuccess;
            }


            PackageManager pm = mContext.getPackageManager();
            Intent intent = new Intent();
            intent.setPackage(packageName);
            ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
            if (null != resolveInfo) {
                AppInfo appInfo = getAppInfo(pm, resolveInfo);
                if (null != appInfo) {
                    AppStartRecordDataBaseHelper.mInstance.delete(appInfo.getKey());
                    mBaseAllAppInfosHashMap.remove(appInfo.getKey());
                }
            }

            for (int i = 0; i < mBaseAllAppInfos.size(); i++) {
                if (mBaseAllAppInfos.get(i).getPackageName().equals(packageName)) {
                    mBaseAllAppInfos.remove(i);
                    break;
                }
            }
            removeSuccess = true;
        return removeSuccess;
    }


    public boolean loaded() {
        return mBaseAllAppInfos != null & mBaseAllAppInfos.size() > 0;
    }

    private void clearAppInfoData() {

        if (null == mBaseAllAppInfos) {
            mBaseAllAppInfos = new ArrayList<>();
        } else {
            mBaseAllAppInfos.clear();
        }

        if (null == mBaseAllAppInfosHashMap) {
            mBaseAllAppInfosHashMap = new HashMap<>();
        } else {
            mBaseAllAppInfosHashMap.clear();
        }

        if (null == mQwertySearchAppInfos) {
            mQwertySearchAppInfos = new ArrayList<>();
        } else {
            mQwertySearchAppInfos.clear();
        }

        if (null == mT9SearchAppInfos) {
            mT9SearchAppInfos = new LinkedList<>();
        } else {
            mT9SearchAppInfos.clear();
        }

        if (null == mFirstNoQwertySearchResultInput) {
            mFirstNoQwertySearchResultInput = new StringBuffer();
        } else {
            mFirstNoQwertySearchResultInput.delete(0, mFirstNoQwertySearchResultInput.length());
        }

        if (null == mFirstNoT9SearchResultInput) {
            mFirstNoT9SearchResultInput = new StringBuffer();
        } else {
            mFirstNoT9SearchResultInput.delete(0, mFirstNoT9SearchResultInput.length());
        }

    }

    private AppInfo getAppInfo(PackageManager pm, ResolveInfo resolveInfo) {
        if ((null == pm) || (null == resolveInfo)) {
            return null;
        }
        AppInfo appInfo = new AppInfo();
        appInfo.setIcon(resolveInfo.loadIcon(pm));
        appInfo.setLabel(resolveInfo.loadLabel(pm).toString());

        appInfo.setPackageName(resolveInfo.activityInfo.packageName);
        appInfo.setName(resolveInfo.activityInfo.name);
        return appInfo;

    }

    private boolean isAppInfoLoading() {
        return ((null != mLoadAppInfoTask) && (mLoadAppInfoTask.getStatus() == Status.RUNNING));
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
            mBaseAllAppInfosHashMap.put(ai.getKey(), ai);
        }

//		Log.i(TAG, "after appInfos.size()"+ appInfos.size());
        if (null != mOnAppInfoLoad) {
            mOnAppInfoLoad.onAppInfoLoadSuccess();
        }

    }

    private List<AppInfo> getBaseAppInfo() {
        return mBaseAllAppInfos;
    }


    public interface OnAppInfoLoad {
        void onAppInfoLoadSuccess();

        void onAppInfoLoadFailed();
    }
}
