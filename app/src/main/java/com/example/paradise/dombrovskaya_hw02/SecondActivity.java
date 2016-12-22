package com.example.paradise.dombrovskaya_hw02;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonGrid;
    Button buttonList;
    RecyclerView recyclerView;
    ViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private EditText editText;
    private Filter filter;
    private PackageManager packageManager = null;
    private List<ApplicationInfo> appList = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                layoutManager = new GridLayoutManager(this, 3);
                recyclerView.setLayoutManager(layoutManager);
                break;
            case R.id.list:
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                break;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Second Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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

            adapter = new ViewAdapter(SecondActivity.this, appList);
            filter = adapter.getFilter();
            recyclerView.setAdapter(adapter);

            layoutManager = new GridLayoutManager(SecondActivity.this, 3);
            recyclerView.setLayoutManager(layoutManager);

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(SecondActivity.this, null, "Info");
            super.onPreExecute();
        }
    }
}
