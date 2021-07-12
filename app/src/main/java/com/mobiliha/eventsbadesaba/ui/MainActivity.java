package com.mobiliha.eventsbadesaba.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        DbHelper.getInstance(this).close();
        super.onStop();
    }
}