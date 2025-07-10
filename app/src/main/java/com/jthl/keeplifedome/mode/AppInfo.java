package com.jthl.keeplifedome.mode;

import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class AppInfo {
    String appName;
    Drawable drawable;
    String packageName;

    public AppInfo() {
    }

    public AppInfo(String str, String str2) {
        this.appName = str;
        this.packageName = str2;
    }

    public AppInfo(String str, String str2, Drawable drawable) {
        this.appName = str;
        this.packageName = str2;
        this.drawable = drawable;
    }

    public String getAppName() {
        String str = this.appName;
        return str == null ? "" : str;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getPackageName() {
        String str = this.packageName;
        return str == null ? "" : str;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
