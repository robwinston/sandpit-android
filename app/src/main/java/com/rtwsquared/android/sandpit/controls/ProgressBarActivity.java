package com.rtwsquared.android.sandpit.controls;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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

    private String[] imageURLs;
    private File directory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        setupTrace(ProgressBarActivity.class.getSimpleName());

        imageURLs = getImageURLsFromConfig();

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        directory = contextWrapper.getDir("MyFileStorage", Context.MODE_PRIVATE);

        setVisibility(R.id.progress_bar_url_progressBar_id, View.GONE);
        findViewById(R.id.progress_bar_url_result_id).setVisibility(View.GONE);

        findViewById(R.id.progress_bar_url_download_button_id).setOnClickListener(this);

        findViewById(R.id.progress_bar_demo_start_button_id).setOnClickListener(this);
        findViewById(R.id.progress_bar_demo_stop_button_id).setOnClickListener(this);
        findViewById(R.id.progress_bar_demo_progress_bar_id).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.progress_bar_demo_start_button_id:
                findViewById(R.id.progress_bar_demo_progress_bar_id).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.progress_bar_demo_result_id))
                        .setText(getString(R.string.progress_bar_demo_text_started));
                break;

            case R.id.progress_bar_demo_stop_button_id:
                setVisibility(R.id.progress_bar_demo_progress_bar_id, View.GONE);
                ((TextView) findViewById(R.id.progress_bar_demo_result_id))
                        .setText(getString(R.string.progress_bar_demo_text_stopped));
                break;

            case R.id.progress_bar_url_download_button_id:
                grabURL(imageURLs[getURLIndex()]);
                break;

            // More buttons go here (if any) ...

        }
    }

    /*
    @Override
    public void onBackPressed() {

        if (grabURL != null) {
            grabURL.cancel(true);
            traceMe("URL download cancelled");
        }

        super.onBackPressed();
    }

    */

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


    private GrabURL grabURL;
    public void grabURL(String url) {
        grabURL = new GrabURL();
        grabURL.execute(url);
    }
    private class GrabURL extends AsyncTask<String, Integer, String> {


        protected void onPreExecute() {
            setVisibility(R.id.progress_bar_url_result_id, View.VISIBLE);
            setVisibility(R.id.progress_bar_url_progressBar_id, View.VISIBLE);
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
            setVisibility(R.id.progress_bar_url_result_id, View.VISIBLE);
            setTextViewText(R.id.progress_bar_url_result_id, String.valueOf(progress[0]) + "%");

            setVisibility(R.id.progress_bar_url_progressBar_id, View.VISIBLE);
            ((ProgressBar) findViewById(R.id.progress_bar_url_progressBar_id)).setProgress(progress[0]);
        }

        protected void onCancelled() {
            Toast toast = Toast.makeText(getBaseContext(), "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

        protected void onPostExecute(String filename) {
            ((ProgressBar) findViewById(R.id.progress_bar_url_progressBar_id)).setProgress(100);
            setVisibility(R.id.progress_bar_url_result_id, View.VISIBLE);
            setTextViewText(R.id.progress_bar_url_result_id, "Finished downloading...");
            File myFile = new File(directory , filename);
            ((ImageView) findViewById(R.id.progress_bar_image_id)).setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }
    }
}