package com.rtwsquared.android.util.activity;

import android.os.Bundle;
import android.util.Xml;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class PackageDispatcherActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_dispatcher);
        setupTrace(PackageDispatcherActivity.class.getSimpleName());

        addDispatcherLayout((LinearLayout) findViewById(R.id.package_dispatch_parent_layout));
        addActivityDispatcherOptions((RadioGroup) findViewById(R.id.package_dispatch_radio_group_id));
    }

    private void addActivityDispatcherOptions(RadioGroup parentLayout) {




    }

    // placeholder to permit doing this some other way ...
    private String[] getDispatcherClassesFromConfig() {
        return getResources().getStringArray(R.array.progress_bar_url_image_urls);
    }



    private void addDispatcherLayout(LinearLayout parentLayout) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ActivityGroupCollection activityGroupCollection = getActivityGroupsFromXml(R.raw.activity_group_config);
        ((TextView) findViewById(R.id.jar_dispatch_debug_text)).setText(activityGroupCollection.size() + " ActivityGroups");


        for (ActivityGroup activityGroup : activityGroupCollection) {

            LinearLayout childLayout = new LinearLayout(this);
            childLayout.setOrientation(LinearLayout.VERTICAL);
            childLayout.setPadding(0, 10, 0, 10);

            LinearLayout grandchildLayout1 = new LinearLayout(this);
            grandchildLayout1.setOrientation(LinearLayout.HORIZONTAL);

            final Button button = new Button(this);
            button.setText(getString(R.string.dispatch_goto_group_button_text));
            button.setLayoutParams(params);

            List<IntentData> dataToSend = new ArrayList<>();
            dataToSend.add(new IntentData(getString(R.string.dispatch_package_name_key), activityGroup.getPackageName()));
            //setNewActivityOnClickListener(button, TableDispatcherActivity.class, dataToSend);
            setNewActivityOnClickListener(button, classForName("com.rtwsquared.android.util.activity.TableDispatcherActivity"), dataToSend);
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

    private ActivityGroupCollection getActivityGroupsFromXml(int resId) {

        ActivityGroupCollection activityGroupCollection = new ActivityGroupCollection();
        XmlPullParser parser = Xml.newPullParser();
        try {

            InputStream is = getApplicationContext().getResources().openRawResource(resId);
            InputStreamReader isr = new InputStreamReader(is);

            // auto-detect the encoding from the stream
            parser.setInput(isr);
            int eventType = parser.getEventType();
            ActivityGroup activityGroup = null;
            String packageRoot = "";
            boolean done = false;

            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("ActivityGroup")){
                            // Get attributes
                            String nameAttr = parser.getAttributeValue(null, "name");
                            String descriptionAttr = parser.getAttributeValue(null, "description");
                            String packageSuffixAttr = parser.getAttributeValue(null, "packageSuffix");
                            activityGroup = new ActivityGroup(nameAttr, descriptionAttr, packageRoot + packageSuffixAttr);
                        }
                        else if (name.equalsIgnoreCase("PackageRoot")){
                            packageRoot = parser.getAttributeValue(null, "name");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("ActivityGroup") && activityGroup != null) {
                            activityGroupCollection.addActivityGroup(activityGroup);
                        } else if (name.equalsIgnoreCase("ActivityGroupConfig")){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
            //TODO More robust "activity parsing" exception handling
        } catch (FileNotFoundException e) {
            traceMe(String.format("getActivityGroupsFromXml => %s", e.getMessage()));
        } catch (IOException e) {
            traceMe(String.format("getActivityGroupsFromXml => %s", e.getMessage()));
        } catch (Exception e){
            traceMe(String.format("getActivityGroupsFromXml => %s", e.getMessage()));
        }

        return activityGroupCollection;
    }
}
