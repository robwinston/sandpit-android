package com.rtwsquared.android.util.trace;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by robw on 15/09/2015.
 * Wrapper class for Toast tracing facility
 */
public class LogTracer extends TraceBase implements Tracer {

    public LogTracer(Context context, String parentClass)
    {
        super(context, parentClass);
    }

    public void traceMe(String message)
    {
        final String tag = "Tracer";
        String callingMethod = getCallingMethod();
        String baseMessage = getBaseMessage(callingMethod);
        if (message == null || message.equals(""))
            Log.d(tag, String.format("%s", baseMessage));
        else
            Log.d(tag, String.format("%s ==> %s", baseMessage, message));
    }
}
