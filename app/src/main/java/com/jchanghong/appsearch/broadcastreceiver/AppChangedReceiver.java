package com.jchanghong.appsearch.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.service.XDesktopHelperService;

public class AppChangedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			String packageName = intent.getData().getSchemeSpecificPart();
			if(false==AppInfoHelper.mInstance.isAppExist(packageName)){
			    AppInfoHelper.mInstance.add(packageName);
			}
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
			XDesktopHelperService.startService(context);
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			String packageName = intent.getData().getSchemeSpecificPart();
			AppInfoHelper.mInstance.remove(packageName);
		}
	}
	


}
