package com.rtwsquared.android.sandpit.controls;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ProgressBarActivity extends TraceBaseActivity implements OnClickListener{

    private ProgressBar progressBarUrl;
    private ProgressBar progressBarDemo;
    private String filepath = "MyFileStorage";
    private String[] imageURLs;
    private File directory;
    private TextView statusUrl;
    private TextView statusDemo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        imageURLs = getImageURLsFromConfig();

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);

        progressBarUrl = (ProgressBar) findViewById(R.id.progress_bar_url_progressBar_id);
        progressBarUrl.setVisibility(View.GONE);
        statusUrl = (TextView) findViewById(R.id.progress_bar_url_result_id);
        statusUrl.setVisibility(View.GONE);

        Button downloadButtonUrl = (Button) findViewById(R.id.progress_bar_url_download_button_id);
        downloadButtonUrl.setOnClickListener(this);

        Button startButtonDemo = (Button) findViewById(R.id.progress_bar_demo_start_button_id);
        startButtonDemo.setOnClickListener(this);
        Button stopButtonDemo = (Button) findViewById(R.id.progress_bar_demo_stop_button_id);
        stopButtonDemo.setOnClickListener(this);
        progressBarDemo = (ProgressBar) findViewById(R.id.progress_bar_demo_progress_bar_id);
        progressBarDemo.setVisibility(View.GONE);
        statusDemo = (TextView) findViewById(R.id.progress_bar_demo_result_id);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.progress_bar_demo_start_button_id:
                progressBarDemo.setVisibility(View.VISIBLE);
                statusDemo.setText(getString(R.string.progress_bar_demo_text_started));
                break;

            case R.id.progress_bar_demo_stop_button_id:
                progressBarDemo.setVisibility(View.GONE);
                statusDemo.setText(getString(R.string.progress_bar_demo_text_started));
                break;

            case R.id.progress_bar_url_download_button_id:
                grabURL(imageURLs[getURLIndex()]);
                break;

            // More buttons go here (if any) ...

        }
    }

    // placeholder to permit doing this some other way ...
    private String[] getImageURLsFromConfig()
    {
        return getResources().getStringArray(R.array.progress_bar_url_image_urls);
    }

    int index = 0;
    private int getURLIndex() {
        int indexToReturn = index % imageURLs.length;
        index += 1;
        return indexToReturn;
    }


    public void grabURL(String url) {
        new GrabURL().execute(url);
    }
    private class GrabURL extends AsyncTask<String, Integer, String> {


        protected void onPreExecute() {
            statusUrl.setVisibility(View.VISIBLE);
            progressBarUrl.setVisibility(View.VISIBLE);
            statusDemo.setVisibility(View.GONE);

        }

        protected String doInBackground(String... urls) {

            String filename = "MySampleFile.png";
            File myFile = new File(directory , filename);

            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileSize = connection.getContentLength();

                InputStream is = new BufferedInputStream(url.openStream());
                OutputStream os = new FileOutputStream(myFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = is.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileSize));
                    os.write(data, 0, count);
                }

                os.flush();
                os.close();
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return filename;

        }

        protected void onProgressUpdate(Integer... progress) {
            statusUrl.setVisibility(View.VISIBLE);
            progressBarUrl.setVisibility(View.VISIBLE);
            statusUrl.setText(String.valueOf(progress[0]) + "%");
            progressBarUrl.setProgress(progress[0]);
        }

        protected void onCancelled() {
            Toast toast = Toast.makeText(getBaseContext(),
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

        protected void onPostExecute(String filename) {
            progressBarUrl.setProgress(100);
            statusUrl.setVisibility(View.VISIBLE);
            statusUrl.setText("Finished downloading...");
            File myFile = new File(directory , filename);
            ImageView myImage = (ImageView) findViewById(R.id.progress_bar_image_id);
            myImage.setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }
    }
}