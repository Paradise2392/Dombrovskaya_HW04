package com.example.paradise.dombrovskaya_hw02.ui;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;

import com.example.paradise.dombrovskaya_hw02.R;
import com.example.paradise.dombrovskaya_hw02.adapter.ViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonGrid;
    Button buttonList;
    RecyclerView recyclerView;
    ViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private EditText editText;
    private Filter filter;
    private PackageManager packageManager = null;
    private List<ApplicationInfo> appList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);

        packageManager = getPackageManager();
        new LoadApplications().execute();

        buttonGrid = (Button) findViewById(R.id.grid);
        buttonList = (Button) findViewById(R.id.list);

        buttonGrid.setOnClickListener(this);
        buttonList.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.text);
        editText.addTextChangedListener(textWatcher);

        recyclerView = (RecyclerView) findViewById(R.id.listRecycle);
        recyclerView.setHasFixedSize(true);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            filter.filter(s);
        }
    };

    private List checkForLaunchIntent(List<ApplicationInfo> list) {

        ArrayList appList = new ArrayList();

        for (ApplicationInfo info : list) {
            try {
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appList;
    }

    @Override
    public void onClick(View view) {
        RecyclerView.LayoutManager layoutManager;
        switch (view.getId()) {
            case R.id.grid:
                adapter.setLayoutFormat(R.layout.item_grid);
                layoutManager = new GridLayoutManager(this, 3);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.getRecycledViewPool().clear();
                break;
            case R.id.list:
                adapter.setLayoutFormat(R.layout.item_list);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.getRecycledViewPool().clear();
                break;
        }
    }



    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

            adapter = new ViewAdapter(AppActivity.this, appList);
            filter = adapter.getFilter();
            recyclerView.setAdapter(adapter);

            layoutManager = new LinearLayoutManager(AppActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            adapter.setLayoutFormat(R.layout.item_list);

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AppActivity.this, null, "Info");
            super.onPreExecute();
        }
    }
}
