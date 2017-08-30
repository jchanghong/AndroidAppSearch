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

import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.adapter.AppInfoAdapter;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;
import com.jchanghong.appsearch.util.AppUtil;
import com.jchanghong.appsearch.view.T9TelephoneDialpadView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity
        implements
        T9TelephoneDialpadView.OntextChangedlister,ServiceConnection, AppService.Ondata {
    private int initnumber = 2;//需要init的数量，当iniitnumber=0的时候就显示最后的数据，不然代表有异步任务没有完成
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
//        mAppInfoAdapter = new AppInfoAdapter(this, empty);
//        mT9SearchGv.setAdapter(mAppInfoAdapter);
        mT9TelephoneDialpadView.ontextChangedlister = this;
        Intent service = new Intent(this, AppService.class);
        startService(service);
        bindService(service, this, BIND_AUTO_CREATE);
    }

    //得到server以后
    private void initAfterServer() {
//        service.recordHelper.startLoadAppStartRecord();
//        if (!service.appInfoHelper.loaded()) {
//            service.appInfoHelper.startLoadAppInfo();
//        }
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
//        refreshT9SearchGv();
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

    @Override
    public void onInputTextChanged(String curCharacter) {
        if (service == null) {
            return;
        }
        search(curCharacter);
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

    private void search(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            service.appInfoHelper.t9Search(null);
            mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mBaseAllAppInfos);
            refreshT9SearchGv();
        } else {
            service.appInfoHelper.t9Search(keyword);
            mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mT9SearchAppInfos);
            refreshT9SearchGv();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
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
        mAppInfoAdapter = new AppInfoAdapter(this, new ArrayList<>(service.appInfoHelper.mBaseAllAppInfos));
        mT9SearchGv.setAdapter(mAppInfoAdapter);
        initAfterServer();

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }
    @Override
    public void onAppinfo(List<AppInfo> list) {
        initnumber--;
        if (initnumber == 0) {
            showinitview();
        }
    }
    //初始化完成后显示
    private void showinitview() {
        for (AppInfo appInfo : service.appInfoHelper.mBaseAllAppInfos) {
            AppStartRecord record = service.recordHelper.cache.get(appInfo.mPackageName);
            if (record != null) {
                appInfo.mstartTime = record.mStartTime;
            }
        }
        Collections.sort(service.appInfoHelper.mBaseAllAppInfos, AppInfo.mSortByTime);
        mAppInfoAdapter = new AppInfoAdapter(this, new ArrayList<>(service.appInfoHelper.mBaseAllAppInfos));
        mT9SearchGv.setAdapter(mAppInfoAdapter);

    }
    @Override
    public void onAppinfoChanged() {
        mAppInfoAdapter.setmAppInfos(service.appInfoHelper.mBaseAllAppInfos);
        Log.i(debug, "onAppinfoChanged");
        refreshT9SearchGv();
    }
    @Override
    public void onrecodeUpdate() {
        initnumber--;
        if (initnumber == 0) {
            showinitview();
        }
    }
}
