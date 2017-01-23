package com.example.paradise.dombrovskaya_hw02.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.paradise.dombrovskaya_hw02.model.ApplicationPlacer;
import com.example.paradise.dombrovskaya_hw02.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton dialer;
    private ImageButton message;
    private Button app;
    private GridLayout gridLayout;
    private ApplicationPlacer applicationPlacer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        dialer = (ImageButton) findViewById(R.id.ButtonDialer);
        message = (ImageButton) findViewById(R.id.ButtonMessage);
        app = (Button) findViewById(R.id.ButtonApp);
        gridLayout = (GridLayout) findViewById(R.id.gl_main);
        applicationPlacer = new ApplicationPlacer(gridLayout, this);
        applicationPlacer.fillGridLayout();


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ButtonDialer:
                        Intent dial = new Intent(Intent.ACTION_DIAL);
                        startActivity(dial);
                    case R.id.ButtonMessage:
                        Intent mes = new Intent(Intent.ACTION_VIEW);
                        mes.setData(Uri.parse("sms:"));
                        startActivity(mes);
                    case R.id.ButtonApp:
                        Intent app = new Intent(MainActivity.this,AppActivity.class);
                        startActivity(app);
                }
            }
        };
        dialer.setOnClickListener(onClickListener);
        message.setOnClickListener(onClickListener);
        app.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applicationPlacer.fillWithApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
