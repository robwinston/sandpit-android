package com.rtwsquared.android.sandpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class ParentActivity extends TraceBaseActivity {

    static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setupTrace(ParentActivity.class.getSimpleName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        final Intent intent = new Intent(ParentActivity.this, ChildActivity1.class);

        findViewById(R.id.parentActivity_goto_child_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theData = ((EditText) findViewById(R.id.parentActivity_edit_id)).getText().toString();
                intent.putExtra(getString(R.string.parentActivity_data_key_1),
                        "Parent Entered: " + (theData.isEmpty() ? "nothing?!" : theData));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra(getString(R.string.childActivity_data_key_1))) {


                ((TextView) findViewById(R.id.myactivity_text_id)).
                        setText(data.getStringExtra(getString(R.string.childActivity_data_key_1)));
            }
            else {
                String dataFromSub = getString(R.string.parentActivity_no_data_from_child);
                ((TextView) findViewById(R.id.myactivity_text_id)).setText(dataFromSub);
            }
        }
    }
}
