package com.jchanghong.appsearch.application;

import android.app.Application;
import android.content.Context;


public class XDesktopHelperApplication extends Application {
	public static Context mcontext;
	@Override
	public void onCreate() {
		mcontext = this;

	}
}
