package component.shine.com.basemoudle.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.List;

/**
 * 开机广播 运行本程序 android root 定制设备使用
 * 程序需声明
 *         <receiver
 *             android:name="com.anxiangzhiyun.carlock.receiver.BootCompleteReceiver"
 *             android:label="@string/app_name">
 *             <intent-filter>
 *                 <action android:name="android.intent.action.BOOT_COMPLETED" />
 *             </intent-filter>
 *         </receiver>
 *
 *
 *         所需权限为
 *             <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 *
 *             入口的activity
 *             <intent-filter>
 *                 <action android:name="android.intent.action.MAIN" />
 *                 <action android:name="android.intent.action.MyStart" />
 *                 <category android:name="android.intent.category.HOME" />
 *                 <category android:name="android.intent.category.DEFAULT" />
 *                 <category android:name="android.intent.category.LAUNCHER" />
 *             </intent-filter>
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    public static final String TAG = "BootCompleteReceiver";

    public BootCompleteReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isAppOpen(context)) {
            Intent upgradeIntent = new Intent("android.intent.action.MyStart");
            upgradeIntent.putExtra("message", "要启动的程序");
            upgradeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 已有就把原有的Activity提升到栈顶,否则创建新的Activity
            context.startActivity(upgradeIntent);
        }

    }


    public static boolean isAppOpen(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals("component.shine.com.basemoudle")) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i(TAG, "后台进程为:" + appProcess.processName);

                    return false;
                } else {
                    Log.i(TAG, "前台进程为" + appProcess.processName);

//                    ActivityManager am = (ActivityManager) CarLockApplication.getCarLockApplication().getSystemService(Context.ACTIVITY_SERVICE);
//                    //获得task列表
//                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//                    if (taskInfo.size() > 0) {
//                        String topActivityName = taskInfo.get(0).topActivity.getClassName();
//                        Log.i(TAG, "CURRENT Activity ::" + topActivityName);
//                        if ("component.shine.com.basemoudle.MainActivity".equals(topActivityName)) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }


                }
            }
        }
        Log.i(TAG, "程序未运行");
        return false;
    }

}
