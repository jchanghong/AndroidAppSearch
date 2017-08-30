package com.jchanghong.appsearch.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.jchanghong.appsearch.R;
import com.jchanghong.appsearch.model.AppInfo;
import com.jchanghong.appsearch.model.AppStartRecord;
import com.jchanghong.appsearch.service.AppService;

public class AppUtil {

    /**
     * @param context
     * @param packageName
     * @param cls
     * @return
     */

    private static boolean startApp(Context context, String packageName, String cls) {
        boolean startAppSuccess = false;

        if ((null == context) || TextUtils.isEmpty(packageName)) {
            return false;
        }
        ComponentName componet = new ComponentName(packageName, cls);
        Intent intent = createLaunchIntent(componet);
        if (context.getPackageManager().getLaunchIntentForPackage(
                packageName) != null) {
            context.startActivity(intent);
            startAppSuccess = true;
        } else {
            System.out.println("app not found");
        }

        return startAppSuccess;
    }

    private static Intent createLaunchIntent(ComponentName componentName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    /**
     * start app via appinfo入口
     *
     * @param context
     * @param appInfo
     */
    public static void startApp(Context context, AppInfo appInfo) {
        boolean startAppSuccess = false;
        if (!appInfo.mPackageName.equals(context.getPackageName())) {
            startAppSuccess = AppUtil.startApp(context, appInfo.mPackageName,
                    appInfo.mName);
            if (!startAppSuccess) {
                Toast.makeText(context, R.string.app_can_not_be_launched_directly,
                        Toast.LENGTH_SHORT).show();
            } else {
                long startTimeMs = System.currentTimeMillis();
                AppStartRecord appStartRecord = new AppStartRecord(appInfo.mPackageName,
                        startTimeMs);
                AppService service = (AppService) context;
                service.recordHelper.insert(appStartRecord);
                appInfo.mstartTime = startTimeMs;
            }
        } else {
            Toast.makeText(context, R.string.the_app_has_been_launched, Toast.LENGTH_SHORT)
                    .show();
        }
        return;
    }

    /**
     * whether app can Launch the main activity. Return true when can Launch,otherwise return false.
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean appCanLaunchTheMainActivity(Context context, String packageName) {
        boolean canLaunchTheMainActivity = false;
        do {
            if ((null == context) || TextUtils.isEmpty(packageName)) {
                break;
            }

            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            canLaunchTheMainActivity = null != intent;
        } while (false);

        return canLaunchTheMainActivity;
    }

    /**
     * uninstall app via appInfo
     *
     * @param context
     * @param appInfo
     */
    public static void uninstallApp(Context context, AppInfo appInfo) {
        if (!appInfo.mPackageName.equals(context.getPackageName())) {
            Uri packageUri = Uri.parse("package:" + appInfo.mPackageName);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(packageUri);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.can_not_to_uninstall_yourself, Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public static void viewApp(Context context, AppInfo appInfo) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + appInfo.mPackageName));
        intent.putExtra("cmp", "com.android.settings/.applications.InstalledAppDetails");
        intent.addCategory("android.intent.category.DEFAULT");
        context.startActivity(intent);
    }
}
