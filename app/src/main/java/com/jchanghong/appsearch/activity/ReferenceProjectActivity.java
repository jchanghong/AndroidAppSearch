package com.jchanghong.appsearch.activity;

import android.support.v4.app.Fragment;

import com.jchanghong.appsearch.fragment.ReferenceProjectFragment;

public class ReferenceProjectActivity extends BaseSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new ReferenceProjectFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {
		// TODO Auto-generated method stub
		return false;
	}

}
