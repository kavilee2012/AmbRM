package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016-07-15.
 */
public class DrawActivity extends AppCompatActivity {

    android.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

         actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
