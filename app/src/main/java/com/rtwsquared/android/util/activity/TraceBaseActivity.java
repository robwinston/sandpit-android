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
        traceMe("");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        // We have to do this dance every time (instead of in a convenience method) to preserve call depth
        traceMe("");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // We have to do this dance every time (instead of in a convenience method) to preserve call depth
        traceMe("");
        super.onResume();
    }

    @Override
    protected void onPause() {
        // We have to do this dance every time (instead of in a convenience method) to preserve call depth
        traceMe("");
        super.onPause();
    }

    @Override
    protected void onStop() {
        // We have to do this dance every time (instead of in a convenience method) to preserve call depth
        traceMe("");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // We have to do this dance every time (instead of in a convenience method) to preserve call depth
        traceMe("");
        super.onDestroy();
    }
}
