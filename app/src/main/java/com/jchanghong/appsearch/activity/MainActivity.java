package com.jchanghong.appsearch.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.adapter.AppInfoAdapter;
import com.jchanghong.appsearch.helper.AppInfoHelper;
import com.jchanghong.appsearch.helper.AppStartRecordHelper;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.service.XDesktopHelperService;
import com.jchanghong.appsearch.util.AppUtil;
import com.jchanghong.appsearch.view.T9TelephoneDialpadView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity
        implements
        T9TelephoneDialpadView.OnT9TelephoneDialpadView, AppInfoHelper.OnAppInfoLoad {
    static public AppInfoAdapter mAppInfoAdapter;
    private GridView mT9SearchGv;
    private T9TelephoneDialpadView mT9TelephoneDialpadView;

    public MainActivity() {

        AppInfoHelper.mInstance.mOnAppInfoLoad = this;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("onnewintent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AppStartRecordHelper.mInstance.mrecords == null) {
            AppStartRecordHelper.mInstance.startLoadAppStartRecord();
        }
        if (!AppInfoHelper.mInstance.loaded()) {
            XDesktopHelperService.startService(getApplicationContext());
        }
        initData();
        mT9SearchGv = (GridView) findViewById(R.id.t9_search_grid_view);
        mT9SearchGv.setAdapter(mAppInfoAdapter);
        mT9TelephoneDialpadView = (T9TelephoneDialpadView)
                findViewById(R.id.t9_telephone_dialpad_view);
        mT9TelephoneDialpadView.mOnT9TelephoneDialpadView = this;
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

    private void initData() {
        mAppInfoAdapter = new AppInfoAdapter(this,
                R.layout.app_info_grid_item, AppInfoHelper.mInstance
                .mT9SearchAppInfos);
    }


    private void initListener() {
        mT9SearchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AppInfo appInfo = (AppInfo) parent.getItemAtPosition(position);
                AppUtil.startApp(MainActivity.this, appInfo);
            }
        });
    }

    @Override
    public void onDialInputTextChanged(String curCharacter) {
        if (AppStartRecordHelper.mInstance.mrecords == null) {
            AppStartRecordHelper.mInstance.startLoadAppStartRecord();
        }
        if (!AppInfoHelper.mInstance.loaded()) {
            XDesktopHelperService.startService(getApplicationContext());
        }
        initData();
        search(curCharacter);
        refreshT9SearchGv();

    }

    private void search(String keyword) {
        String curCharacter;
        if (null == keyword) {
            curCharacter = keyword;
        } else {
            curCharacter = keyword.trim();
        }
        if (TextUtils.isEmpty(curCharacter)) {
            AppInfoHelper.mInstance.t9Search(null, false);
        } else {
            AppInfoHelper.mInstance.t9Search(curCharacter, false);
        }
    }

    private void refreshT9SearchGv() {
        BaseAdapter baseAdapter = (BaseAdapter) mT9SearchGv.getAdapter();
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppInfoLoadSuccess() {
        search("");
        refreshT9SearchGv();
    }

    @Override
    public void onAppInfoLoadFailed() {

    }

}
