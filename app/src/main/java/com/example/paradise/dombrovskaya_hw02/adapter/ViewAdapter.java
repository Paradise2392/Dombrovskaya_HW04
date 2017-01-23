package com.example.paradise.dombrovskaya_hw02.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paradise.dombrovskaya_hw02.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Paradise on 22.12.2016.
 */


public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> implements Filterable {

    int filterCounter = 0;

    private ListFilter listFilter = new ListFilter();
    private List<ApplicationInfo> originalObjectsList;
    private ArrayList<ApplicationInfo> filteredListObjects;
    private int layoutFormat;
    private SharedPreferences sharedPreferences;
    private final int MAXIMUM = 12;

    static private List<ApplicationInfo> objectsList;
    static private PackageManager packageManager;

    static Context contextApp;

    public void setLayoutFormat(int layoutFormat) {
        this.layoutFormat = layoutFormat;
    }

    public ViewAdapter(Context context, List<ApplicationInfo> objects) {
        contextApp = context;
        objectsList = objects;
        originalObjectsList = new ArrayList<ApplicationInfo>(objects);
        filteredListObjects = new ArrayList<ApplicationInfo>();
        packageManager = context.getPackageManager();
        sharedPreferences = context.getSharedPreferences("CheckPosition" , Context.MODE_PRIVATE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appName;
        public ImageView appImage;
        public CheckBox appCheckBox;

        public ViewHolder(View view) {
            super(view);
            appName = (TextView) view.findViewById(R.id.app_item_text);
            appImage = (ImageView) view.findViewById(R.id.app_item_image);
            appCheckBox = (CheckBox) view.findViewById(R.id.app_item_checkbox_);
            appCheckBox.setClickable(false);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View withText = LayoutInflater.from(parent.getContext()).inflate(layoutFormat, parent, false);
        ViewHolder viewHolder = new ViewHolder(withText);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ApplicationInfo app = objectsList.get(position);
        holder.appName.setText(objectsList.get(position).loadLabel(packageManager));
        holder.appImage.setImageDrawable(objectsList.get(position).loadIcon(packageManager));
        loadPreferences(holder, app);
        if (countMax() == MAXIMUM) {
            holder.appCheckBox.setEnabled(holder.appCheckBox.isChecked());
            holder.itemView.setEnabled(holder.appCheckBox.isChecked());
        }else{
            holder.appCheckBox.setEnabled(true);
            holder.itemView.setEnabled(true);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.appCheckBox.isChecked()){
                    holder.appCheckBox.setChecked(true);
                }else{
                    holder.appCheckBox.setChecked(false);
                }
                sharedPreferences.edit().putBoolean(app.packageName, holder.appCheckBox.isChecked()).apply();
                notifyDataSetChanged();
            }
        });
    }

    public void loadPreferences(ViewHolder holder, ApplicationInfo app){
        SharedPreferences sharedPreferences = contextApp.getSharedPreferences("CheckPosition" , Context.MODE_PRIVATE);
        boolean checked = sharedPreferences.getBoolean(app.packageName, false);
        holder.appCheckBox.setChecked(checked);
    }

    public int countMax(){
        int counter = 0;
        for(ApplicationInfo temp : objectsList){
            if(sharedPreferences.getBoolean(temp.packageName, false)){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            objectsList.clear();
            filteredListObjects.clear();
            FilterResults filterResults = new FilterResults();
            String str = constraint.toString().toLowerCase();

            if (constraint != null || constraint.length() != 0) {
                for (ApplicationInfo obj : originalObjectsList) {
                    if (obj.loadLabel(packageManager).toString().toLowerCase().contains(str)) {
                        filteredListObjects.add(obj);
                    }
                }
            }

            filterResults.values = filteredListObjects;
            filterResults.count = filteredListObjects.size();
            filterCounter = filterResults.count;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            objectsList.addAll(((List<ApplicationInfo>) results.values));
            notifyDataSetChanged();
        }
    }
}
