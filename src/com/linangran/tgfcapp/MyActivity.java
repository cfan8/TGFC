package com.linangran.tgfcapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MyActivity extends ActionBarActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
