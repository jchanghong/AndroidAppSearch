package com.jchanghong.appsearch.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.jchanghong.appsearch.application.XDesktopHelperApplication;
import com.jchanghong.appsearch.fragment.MainFragment;
import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.helper.AppStartRecordHelper;
import com.jchanghong.appsearch.service.XDesktopHelperService;

@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseSingleFragmentActivity
		 {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (AppStartRecordHelper.mInstance.mrecords == null) {
			AppStartRecordHelper.mInstance.startLoadAppStartRecord();
		}
		if (!AppInfoHelper.mInstance.loaded()) {
			XDesktopHelperService.startService(getApplicationContext());
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		return  new MainFragment();
	}

	@Override
	public void onBackPressed() {
		runInBackgroud();
	}
			 @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
			         if (keyCode == KeyEvent.KEYCODE_BACK) {
				                  runInBackgroud();
				             return true;
				          }
			         return super.onKeyDown(keyCode, event);
			      }
	private void runInBackgroud() {
		moveTaskToBack(true);
	}
}
