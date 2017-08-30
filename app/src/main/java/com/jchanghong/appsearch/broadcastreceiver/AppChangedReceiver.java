package com.jchanghong.appsearch.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jchanghong.appsearch.service.AppService;

public class AppChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppService.startService(context, intent);
    }

}
