package com.jchanghong.appsearch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.util.ViewUtil;

import java.util.List;

public class AppInfoAdapter extends ArrayAdapter<AppInfo> {
    private final Context mContext;
    private final int mTextViewResourceId;
    private final List<AppInfo> mAppInfos;

    /**
     * 保护copy
     */
    public AppInfoAdapter(Context context,
                          List<AppInfo> appInfos) {
        super(context, R.layout.app_info_grid_item, appInfos);
        mContext = context;
        mTextViewResourceId = R.layout.app_info_grid_item;
        mAppInfos = appInfos;

    }


    public void setmAppInfos(List<AppInfo> newinfo) {
        mAppInfos.clear();
        for (AppInfo appInfo : newinfo) {
            mAppInfos.add(appInfo);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        AppInfo appInfo = getItem(position);
        if (null == convertView) {
            view = LayoutInflater.from(mContext).inflate(mTextViewResourceId,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.mIconIv = view
                    .findViewById(R.id.icon_image_view);
            viewHolder.mLabelTv = view
                    .findViewById(R.id.label_text_view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        assert appInfo != null;
        viewHolder.mIconIv.setBackground(appInfo.mIcon);
        switch (appInfo.mSearchByType) {
            case SearchByLabel:
                ViewUtil.showTextHighlight(viewHolder.mLabelTv, appInfo.mLabel,
                        appInfo.mMatchKeywords.toString());
                break;
            case SearchByTIME:
                viewHolder.mLabelTv.setText(appInfo.mLabel);
                break;
            default:
                break;
        }

        return view;
    }

    private class ViewHolder {
        ImageView mIconIv;
        TextView mLabelTv;
    }

}
