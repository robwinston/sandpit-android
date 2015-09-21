package com.rtwsquared.android.util.activity;

import android.os.Bundle;

import com.rtwsquared.android.sandpit.R;

public class TraceBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_trace_base);
    }

    @Override
    protected void onStart() {
        traceCacheStats();
        //traceMe("");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        //traceCacheStats();
        //traceMe("");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        //traceCacheStats();
        //traceMe("");
        super.onResume();
    }

    @Override
    protected void onPause() {
        //traceCacheStats();
        //traceMe("");
        super.onPause();
    }

    @Override
    protected void onStop() {
        //traceCacheStats();
        //traceMe("");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        traceCacheStats();
        //traceMe("");
        super.onDestroy();
    }
}
