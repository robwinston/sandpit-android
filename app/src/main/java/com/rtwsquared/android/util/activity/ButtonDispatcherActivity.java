package com.rtwsquared.android.util.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;

import java.util.List;

public class ButtonDispatcherActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_dispatcher);
        setupTrace(ButtonDispatcherActivity.class.getSimpleName());

        final LinearLayout lm = (LinearLayout) findViewById(R.id.dispatch_layout);


        try {

            String packageName = getDispatchPackageName();
            List<Class<?>> classes = getClasses(packageName);

            int activities = 0;
            for (Class aClass : classes) {
                // Create LinearLayout
                LinearLayout ll = getLinearLayoutForActivityClass(aClass);

                lm.addView(ll);
                activities++;
            }
            String activityString = activities == 1 ? "activity" : "activities";
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(String.format("Found %d %s", activities, activityString));

        } catch (Exception e) {
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(e.getMessage());
        }
    }
}
