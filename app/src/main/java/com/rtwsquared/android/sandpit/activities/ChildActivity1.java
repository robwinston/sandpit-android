package com.rtwsquared.android.sandpit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class ChildActivity1 extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTrace(ChildActivity1.class.getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child1);


        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(getString(R.string.parentActivity_data_key_1)))
            showData(extras.getString(getString(R.string.parentActivity_data_key_1)));
        else {
            showData(getString(R.string.childActivity_no_data_from_parent));
        }

        findViewById(R.id.childActivity_sendButton_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setData();
        super.onBackPressed();
    }

    private void setData() {
        String theData = ((EditText) findViewById(R.id.childActivity_edit_id)).getText().toString();

        getIntent().putExtra(getString(R.string.childActivity_data_key_1), "Child Entered: "
                + (theData.isEmpty() ? "nothing?!" : theData));
        setResult(RESULT_OK, getIntent());
    }
    private void showData(String dataFromSub) {
        ((TextView)findViewById(R.id.childActivity_text_id)).setText(dataFromSub);
        traceMe(dataFromSub);
    }
}
