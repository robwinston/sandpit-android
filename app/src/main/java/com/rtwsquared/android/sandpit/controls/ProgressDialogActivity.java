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

    private static final int SLEEP_TIME = 100;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private int progressDialogStatus = 0;
    private Handler progressDialogHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);
        setupTrace(ProgressDialogActivity.class.getSimpleName());

        addProgressDialogButtonListener();
        addProgressBarButtonListener();
    }

    public void addProgressBarButtonListener() {

        findViewById(R.id.progress_dialog_bar_button_id).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        /*
                        // prepare for a progress dialog
                        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setCancelable(true);
                        progressDialog.setMessage("File downloading ...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
*/

                        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_dialog_progress_bar_id);

                        //reset progress bar status
                        progressBarStatus = 0;
                        //reset file size
                        fileSize = 0;

                        final Thread theThread = getThreadForProgressBar(progressBar);

                        theThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                            @Override
                            public void uncaughtException(Thread thread, Throwable ex) {
                                traceMe("ProgressBar thread exception: " + ex.getMessage());
                            }
                        });


                        //Only allow one at a time ...
                        setProgressStart();

                        progressBar.setVisibility(View.VISIBLE);
                        theThread.start();
                    }
                });
    }

    public void addProgressDialogButtonListener() {

        findViewById(R.id.progress_dialog_button_id).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // prepare for a progress dialog
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

                        theThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                            @Override
                            public void uncaughtException(Thread thread, Throwable ex) {
                                traceMe("ProgressDialog thread exception: " + ex.getMessage());
                            }
                        });


                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                traceMe("Progress dialog cancelled");
                                theThread.interrupt();
                                // re-enable buttons
                                findViewById(R.id.progress_dialog_bar_button_id).setEnabled(true);
                                findViewById(R.id.progress_dialog_button_id).setEnabled(true);
                            }
                        });
                        setProgressStart();


                        progressDialog.show();
                        theThread.start();
                    }
                });
    }

    private void setProgressStart() {
        //Only allow one at a time ...
        setEnabled(R.id.progress_dialog_button_id, false);
        setEnabled(R.id.progress_dialog_bar_button_id, false);
        setTextViewText(R.id.progress_dialog_result, getString(R.string.progress_bar_demo_text_started));
    }

    private void setProgressComplete() {
        //Only allow one at a time ...
        setEnabled(R.id.progress_dialog_button_id, true);
        setEnabled(R.id.progress_dialog_bar_button_id, true);
        setTextViewText(R.id.progress_dialog_result, getString(R.string.progress_bar_demo_text_finished));
    }

    private Thread getThreadForProgressDialog(final ProgressDialog progressDialog) {
        return new Thread(new Runnable() {
            public void run() {
                while (progressDialogStatus < 100) {

                    // process some tasks
                    progressDialogStatus = doSomeTasks();

                    // your computer is too fast, sleep a bit ...
                    try {
                        Thread.sleep(SLEEP_TIME);
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

                // update view ...
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        setProgressComplete();
                    }
                });
            }
        });
    }

    private Thread getThreadForProgressBar(final ProgressBar progressBar) {
        return new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // process some tasks
                    progressBarStatus = doSomeTasks();

                    // your computer is too fast, sleep a bit ...
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                // ok, file is downloaded,
                if (progressBarStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // update view ...
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            progressBar.setProgress(0);
                            setProgressComplete();
                        }
                    });
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
