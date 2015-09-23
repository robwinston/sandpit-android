package com.rtwsquared.android.sandpit.storage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SimpleFileActivity extends TraceBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_file);
        setupTrace(SimpleFileActivity.class.getSimpleName());

        ((EditText) findViewById(R.id.simple_file_edit_text_id)).setText(readFromFile());

        findViewById(R.id.simple_file_save_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile(((EditText) findViewById(R.id.simple_file_edit_text_id)).getText().toString());
            }
        });
    }


    private void writeToFile(String myData) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("myThoughts.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(myData);
            outputStreamWriter.close(); // always close your streams!
        } catch (IOException e) {
            traceMe(e.getMessage());
        }
    }


    private String readFromFile() {

        String result = "";

        try {
            InputStream inputStream = openFileInput("myThoughts.txt");
            if (inputStream != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String tempString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((tempString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(tempString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }

        } catch (IOException e) {
            traceMe(e.getMessage());
        }

        return result;
    }
}
