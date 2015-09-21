package com.rtwsquared.android.sandpit;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.rtwsquared.android.util.activity.DispatchBaseActivity;
import com.rtwsquared.android.util.activity.JarDispatcherActivity;

public class MainActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTrace(MainActivity.class.getSimpleName());

        LinearLayout ll = (LinearLayout) findViewById(R.id.main_parent_layout);
        ll.addView(getLinearLayoutForActivityClass(JarDispatcherActivity.class));
    }
}
