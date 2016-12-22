package com.example.paradise.dombrovskaya_hw02;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    static private List<ApplicationInfo> objectsList;
    static private PackageManager packageManager;

    static OnItemClickListener itemClickListener;
    static Activity activity;
    static Context contextApp;

    public ViewAdapter(Context context, List<ApplicationInfo> objects) {
        contextApp = context;
        objectsList = objects;
        originalObjectsList = new ArrayList<ApplicationInfo>(objects);
        filteredListObjects = new ArrayList<ApplicationInfo>();
        packageManager = context.getPackageManager();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView appName;
        public ImageView appImage;

        public ViewHolder(View view) {
            super(view);
            appName = (TextView) view.findViewById(R.id.app_item_text);
            appImage = (ImageView) view.findViewById(R.id.app_item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getPosition());
            }

            try {
                Intent intent = packageManager.getLaunchIntentForPackage(objectsList.get(getPosition()).packageName);
                if (intent != null) {
                    contextApp.startActivity(intent);
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View withText = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        ViewHolder viewHolder = new ViewHolder(withText);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.appName.setText(objectsList.get(position).loadLabel(packageManager));
        holder.appImage.setImageDrawable(objectsList.get(position).loadIcon(packageManager));
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
