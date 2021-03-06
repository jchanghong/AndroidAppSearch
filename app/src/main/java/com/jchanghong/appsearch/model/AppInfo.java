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
            return rhs.mMatchLength - lhs.mMatchLength;
        }
    };
    //    private static final Comparator<Object> mChineseComparator = Collator.getInstance(Locale.CHINA);
    public static final Comparator<AppInfo> mSortByTime = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {
            if (lhs.mstartTime == rhs.mstartTime) {
                return 0;
            }
            return (rhs.mstartTime - lhs.mstartTime) > 0 ? 1 : -1;
        }
    };

    public final String mkey; // as the sort key word
    public final PinyinSearchUnit mLabelPinyinSearchUnit;// save the mLabel converted to Pinyin characters.
    public final StringBuilder mMatchKeywords;// Used to save the type of Match Keywords.(label)
    public SearchByType mSearchByType; // Used to save the type of search
    public int mMatchStartIndex = -1;        //the match start  position of mMatchKeywords in original string(label).
    public int mMatchLength = 0;            //the match length of mMatchKeywords in original string(name or phoneNumber).
    public long mstartTime = 0L;

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

    @Override
    public String toString() {
        return mLabel + mPackageName;
    }

    public enum SearchByType {
        SearchByTIME, SearchByLabel,
    }
}
