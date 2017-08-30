package com.jchanghong.appsearch.model;

import android.graphics.drawable.Drawable;

public class BaseAppInfo {
    public final String mLabel;
    public final Drawable mIcon;
    public final String mPackageName;
    public final String mName;

    BaseAppInfo(String label, Drawable icon, String packageName, String name) {
        mLabel = label;
        mIcon = icon;
        mPackageName = packageName;
        mName = name;
    }

}
