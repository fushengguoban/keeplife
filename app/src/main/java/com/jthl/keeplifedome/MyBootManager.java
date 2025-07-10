package com.jthl.keeplifedome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.jthl.keeplifedome.mode.MyLog;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class MyBootManager extends BroadcastReceiver {
    static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    ArrayList<String> app_list;
    MyLog log = new MyLog();
    Context mContext;
    PackageManager packageManager;
    public SharedPreferences shared;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        this.shared = context.getSharedPreferences("boot_app", 0);
        String action = intent.getAction();
        this.packageManager = this.mContext.getPackageManager();
        if (action.equals(BOOT_COMPLETED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0+ 必须使用 startForegroundService
                StartApp();
                context.startForegroundService(new Intent(context, AliveService.class));
            } else {
                StartApp();
                context.startService(new Intent(context, AliveService.class));
            }

        }
    }

    public void StartApp() {
        Intent launchIntentForPackage;
        String string = this.shared.getString("appPackageName", " ");
        if (string.equals(" ") || (launchIntentForPackage = this.packageManager.getLaunchIntentForPackage(string)) == null) {
            return;
        }
        this.mContext.startActivity(launchIntentForPackage);
    }
}
