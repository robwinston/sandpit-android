package com.rtwsquared.android.sandpit.controls;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.activity.TraceBaseActivity;

public class ProgressMockActivity extends TraceBaseActivity {

    private static final int SLEEP_TIME = 50;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private Thread progressBarThread = null;

    private int progressDialogStatus = 0;
    private Handler progressDialogHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_mock);
        setupTrace(ProgressMockActivity.class.getSimpleName());

        addProgressDialogButtonListener();
        addProgressBarButtonListener();
        addProgressCancelButtonListener();
        setEnabled(R.id.progress_mock_cancel_button_id, false);
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.progress_mock_cancel_button_id).callOnClick();
        super.onBackPressed();
    }

    public void addProgressCancelButtonListener() {

        findViewById(R.id.progress_mock_cancel_button_id).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_mock_progress_bar_id);

                        //reset progress bar status
                        progressBarStatus = 0;
                        progressBar.setProgress(progressBarStatus);

                        //reset file size
                        fileSize = 0;

                        if (progressBarThread != null)
                            progressBarThread.interrupt();

                        setProgressCancelled();
                    }
                });
    }

    public void addProgressDialogButtonListener() {

        findViewById(R.id.progress_mock_button_id).setOnClickListener(
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
                                setProgressCancelled();
                            }
                        });

                        setProgressStart();
                        progressDialog.show();
                        theThread.start();
                    }
                });
    }

    public void addProgressBarButtonListener() {

        findViewById(R.id.progress_mock_bar_button_id).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_mock_progress_bar_id);

                        //reset progress bar status
                        progressBarStatus = 0;
                        //reset file size
                        fileSize = 0;

                        progressBarThread = getThreadForProgressBar(progressBar);

                        progressBarThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                            @Override
                            public void uncaughtException(Thread thread, Throwable ex) {
                                traceMe("ProgressBar thread exception: " + ex.getMessage());
                                setProgressComplete();
                            }
                        });

                        setProgressStart();
                        progressBar.setVisibility(View.VISIBLE);
                        progressBarThread.start();
                    }
                });
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
                        break;
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

    // yes, these methods are trivial & similar
    // but keeping them to make consuming code easier to read
    private void setProgressCancelled() {
        setEnabled(R.id.progress_mock_bar_button_id, true);
        setEnabled(R.id.progress_mock_button_id, true);
        setEnabled(R.id.progress_mock_cancel_button_id, false);
        setTextViewText(R.id.progress_mock_result, getString(R.string.progress_bar_demo_text_cancelled));
    }

    private void setProgressStart() {
        //Only allow one at a time ...
        setEnabled(R.id.progress_mock_button_id, false);
        setEnabled(R.id.progress_mock_bar_button_id, false);
        setEnabled(R.id.progress_mock_cancel_button_id, true);
        setTextViewText(R.id.progress_mock_result, getString(R.string.progress_bar_demo_text_started));
    }

    private void setProgressComplete() {
        setEnabled(R.id.progress_mock_button_id, true);
        setEnabled(R.id.progress_mock_bar_button_id, true);
        setEnabled(R.id.progress_mock_cancel_button_id, false);
        setTextViewText(R.id.progress_mock_result, getString(R.string.progress_bar_demo_text_finished));
    }


    private int fileSize = 0;
    // a really simple file download simulator...
    public int doSomeTasks() {
        while (fileSize <= 1000000) {
            fileSize++;
            if (fileSize % 10000 == 0)
                return fileSize / 10000;
        }
        return 100;
    }
}
