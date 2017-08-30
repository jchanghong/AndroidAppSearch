package com.jchanghong.appsearch.model;

import android.graphics.drawable.Drawable;

import com.pinyinsearch.model.PinyinSearchUnit;

import java.util.Comparator;

public class AppInfo extends BaseAppInfo {
    public static final Comparator<AppInfo> mSortBySearch = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {
            int compareMatchStartIndex = (lhs.mMatchStartIndex - rhs.mMatchStartIndex);
            if (compareMatchStartIndex != 0) {
                return compareMatchStartIndex;
            }
            int compareMatchLength = rhs.mMatchLength - lhs.mMatchLength;
            return compareMatchLength;
        }
    };
//    private static final Comparator<Object> mChineseComparator = Collator.getInstance(Locale.CHINA);
    public static final Comparator<AppInfo> mSortByTime = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {
            return (int) (rhs.mstartTime - lhs.mstartTime);
        }
    };

    public String mkey; // as the sort key word
    public PinyinSearchUnit mLabelPinyinSearchUnit;// save the mLabel converted to Pinyin characters.
    public SearchByType mSearchByType; // Used to save the type of search
    public StringBuilder mMatchKeywords;// Used to save the type of Match Keywords.(label)
    public int mMatchStartIndex=-1;        //the match start  position of mMatchKeywords in original string(label).
    public int mMatchLength=0;            //the match length of mMatchKeywords in original string(name or phoneNumber).
    public long mstartTime = 0l;

    public AppInfo(String label, Drawable icon, String packageName, String name) {
        super(label, icon, packageName, name);
        mkey = mPackageName + mName;
        mLabelPinyinSearchUnit = new PinyinSearchUnit(label);
        mSearchByType = SearchByType.SearchByTIME;
        mMatchKeywords = new StringBuilder();
    }
    public void setMatchKeywords(String matchKeywords) {
        mMatchKeywords.delete(0, mMatchKeywords.length());
        mMatchKeywords.append(matchKeywords);
    }
    public void clearMatchKeywords() {
        mMatchKeywords.delete(0, mMatchKeywords.length());
    }
    public enum SearchByType {
        SearchByTIME, SearchByLabel,
    }
}
