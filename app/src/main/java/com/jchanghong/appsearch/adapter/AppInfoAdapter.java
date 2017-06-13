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

    public AppInfoAdapter(Context context, int textViewResourceId,
                          List<AppInfo> appInfos) {
        super(context, textViewResourceId, appInfos);
        mContext = context;
        mTextViewResourceId = textViewResourceId;
        mAppInfos = appInfos;

    }

    public void setmAppInfos(Object[] appInfos) {
        mAppInfos.clear();
        for (Object info : appInfos) {
            mAppInfos.add((AppInfo) info);
        }
//		notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        AppInfo appInfo = getItem(position);
        if (null == convertView) {
            view = LayoutInflater.from(mContext).inflate(mTextViewResourceId,
                    null);
            viewHolder = new ViewHolder();
//			viewHolder.mAlphabetTv = (TextView) view
//					.findViewById(R.id.alphabet_text_view);
            viewHolder.mIconIv = (ImageView) view
                    .findViewById(R.id.icon_image_view);
            viewHolder.mLabelTv = (TextView) view
                    .findViewById(R.id.label_text_view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mIconIv.setBackground(appInfo.getIcon());
        switch (appInfo.getSearchByType()) {
            case SearchByLabel:
                ViewUtil.showTextHighlight(viewHolder.mLabelTv, appInfo.getLabel(),
                        appInfo.getMatchKeywords().toString());

                break;
            case SearchByNull:
                ViewUtil.showTextNormal(viewHolder.mLabelTv, appInfo.getLabel());
                break;
            default:
                break;
        }

        return view;
    }

    private class ViewHolder {
        TextView mAlphabetTv;
        ImageView mIconIv;
        TextView mLabelTv;
    }

}
