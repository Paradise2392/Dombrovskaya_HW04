package com.example.paradise.dombrovskaya_hw02.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paradise.dombrovskaya_hw02.R;
import com.example.paradise.dombrovskaya_hw02.interfaces.IApplicationPlacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Paradise on 21.01.2017.
 */

public class ApplicationPlacer implements IApplicationPlacer {
    GridLayout gridLayout;
    Context context;
    ArrayList<View> listViews;
    PackageManager packageManager;

    public ApplicationPlacer(GridLayout gridLayout, Context context) {
        this.gridLayout = gridLayout;
        this.context = context;
        listViews = new ArrayList<>();
        packageManager = context.getPackageManager();
    }

    @Override
    public void fillGridLayout() {
        for (int i = 0; i < 12; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.gridlayout_cell, gridLayout, false);
            view.setTag(new ViewHolder(view.findViewById(R.id.gridlayout_tv), view.findViewById(R.id.gridlayout_iv)));
            ((ViewHolder) view.getTag()).textView.setText("Application");
            ((ViewHolder) view.getTag()).imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
            CustomDragListener customDragListener = new CustomDragListener();
            view.setOnLongClickListener(customDragListener.longClickListener());
            view.setOnDragListener(customDragListener.onDragListener());
            listViews.add(view);
            gridLayout.addView(view);
        }

    }

    @Override
    public void fillWithApplication() {
        int cellCount = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("CheckPosition", Context.MODE_PRIVATE);
        ApplicationInfo appInfo;
        List<ApplicationInfo> temp = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        Map<String, ?> sharedApp = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : sharedApp.entrySet()) {
            final String packageName = entry.getKey();
            for (int i = 0; i < temp.size(); i++) {
                appInfo = temp.get(i);
                if (appInfo.packageName.equals(packageName)) {
                    View view = listViews.get(cellCount);
                    ((ViewHolder) view.getTag()).textView.setText(appInfo.loadLabel(packageManager));
                    ((ViewHolder) view.getTag()).imageView.setImageDrawable(appInfo.loadIcon(packageManager));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent appIntent = packageManager.getLaunchIntentForPackage(packageName);
                            context.startActivity(appIntent);
                        }
                    });
                }
            }
            cellCount++;
        }
        for (int i = cellCount; i < 12; i++) {
            View view = listViews.get(i);
            ((ViewHolder) view.getTag()).textView.setText("Application");
            ((ViewHolder) view.getTag()).imageView.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
        }

    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View text, View image) {
            this.textView = (TextView) text;
            this.imageView = (ImageView) image;
        }
    }

}
