package com.jchanghong.appsearch.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.adapter.AppInfoAdapter;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;
import com.jchanghong.appsearch.util.AppUtil;
import com.jchanghong.appsearch.view.T9TelephoneDialpadView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity
        implements
        T9TelephoneDialpadView.OnT9TelephoneDialpadView,ServiceConnection, AppService.Ondata {
    private GridView mT9SearchGv;
    private AppInfoAdapter mAppInfoAdapter;
    private T9TelephoneDialpadView mT9TelephoneDialpadView;
    private AppService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mT9SearchGv = findViewById(R.id.t9_search_grid_view);
        mT9TelephoneDialpadView = findViewById(R.id.t9_telephone_dialpad_view);
        mAppInfoAdapter = new AppInfoAdapter(this, empty);
        mT9SearchGv.setAdapter(mAppInfoAdapter);
        mT9TelephoneDialpadView.mOnT9TelephoneDialpadView = this;
        Intent service = new Intent(this, AppService.class);
        startService(service);
        bindService(service, this, BIND_AUTO_CREATE);
    }

    private void initAfterServer() {
        if (service.recordHelper.mrecords == null) {
            service.recordHelper.startLoadAppStartRecord();
        }
        if (!service.appInfoHelper.loaded()) {
            service.appInfoHelper.startLoadAppInfo();
        }
        mT9SearchGv.setAdapter(mAppInfoAdapter);
        initListener();
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

    @Override
    public void onResume() {
        super.onResume();
        mT9TelephoneDialpadView.mT9InputEt.setText("");
        refreshT9SearchGv();
    }

  private   List<AppInfo> empty = new ArrayList<>();


    private void initListener() {
        mT9SearchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppInfo appInfo = (AppInfo) parent.getItemAtPosition(position);
                AppUtil.startApp(service, appInfo);
            }
        });
        mT9SearchGv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.pop_menu);
                AppInfo appInfo = (AppInfo) adapterView.getItemAtPosition(i);
                popupMenu.setOnMenuItemClickListener(new OnitemlongClick(appInfo));
                popupMenu.show();
                return true;
            }
        });
    }

    class OnitemlongClick implements PopupMenu.OnMenuItemClickListener {
        AppInfo info;
        public OnitemlongClick(AppInfo appInfo) {
            info = appInfo;
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.item_view) {
                AppUtil.viewApp(MainActivity.this, info);
                return true;
            }
            if (menuItem.getItemId() == R.id.item_delete) {
                AppUtil.uninstallApp(MainActivity.this, info);
                return true;
            }
            return false;
        }
    }
    private static String debug = MainActivity.class.getName();
    @Override
    public void onDialInputTextChanged(String curCharacter) {
//        if (AppStartRecordHelper.mInstance.mrecords == null) {
//            AppStartRecordHelper.mInstance.startLoadAppStartRecord();
//        }
//        if (AppInfoHelper.mInstance.loaded()) {
//            AppService.startService(getApplicationContext());
//        }
        if (service == null) {
            return;
        }
//        Log.i(debug, curCharacter+"     textchange");
//        initData();
        search(curCharacter);

    }

    private void search(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            service.appInfoHelper.t9Search(null);
            mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mBaseAllAppInfos.toArray());
            refreshT9SearchGv();
        } else {
            service.appInfoHelper.t9Search(keyword);
//            Log.i(debug, service.appInfoHelper.mT9SearchAppInfos.size()+"");
            mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mT9SearchAppInfos.toArray());
            refreshT9SearchGv();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void refreshT9SearchGv() {
        if (service == null) {
            return;
        }
        BaseAdapter baseAdapter = (BaseAdapter) mT9SearchGv.getAdapter();
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((AppService.MYBinder) iBinder).getserver();
        service.ondata = this;
        mAppInfoAdapter = new AppInfoAdapter(this,service.appInfoHelper.mT9SearchAppInfos);
        mT9SearchGv.setAdapter(mAppInfoAdapter);
        initAfterServer();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }
    @Override
    public void onAppinfo(List<AppInfo> list) {
//        mAppInfoAdapter = new AppInfoAdapter(this, service.appInfoList
//        );
//        mT9SearchGv.setAdapter(mAppInfoAdapter);
//        Log.d("changhong", list.toString());
//        service.appInfoHelper.t9Search(null);
        mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mBaseAllAppInfos.toArray());
        refreshT9SearchGv();
    }

    @Override
    public void onAppinfoChanged() {
        mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mBaseAllAppInfos.toArray());
        Log.i(debug, "onAppinfoChanged");
        refreshT9SearchGv();
    }

    @Override
    public void onrecode(List<AppStartRecord> list) {

    }
}
