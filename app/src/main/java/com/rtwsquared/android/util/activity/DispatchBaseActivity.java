package com.rtwsquared.android.util.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.misc.ClassFinder;

import java.util.ArrayList;
import java.util.List;

public class DispatchBaseActivity extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private static int idInt = 1;
    private static int nextInt()
    {
        return idInt++;
    }

    // This method is implemented at api level 17
    // Not really sure what happens if running at a lower level, so doing this for now
    // TODO Learn canonical approach to using API levels
    protected int getViewId()
    {
        int i;
        try {
            i = View.generateViewId();
        }
        catch (Exception e) {
            i = nextInt();
        }
        return i;
    }


    protected <T> void setNewActivityOnClickListener(int viewId, final Class<T> aClass)
    {
        setNewActivityOnClickListener(findViewById(viewId), aClass);
    }

    protected <T> void setNewActivityOnClickListener(View view, final Class<T> aClass)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), aClass);
                startActivity(intent);
            }
        });
    }

    protected <T> void setNewActivityOnClickListener(View view, final Class<T> aClass,  final List<IntentData> dataToSend)
    {
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
