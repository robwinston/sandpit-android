package com.rtwsquared.android.sandpit.controls;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class ProgressDialogActivity extends TraceBaseActivity {

    private int progressDialogStatus = 0;
    private Handler progressDialogHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);
        setupTrace(ProgressDialogActivity.class.getSimpleName());

        addProgressDialogButtonListener();
    }

    public void addProgressDialogButtonListener() {

        findViewById(R.id.progress_dialog_button_id).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // prepare for a progress bar dialog
                        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setCancelable(true);
                        progressDialog.setMessage("File downloading ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);

                        //reset progress bar status
                        progressDialogStatus = 0;
                        //reset file size
                        fileSize = 0;

                        final Thread theThread = getThreadForProgressDialog(progressDialog);
                        theThread.start();

                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                traceMe("Progress dialog cancelled");
                                theThread.interrupt();

                            }
                        });
                        progressDialog.show();
                    }
                });
    }



    private Thread getThreadForProgressDialog(final ProgressDialog progressDialog) {
        return new Thread(new Runnable() {
            public void run() {
                while (progressDialogStatus < 100) {

                    // process some tasks
                    progressDialogStatus = doSomeTasks();

                    // your computer is too fast, sleep 1/3 second
                    try {
                        Thread.sleep(333);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    progressDialogHandler.post(new Runnable() {
                        public void run() {
                            progressDialog.setProgress(progressDialogStatus);
                        }
                    });
                }

                // ok, file is downloaded,
                if (progressDialogStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog
                    progressDialog.dismiss();
                }
            }
        });
    }


    private int fileSize = 0;

    // file download simulator... a really simple
    public int doSomeTasks() {

        while (fileSize <= 1000000) {
            fileSize++;
            if (fileSize % 10000 == 0)
                return fileSize / 10000;
        }

        return 100;
    }
}
