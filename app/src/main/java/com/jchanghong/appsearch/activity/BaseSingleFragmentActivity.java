package com.jchanghong.appsearch.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import com.jchanghong.appsearch.R;

public abstract class BaseSingleFragmentActivity extends Activity {
	protected Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// load fragment
		loadFragment();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	protected abstract Fragment createFragment();
	



	private void loadFragment() {
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);
			if (null == fragment) {
				fragment = createFragment();
				fm.beginTransaction().add(R.id.fragment_container, fragment)
						.commit();
			}

	}
}
