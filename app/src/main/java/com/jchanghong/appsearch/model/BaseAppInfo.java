package com.jchanghong.appsearch.model;

import android.graphics.drawable.Drawable;

public class BaseAppInfo  {
    public String mLabel;
    public Drawable mIcon;
    public String mPackageName;
    public String mName;
    BaseAppInfo(String label, Drawable icon, String packageName, String name) {
        mLabel = label;
        mIcon = icon;
        mPackageName = packageName;
        mName = name;
    }

}
