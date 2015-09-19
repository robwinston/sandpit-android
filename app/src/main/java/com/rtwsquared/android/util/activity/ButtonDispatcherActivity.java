package com.rtwsquared.android.util.activity;

import android.os.Bundle;

public class ButtonDispatcherActivity extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTrace(ButtonDispatcherActivity.class.getSimpleName());
    }
}
