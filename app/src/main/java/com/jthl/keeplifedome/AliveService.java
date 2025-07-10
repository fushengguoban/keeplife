package com.jthl.keeplifedome;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.os.EnvironmentCompat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes.dex */
public class AliveService extends Service {
    private String TAG = "BootService";
    Context mContext;
    private String outPackageInfo;
    PackageManager packageManager;
    public SharedPreferences shared;
    public SharedPreferences shared2;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Application application = getApplication();
        mContext = application;
        shared = application.getSharedPreferences("alive_app", 0);
        shared2 = mContext.getSharedPreferences("alive_time", 0);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        String string = shared2.getString("aliveTime", "10");
        new Timer(true).schedule(new MyTimerTask(), 0L, 1000 * Long.parseLong(string));
        return super.onStartCommand(intent, i, i2);
    }

    /* loaded from: classes.dex */
    public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            String string = shared.getString("appPackageName", " ");
            Log.e("aaaa","当前存储的包名是："+string);
            String s = RootCommand("dumpsys window | grep mCurrentFocus");
            if (string.equals(" ") || s.contains(string)) {
                return;
            }
            AliveService.this.ApplicationLauncher(string);
        }
    }

    public String getProperty(String str) {
        try {
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, EnvironmentCompat.MEDIA_UNKNOWN);
            } catch (Exception e) {
                e.printStackTrace();
                return EnvironmentCompat.MEDIA_UNKNOWN;
            }
        } catch (Throwable unused) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public void setProperty(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            cls.getMethod("set", String.class, String.class).invoke(cls, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean ApplicationLauncher(String str) {
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage != null) {
            getApplication().startActivity(launchIntentForPackage);
            return true;
        }
        Log.d(this.TAG, "PackageName is error");
        return false;
    }

    public String RootCommand(String str) {
        Process process;
        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream2 = new DataOutputStream(process.getOutputStream());

            dataOutputStream2.writeBytes(str + "\n");
            dataOutputStream2.writeBytes("exit\n");
            dataOutputStream2.flush();
            process.waitFor();
            dataOutputStream2.close();
            return dataOutputStream2.toString();
        } catch (Exception e) {

        }
        return dataOutputStream.toString();
    }
}
