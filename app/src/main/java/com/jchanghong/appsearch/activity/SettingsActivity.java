package com.jchanghong.appsearch.activity;

import android.support.v4.app.Fragment;

import com.jchanghong.appsearch.fragment.SettingsFragment;

public class SettingsActivity extends BaseSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new SettingsFragment();
	}

	@Override
	protected boolean isRealTimeLoadFragment() {
		
		return false;
	}

}
