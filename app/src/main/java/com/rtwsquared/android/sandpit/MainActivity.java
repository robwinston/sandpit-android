package com.rtwsquared.android.sandpit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class MainActivity extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTrace(MainActivity.class.getSimpleName());
    }
}
