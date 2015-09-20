package com.rtwsquared.android.sandpit.controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rtwsquared.android.sandpit.R;

public class MainActivity extends Activity implements OnClickListener{

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private String filepath = "MyFileStorage";
    private File directory;
    private TextView finished;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.GONE);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);
        finished = (TextView) findViewById(R.id.textView1);
        finished.setVisibility(View.GONE);

        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start:
                progressBar1.setVisibility(View.VISIBLE);
                break;

            case R.id.stop:
                progressBar1.setVisibility(View.GONE);
                break;

            case R.id.download:
                String url = "http://upload.wikimedia.org/wikipedia/commons/0/05/Sna_large.png";
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
            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            myImage.setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}