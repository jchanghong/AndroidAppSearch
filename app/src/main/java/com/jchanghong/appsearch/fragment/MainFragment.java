package com.jchanghong.appsearch.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.adapter.AppInfoAdapter;
import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.util.AppUtil;
import com.jchanghong.appsearch.util.ViewUtil;
import com.jchanghong.appsearch.view.T9TelephoneDialpadView;
import com.jchanghong.appsearch.view.T9TelephoneDialpadView.OnT9TelephoneDialpadView;

import java.util.List;

public class MainFragment extends Fragment implements
		OnT9TelephoneDialpadView, AppInfoHelper.OnAppInfoLoad {
	private static final String TAG="MainFragment";
	private GridView mT9SearchGv;
	private T9TelephoneDialpadView mT9TelephoneDialpadView;
	private AppInfoAdapter mAppInfoAdapter;

public 	MainFragment() {

	AppInfoHelper.mInstance.mOnAppInfoLoad = this;

	}

	@Override
	public void onResume() {
		super.onResume();
		mT9TelephoneDialpadView.mT9InputEt.setText("");
		refreshT9SearchGv();
	}

	protected void initData() {
		mAppInfoAdapter = new AppInfoAdapter(getActivity(),
				R.layout.app_info_grid_item, AppInfoHelper.mInstance
						.mT9SearchAppInfos);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initData();
		View view = inflater.inflate(R.layout.fragment_main, container,
				false);
		mT9SearchGv = (GridView) view.findViewById(R.id.t9_search_grid_view);
		mT9SearchGv.setAdapter(mAppInfoAdapter);
		mT9TelephoneDialpadView = (T9TelephoneDialpadView) view
				.findViewById(R.id.t9_telephone_dialpad_view);
		mT9TelephoneDialpadView.mOnT9TelephoneDialpadView = this;
		initListener();
		return view;
	}


	protected void initListener() {
		mT9SearchGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo appInfo=(AppInfo) parent.getItemAtPosition(position);
				AppUtil.startApp(getContext(), appInfo);
			}
		});
	}

	@Override
	public void onDialInputTextChanged(String curCharacter) {
			search(curCharacter);
			refreshT9SearchGv();

	}

	private void search(String keyword) {
		Log.i(TAG, "keyword=["+keyword+"]");
		String curCharacter;
		if (null == keyword) {
			curCharacter = keyword;
		} else {
			curCharacter = keyword.trim();
		}
		if (TextUtils.isEmpty(curCharacter)) {
			AppInfoHelper.mInstance.t9Search(null,false);
		} else {
			AppInfoHelper.mInstance.t9Search(curCharacter,false);
		}
	}

	private void refreshT9SearchGv() {
		BaseAdapter baseAdapter = (BaseAdapter) mT9SearchGv.getAdapter();
		Log.i(TAG, "getCount"+baseAdapter.getCount()+"");
			baseAdapter.notifyDataSetChanged();
				ViewUtil.showView(mT9SearchGv);
	}
	@Override
	public void onAppInfoLoadSuccess() {
	refreshT9SearchGv();
	}

	@Override
	public void onAppInfoLoadFailed() {

	}
}
