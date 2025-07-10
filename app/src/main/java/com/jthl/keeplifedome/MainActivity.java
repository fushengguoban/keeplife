package com.jthl.keeplifedome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jthl.keeplifedome.adapter.AppInfos2Adapter;
import com.jthl.keeplifedome.adapter.KeepAlive2Adapter;
import com.jthl.keeplifedome.mode.AppInfo;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MainActivity extends Activity {
    Button alive_null;
    Spinner alive_spinner;
    Button app_null;
    SharedPreferences shared;
    SharedPreferences shared2;
    SharedPreferences shared3;
    RecyclerView appInfoListView = null;
    RecyclerView aliveInfoListView = null;
    List<AppInfo> appInfos = null;
    AppInfos2Adapter infosAdapter1 = null;
    KeepAlive2Adapter infosAdapter2 = null;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        appInfoListView = (RecyclerView) findViewById(R.id.appinfo_list1);
        aliveInfoListView = (RecyclerView) findViewById(R.id.appinfo_list2);
        shared = getSharedPreferences("boot_app", 0);
        shared2 = getSharedPreferences("alive_time", 0);
        shared3 = getSharedPreferences("alive_app", 0);
        app_null = (Button) findViewById(R.id.app_null);
        alive_null = (Button) findViewById(R.id.alive_null);
        Spinner spinner = (Spinner) findViewById(R.id.alive_spinner);
        alive_spinner = spinner;

        appInfoListView.setLayoutManager(new LinearLayoutManager(this));
        aliveInfoListView.setLayoutManager(new LinearLayoutManager(this));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.xinhua.appmanager.MainActivity.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String[] stringArray = MainActivity.this.getResources().getStringArray(R.array.times);
                SharedPreferences.Editor edit = MainActivity.this.shared2.edit();
                edit.putString("aliveTime", stringArray[i]);
                edit.putInt("timePosition", i);
                edit.commit();
                MainActivity.this.startService(new Intent(MainActivity.this, AliveService.class));
                Context applicationContext = MainActivity.this.getApplicationContext();
                Toast.makeText(applicationContext, "set app alive time: " + stringArray[i] + " s", 1).show();
            }
        });
        app_null.setOnClickListener(new View.OnClickListener() { // from class: com.xinhua.appmanager.MainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SharedPreferences.Editor edit = MainActivity.this.shared.edit();
                edit.putString("appPackageName", " ");
                edit.commit();
                Toast.makeText(MainActivity.this.getApplicationContext(), "cancel app self start", 1).show();
            }
        });
        alive_null.setOnClickListener(new View.OnClickListener() { // from class: com.xinhua.appmanager.MainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SharedPreferences.Editor edit = MainActivity.this.shared3.edit();
                edit.putString("appPackageName", " ");
                edit.commit();
                Toast.makeText(MainActivity.this.getApplicationContext(), "cancel app keep alive", 1).show();
            }
        });
        List<AppInfo> appInfos = getAppInfos();
        appInfos = appInfos;
        updateUI(appInfos);
        alive_spinner.setSelection(shared2.getInt("timePosition", 1));
        startService(new Intent(this, AliveService.class));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION"));
            return;
        }
        Button button = new Button(getApplicationContext());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2038;
        layoutParams.format = 1;
        layoutParams.flags = 56;
        layoutParams.width = 1;
        layoutParams.height = 1;
        ((WindowManager) getApplicationContext().getSystemService("window")).addView(button, layoutParams);

    }

    public void updateUI(List<AppInfo> list) {
        Log.e("aaaa","数据集合：+"+list);
        if (list != null) {
            infosAdapter1 = new AppInfos2Adapter(getApplication(), list);
            infosAdapter2 = new KeepAlive2Adapter(getApplication(), list);
            appInfoListView.setAdapter(infosAdapter1);
            aliveInfoListView.setAdapter(infosAdapter2);
        }
    }

    public List<AppInfo> getAppInfos() {
        PackageManager packageManager = getApplication().getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(8192);
        appInfos = new ArrayList();
        for (PackageInfo packageInfo : installedPackages) {
            int i = packageInfo.applicationInfo.flags;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((i & 1) == 0 && !packageInfo.packageName.equals(BuildConfig.APPLICATION_ID)) {
                appInfos.add(new AppInfo(applicationInfo.loadLabel(packageManager).toString(), packageInfo.packageName, packageInfo.applicationInfo.loadIcon(packageManager)));
            }
        }
        return appInfos;
    }
}
