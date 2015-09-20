package com.rtwsquared.android.sandpit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtwsquared.android.util.activity.ActivityGroup;
import com.rtwsquared.android.util.activity.ActivityGroupCollection;
import com.rtwsquared.android.util.activity.DispatchBaseActivity;
import com.rtwsquared.android.util.activity.IntentData;
import com.rtwsquared.android.util.activity.TableDispatcherActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTrace(MainActivity.class.getSimpleName());

        LinearLayout ll = (LinearLayout) findViewById(R.id.main_parent_layout);
        int itsId = ll == null ? 0 : ll.getId();
        traceMe("Got parent layout: " + itsId);
        addDispatcherLayout(ll);
    }

    private void addDispatcherLayout(LinearLayout parentLayout)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ActivityGroupCollection activityGroupCollection = getActivityGroups();
        ((TextView) findViewById(R.id.main_debug_text)).setText(activityGroupCollection.size() + " ActivityGroups");

        for (ActivityGroup activityGroup : activityGroupCollection) {

            //getTracer().traceMe("Adding: " + activityGroup.getName());

            LinearLayout childLayout = new LinearLayout(this);
            childLayout.setOrientation(LinearLayout.VERTICAL);
            childLayout.setPadding(0,10,0,10);

            LinearLayout grandchildLayout1 = new LinearLayout(this);
            grandchildLayout1.setOrientation(LinearLayout.HORIZONTAL);

            // Create Launcher Button
            final Button button = new Button(this);
            // TODO decide whether or not an id is really needed
            button.setId(getViewId());
            button.setText("Go To");
            button.setLayoutParams(params);

            List<IntentData> dataToSend = new ArrayList<>();
            dataToSend.add(new IntentData(getString(R.string.dispatch_package_name_key), activityGroup.getPackageName()));
            setNewActivityOnClickListener(button, TableDispatcherActivity.class, dataToSend);
            grandchildLayout1.addView(button);

            TextView name1 = new TextView(this);
            name1.setText(String.format("%s", activityGroup.getName()));
            grandchildLayout1.addView(name1);

            LinearLayout grandchildLayout2 = new LinearLayout(this);
            grandchildLayout2.setOrientation(LinearLayout.HORIZONTAL);
            TextView name2 = new TextView(this);
            name2.setText(String.format("%s", activityGroup.getDescription()));
            grandchildLayout2.addView(name2);

            childLayout.addView(grandchildLayout1);
            childLayout.addView(grandchildLayout2);
            parentLayout.addView(childLayout);
        }

    }


    private ActivityGroupCollection getActivityGroups()
    {
        //TODO make this configurable
        // Could also do something by parsing manifest ...
        String packageRoot = "com.rtwsquared.android.sandpit.";

        ActivityGroupCollection activityGroupCollection = new ActivityGroupCollection();

        activityGroupCollection.addActivityGroup(new ActivityGroup("Activities", "Data exchange, etc.", packageRoot + "activities"));
        activityGroupCollection.addActivityGroup(new ActivityGroup("Views", "ListViews, etc.", packageRoot + "views"));
        activityGroupCollection.addActivityGroup(new ActivityGroup("Input Controls", "Radio Buttons, Checkboxes, etc.", packageRoot + "controls"));

        return activityGroupCollection;
    }

}
