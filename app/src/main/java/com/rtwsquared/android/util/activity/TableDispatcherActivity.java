package com.rtwsquared.android.util.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;

import java.util.List;

public class TableDispatcherActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_dispatcher);


        final TableLayout tableLayout = (TableLayout) findViewById(R.id.dispatch_layout);

        TableRow.LayoutParams trlp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        try {

            String packageName = getDispatchPackageName();
            List<Class<?>> classes = getClasses(packageName);

            int activities = 0;
            for (Class aClass : classes) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(trlp);
                //tableRow.setOrientation(LinearLayout.HORIZONTAL);
                //tableRow.setPadding(5, 10, 5, 10);

                // Create Launcher Button
                final Button button = new Button(this);
                // Give button an ID
                // TODO decide whether or not an id is really needed
                button.setId(getViewId());
                button.setText("Launch");
                tableRow.addView(button);

                // Set click listener for button
                setNewActivityOnClickListener(button, classes.get(activities));


                // Create Launcher text using Activity class name
                TextView name1 = new TextView(this);
                name1.setText(String.format("%s", aClass.getSimpleName()));
                tableRow.addView(name1);

                tableLayout.addView(tableRow);
                activities++;
            }
            String activityString = activities == 1 ? "activity" : "activities";
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(String.format("Found %d %s", activities, activityString));

        } catch (Exception e) {
            ((TextView) findViewById(R.id.dispatch_debug_text)).setText(e.getMessage());
        }

    }

}
