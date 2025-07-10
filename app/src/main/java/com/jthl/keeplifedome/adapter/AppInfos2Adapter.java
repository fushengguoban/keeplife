package com.jthl.keeplifedome.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jthl.keeplifedome.R;
import com.jthl.keeplifedome.mode.AppInfo;
import com.jthl.keeplifedome.mode.MyLog;

import java.util.List;

/**
 * @author wanglei
 * @date 2025/7/9 16:52
 * @Description：
 */
public class AppInfos2Adapter extends RecyclerView.Adapter<AppInfos2Adapter.BaseViewHolder> {
    List<AppInfo> appInfos;
    Context context;
    MyLog log = new MyLog();
    SharedPreferences shared;

    public AppInfos2Adapter(Context context, List<AppInfo> list) {
        this.context = context;
        this.appInfos = list;
        this.shared = context.getSharedPreferences("boot_app", 0);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(this.context).inflate(R.layout.app_info_item, (ViewGroup) null);
        return new BaseViewHolder(view2);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        AppInfo appInfo = appInfos.get(position);

        holder.btn_boot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = shared.edit();
                edit.putString("appPackageName",appInfo.getPackageName());
                edit.commit();
                Toast.makeText(context.getApplicationContext(), "set self start app success",Toast.LENGTH_LONG).show();

            }
        });
        holder.appIconImg.setBackground(appInfo.getDrawable());
        holder.appNameText.setText(appInfo.getAppName());
        holder.appPackageText.setText(appInfo.getPackageName());
    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImg;
        TextView appNameText;
        TextView appPackageText;
        Button btn_boot;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImg=itemView.findViewById(R.id.app_icon);
            appNameText = itemView.findViewById(R.id.app_info_name);
            appPackageText = itemView.findViewById(R.id.app_info_package_name);
            btn_boot = itemView.findViewById(R.id.btn_boot);
        }
    }
}
