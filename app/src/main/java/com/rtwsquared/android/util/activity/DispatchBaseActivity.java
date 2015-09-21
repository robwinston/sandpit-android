package com.rtwsquared.android.util.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.misc.ClassFinder;

import java.util.List;

public class DispatchBaseActivity extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private static int idInt = 1;

    private static int nextInt() {
        return idInt++;
    }

    // This method is implemented at api level 17
    // Not really sure what happens if running at a lower level, so doing this for now
    // TODO Learn canonical approach to using API levels
    protected int getViewId() {
        int i;
        try {
            i = View.generateViewId();
        } catch (Exception e) {
            i = nextInt();
        }
        return i;
    }


    @NonNull
    protected LinearLayout getLinearLayoutForActivityClass(Class aClass) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(5, 10, 5, 10);


        // Create Launcher Button
        final Button button = new Button(this);
        button.setId(getViewId());
        button.setText("Launch");
        // set the layoutParams on the button
        button.setLayoutParams(layoutParams);

        // Set click listener for button
        setNewActivityOnClickListener(button, aClass);

        ll.addView(button);

        // Create Launcher text using Activity class name
        TextView name1 = new TextView(this);
        name1.setText(String.format("%s", aClass.getSimpleName()));
        ll.addView(name1);
        return ll;
    }

    protected <T> void setNewActivityOnClickListener(int viewId, final Class<T> aClass) {
        setNewActivityOnClickListener(findViewById(viewId), aClass);
    }

    protected <T> void setNewActivityOnClickListener(View view, final Class<T> aClass) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), aClass);
                startActivity(intent);
            }
        });
    }

    protected <T> void setNewActivityOnClickListener(View view, final Class<T> aClass, final List<IntentData> dataToSend) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), aClass);
                for (IntentData intentData : dataToSend) {
                    intent.putExtra(intentData.getKey(), intentData.getData());
                }
                startActivity(intent);
            }
        });
    }

    protected List<Class<?>> getClasses(String packageName) {
        return ClassFinder.getClassesOfPackage(packageName, getPackageCodePath());
    }


    protected String getDispatchPackageName() {
        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(getString(R.string.dispatch_package_name_key)))
            return extras.getString(getString(R.string.dispatch_package_name_key));

        return "";
    }

}
