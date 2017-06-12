package com.jchanghong.appsearch.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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

public class MainFragment extends Fragment implements
		OnT9TelephoneDialpadView{
	private static final String TAG="MainFragment";
	private GridView mT9SearchGv;
	private T9TelephoneDialpadView mT9TelephoneDialpadView;
	private AppInfoAdapter mAppInfoAdapter;

public 	MainFragment() {


	}

	@Override
	public void onResume() {
		super.onResume();
		mT9TelephoneDialpadView.getT9InputEt().setText("");
		refreshView();
	}

	protected void initData() {
		mAppInfoAdapter = new AppInfoAdapter(getActivity(),
				R.layout.app_info_grid_item, AppInfoHelper.getInstance()
						.getT9SearchAppInfos());
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
		mT9TelephoneDialpadView.setOnT9TelephoneDialpadView(this);
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

		mT9SearchGv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfo appInfo=(AppInfo) parent.getItemAtPosition(position);

				return true;
			}
		});
		
		mT9SearchGv.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {  
                    case OnScrollListener.SCROLL_STATE_IDLE: //  
                        Log.i(TAG, "SCROLL_STATE_IDLE");
                        // mBusy = false;  
                        break;  
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:  
                        Log.i(TAG, "SCROLL_STATE_TOUCH_SCROLL");

                        // mBusy = true;  
                        
                        break;  
                    case OnScrollListener.SCROLL_STATE_FLING:  
                        Log.i(TAG, "SCROLL_STATE_FLING");
                
                    
                        hideKeyboard();
          
                        break;  
                    }  
                
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                // TODO Auto-generated method stub
                
            }
        });

	}

	/* start: OnT9TelephoneDialpadView */
	@Override
	public void onAddDialCharacter(String addCharacter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteDialCharacter(String deleteCharacter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialInputTextChanged(String curCharacter) {

			search(curCharacter);
			refreshView();

	}




	public void refreshView() {
		refreshT9SearchGv();
		refreshT9TelephoneDialpadView();
	}

	public void search(){
		if(null==mT9TelephoneDialpadView){
			return;
		}
		
		search(mT9TelephoneDialpadView.getT9Input());
	}

	private void search(String keyword) {
		search(keyword,false);
	}

	private void voiceTextSearch(String keyword){
		search(keyword,true);
	}

	private void search(String keyword, boolean voiceSearch) {
		Log.i(TAG, "keyword=["+keyword+"]");
		String curCharacter;
		if (null == keyword) {
			curCharacter = keyword;
		} else {
			curCharacter = keyword.trim();
		}

		if (TextUtils.isEmpty(curCharacter)) {
			AppInfoHelper.getInstance().t9Search(null,voiceSearch);
		} else {
			AppInfoHelper.getInstance().t9Search(curCharacter,voiceSearch);
		}
	}

	private void hideKeyboard() {
//		ViewUtil.hideView(mT9TelephoneDialpadView);
//		mKeyboardSwitchIv
//				.setBackgroundResource(R.drawable.keyboard_show_selector);
	}

	private void showKeyboard() {
		ViewUtil.showView(mT9TelephoneDialpadView);
//		mKeyboardSwitchIv
//				.setBackgroundResource(R.drawable.keyboard_hide_selector);
	}

	private void refreshT9SearchGv() {
		if (null == mT9SearchGv) {
			return;
		}

		BaseAdapter baseAdapter = (BaseAdapter) mT9SearchGv.getAdapter();
		Log.i(TAG, "getCount"+baseAdapter.getCount()+"");
		if (null != baseAdapter) {
			baseAdapter.notifyDataSetChanged();
			if (baseAdapter.getCount() > 0) {
				ViewUtil.showView(mT9SearchGv);
			} else {
				ViewUtil.hideView(mT9SearchGv);
			}
		}
	}
	
	private void refreshT9TelephoneDialpadView(){
		if(null==mT9TelephoneDialpadView){
			return;
		}
		
		mT9TelephoneDialpadView.refreshView();
	}
	

	private void voiceStartApp(){
		if(1==mAppInfoAdapter.getCount()){
			AppInfo appInfo=mAppInfoAdapter.getItem(0);
			AppUtil.startApp(getContext(), appInfo);
		}
	}

}
