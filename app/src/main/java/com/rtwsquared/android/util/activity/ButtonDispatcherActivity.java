package com.rtwsquared.android.util.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.misc.ClassFinder;

import java.util.ArrayList;

public class ButtonDispatcherActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_dispatcher);
        setupTrace(ButtonDispatcherActivity.class.getSimpleName());


        // TODO improve layout
        // TODO break up into smaller units
        final LinearLayout lm = (LinearLayout) findViewById(R.id.dispatch_layout);
        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        try {

            String packageName = getDispatchPackageName();
            ArrayList<Class<?>> classes = ClassFinder.getClassesOfPackage(packageName, getPackageCodePath());

            int activities = 0;
            for (Class aClass : classes) {
                //for (String aClass : classes) {
                // Create LinearLayout
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(5, 10, 5, 10);


                // Create Launcher Button
                final Button button = new Button(this);
                // Give button an ID
                // TODO decide whether or not an id is really needed
                button.setId(getViewId());
                button.setText("Launch");
                // set the layoutParams on the button
                button.setLayoutParams(params);

                // Set click listener for button
                setNewActivityOnClickListener(button, classes.get(activities));

                ll.addView(button);

                // Create Launcher text using Activity class name
                TextView name1 = new TextView(this);
                name1.setText(String.format("%s", aClass.getSimpleName()));
                ll.addView(name1);

                lm.addView(ll);
                activities++;
            }
            String activityString = activities == 1 ? "activity" : "activities";
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(String.format("Found %d %s", activities, activityString));

        } catch (Exception e) {
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(e.getMessage());
        }
    }

    private String getDispatchPackageName() {
        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(getString(R.string.dispatch_package_name_key)))
            return extras.getString(getString(R.string.dispatch_package_name_key));

        return "";
    }
}
