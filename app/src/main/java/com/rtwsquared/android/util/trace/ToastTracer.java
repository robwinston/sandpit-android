package com.rtwsquared.android.util.trace;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by robw on 15/09/2015.
 * Wrapper class for Toast tracing facility
 */
public class ToastTracer extends TraceBase implements Tracer {

    private Toaster toaster;

    public ToastTracer(Context context, String parentClass)
    {
        super(context, parentClass);
        toaster = new ToasterQueue();
    }

    public void traceMe(String message)
    {
        String callingMethod = getCallingMethod();
        String baseMessage = getBaseMessage(callingMethod);
        Toast toast = getToast();
        int requests = toaster.add(toast);
        if (message == null || message.isEmpty())
            toast.setText(String.format("%s (%d)", baseMessage, requests));
        else
            toast.setText(String.format("%s (%d) ==> %s", baseMessage, requests, message));
        toast.show();
    }

    private Toast getToast() {
        return Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }
}
