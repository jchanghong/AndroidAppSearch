package com.jchanghong.appsearch.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.jchanghong.appsearch.fragment.MainFragment;
import com.jchanghong.appsearch.service.XDesktopHelperService;

@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseSingleFragmentActivity implements
		OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected Fragment createFragment() {
		return  new MainFragment();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		return super.onKeyDown(keycode, e);
	}

	@Override
	public void onBackPressed() {
		runInBackgroud();
	}

	@Override
	public void onClick(View v) {
	}

	private void runInBackgroud() {
		moveTaskToBack(true);
		XDesktopHelperService.startService(getApplicationContext());

	}
}
