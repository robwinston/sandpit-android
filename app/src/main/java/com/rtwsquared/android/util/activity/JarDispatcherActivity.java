package com.rtwsquared.android.util.activity;

import android.os.Bundle;
import android.util.Xml;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class JarDispatcherActivity extends DispatchBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jar_dispatcher);
        setupTrace(JarDispatcherActivity.class.getSimpleName());

        LinearLayout ll = (LinearLayout) findViewById(R.id.main_parent_layout);
        int itsId = ll == null ? 0 : ll.getId();
        traceMe("Got parent layout: " + itsId);
        addDispatcherLayout(ll);
    }

    private void addDispatcherLayout(LinearLayout parentLayout) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ActivityGroupCollection activityGroupCollection = getActivityGroups();
        ((TextView) findViewById(R.id.main_debug_text)).setText(activityGroupCollection.size() + " ActivityGroups");

        for (ActivityGroup activityGroup : activityGroupCollection) {

            //getTracer().traceMe("Adding: " + activityGroup.getName());

            LinearLayout childLayout = new LinearLayout(this);
            childLayout.setOrientation(LinearLayout.VERTICAL);
            childLayout.setPadding(0, 10, 0, 10);

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


    private ActivityGroupCollection getActivityGroups() {
        //TODO make this configurable
        // Could also do something by parsing manifest ...
        String packageRoot = getString(R.string.dispatch_package_root);

        ActivityGroupCollection activityGroupCollection = new ActivityGroupCollection();

        activityGroupCollection.addActivityGroup(new ActivityGroup("Activities", "Data exchange, etc.", packageRoot + "activities"));
        activityGroupCollection.addActivityGroup(new ActivityGroup("Views", "ListViews, etc.", packageRoot + "views"));
        activityGroupCollection.addActivityGroup(new ActivityGroup("Input Controls", "Radio Buttons, Checkboxes, etc.", packageRoot + "controls"));
        activityGroupCollection.addActivityGroup(new ActivityGroup("Media", "Media Player, etc.", packageRoot + "media"));

        return activityGroupCollection;
    }


    private ActivityGroupCollection getActivityGroupsFromXml() {

        ActivityGroupCollection activityGroupCollection = new ActivityGroupCollection();

        //R.raw.activity_group_config

        XmlPullParser parser = Xml.newPullParser();
        try {

            InputStream is = getApplicationContext().getResources().openRawResource(R.raw.activity_group_config);
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
                            packageRoot = parser.getText();
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
        } catch (FileNotFoundException e) {
            // TODO
        } catch (IOException e) {
            // TODO
        } catch (Exception e){
            // TODO
        }

        return activityGroupCollection;

    }

    // Processes title tags in the feed.
    private String readTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "tagName");
        String value = ""; //readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "tagName");
        return value;
    }


}
