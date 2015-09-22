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

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private String filepath = "MyFileStorage";
    private File directory;
    private TextView finished;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);

        progressBar1 = (ProgressBar) findViewById(R.id.progress_bar_progressBar1_id);
        progressBar1.setVisibility(View.GONE);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar_progressBar2_id);
        progressBar2.setVisibility(View.GONE);
        finished = (TextView) findViewById(R.id.progress_bar_result_id);
        finished.setVisibility(View.GONE);

        Button start = (Button) findViewById(R.id.progress_bar_start_button_id);
        start.setOnClickListener(this);
        Button stop = (Button) findViewById(R.id.progress_bar_stop_button_id);
        stop.setOnClickListener(this);
        Button download = (Button) findViewById(R.id.progress_bar_download_button_id);
        download.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.progress_bar_start_button_id:
                progressBar1.setVisibility(View.VISIBLE);
                break;

            case R.id.progress_bar_stop_button_id:
                progressBar1.setVisibility(View.GONE);
                break;

            case R.id.progress_bar_download_button_id:
                String url =
                // "http://upload.wikimedia.org/wikipedia/commons/0/05/Sna_large.png";  <- this one doesn't have content-length in header, so doesn't work
                        // TODO Understand how to handle download when content-length is missing ...
                // "http://www.comolohago.cl/wp-content/uploads/2013/06/ANDROID.png";
                // "http://4.bp.blogspot.com/-8v_k_fOcfP8/UQIL4ufghBI/AAAAAAAAEDo/9ffRRTM9AnA/s1600/android-robog-alone.png";
                        "http://www.webmastergrade.com/wp-content/uploads/2011/04/Android-VS-Apple.jpg";
                grabURL(url);
                break;

            // More buttons go here (if any) ...

        }
    }

    public void grabURL(String url) {
        new GrabURL().execute(url);
    }

    private class GrabURL extends AsyncTask<String, Integer, String> {


        protected void onPreExecute() {
            progressBar2.setVisibility(View.VISIBLE);
            progressBar2.setProgress(0);
            finished.setVisibility(View.GONE);

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
            finished.setVisibility(View.VISIBLE);
            finished.setText(String.valueOf(progress[0]) + "%");
            progressBar2.setProgress(progress[0]);
        }

        protected void onCancelled() {
            Toast toast = Toast.makeText(getBaseContext(),
                    "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }

        protected void onPostExecute(String filename) {
            progressBar2.setProgress(100);
            finished.setVisibility(View.VISIBLE);
            finished.setText("Finished downloading...");
            File myFile = new File(directory , filename);
            ImageView myImage = (ImageView) findViewById(R.id.progress_bar_image_id);
            myImage.setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }

    }
}