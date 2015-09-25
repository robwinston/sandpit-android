package com.rtwsquared.android.sandpit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rtwsquared.android.util.activity.DispatchBaseActivity;
import com.rtwsquared.android.util.activity.IntentData;
import com.rtwsquared.android.util.activity.PackageDispatcherActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends DispatchBaseActivity  implements CompoundButton.OnCheckedChangeListener {

    HashMap<String, String> dispatcherClassMap;

    IntentData intentData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTrace(MainActivity.class.getSimpleName());

        dispatcherClassMap = getDispatcherClassMapFromConfig();
        addActivityDispatcherOptions((LinearLayout) findViewById(R.id.package_dispatch_radio_group_parent_layout));
        findViewById(R.id.main_run_button_id).setEnabled(false);
        findViewById(R.id.main_run_button_id).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PackageDispatcherActivity.class);
                intent.putExtra(intentData.getKey(), intentData.getData());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findViewById(R.id.package_dispatch_radio_group_parent_layout).setVisibility(View.VISIBLE);
    }

    private void addActivityDispatcherOptions(LinearLayout parentLayout) {
        // Doc says id doesn't have to globally unique ..
        int id = 1;
        TextView textView = new TextView(this);
        textView.setLayoutParams(getLayoutParams());
        textView.setText("Select Dispatcher layout");
        parentLayout.addView(textView);

        RadioGroup dispatcherClassnameRadioGroup = new RadioGroup(this);
        dispatcherClassnameRadioGroup.setId(id++);
        dispatcherClassnameRadioGroup.setLayoutParams(getLayoutParams());

        dispatcherClassMap = getDispatcherClassMapFromConfig();
        for (String classname : dispatcherClassMap.keySet()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(getLayoutParams());
            radioButton.setText(classname);
            // Stash full name of class in button's tag
            radioButton.setTag(dispatcherClassMap.get(classname));
            radioButton.setId(id++);
            radioButton.setOnCheckedChangeListener(this);
            dispatcherClassnameRadioGroup.addView(radioButton);
        }
        parentLayout.addView(dispatcherClassnameRadioGroup);
    }


    /**
     * @return A map of Dispatcher classnames defined as string resources
     */
    private HashMap<String, String> getDispatcherClassMapFromConfig() {
        String[] fullNames =  getResources().getStringArray(R.array.dispatch_package_dispatcher_classes);
        HashMap<String, String> classMap = new HashMap<>(fullNames.length);

        for (String fullName : fullNames) {
            String shortName = fullName.substring(fullName.lastIndexOf('.')+1);
            classMap.put(shortName, fullName);
        }
        return classMap;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // First time, create it ..
            // Subsequent times, update it ... can't replace with new instance because listener has been given a ref to it
            if (intentData == null)
                intentData = new IntentData(getString(R.string.dispatch_classname_key), buttonView.getTag().toString());
            else
                intentData.setData(buttonView.getTag().toString());

            findViewById(R.id.main_run_button_id).setEnabled(true);
        }
    }
}
