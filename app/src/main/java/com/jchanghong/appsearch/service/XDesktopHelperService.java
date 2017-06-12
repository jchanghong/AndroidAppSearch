package com.jchanghong.appsearch.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jchanghong.appsearch.helper.AppInfoHelper;

public class XDesktopHelperService extends Service{
	private static final String TAG="XDesktopHelperService";
	public static final String ACTION_X_DESKTOP_HELPER_SERVICE="com.jchanghong.appsearch.service.X_DESKTOP_HELPER_SERVICE";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		AppInfoHelper.mInstance.startLoadAppInfo();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		startService();
	}

	
	private void startService(){
		Intent intent=new Intent();
		intent.setAction(ACTION_X_DESKTOP_HELPER_SERVICE);
		startService(intent);
	}
	
	public static void startService(Context context){
		Intent intent=new Intent(context,XDesktopHelperService.class);
		intent.setAction(XDesktopHelperService.ACTION_X_DESKTOP_HELPER_SERVICE);
		context.startService(intent);

	}
	
	public static void stopService(Context context){
		Intent intent=new Intent(context,XDesktopHelperService.class);
		context.stopService(intent);
	}

}
