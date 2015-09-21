package com.rtwsquared.android.util.trace;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by robw on 15/09/2015.
 * Wrapper class for TOAST tracing facility
 */
public class LogTracer extends TraceBase implements Tracer {

    public static final String TAG = "Tracer";

    public LogTracer(Context context, String parentClass)
    {
        super(context, parentClass);
    }

    public void traceMe(String message)
    {
        String callingMethod = getCallingMethod();
        String baseMessage = getBaseMessage(callingMethod);
        if (message == null || message.equals(""))
            Log.d(TAG, String.format("%s", baseMessage));
        else
            Log.d(TAG, String.format("%s ==> %s", baseMessage, message));
    }

    @Override
    public List<TraceType> getTraceTypes() {
        return Collections.singletonList(TraceType.LOG);
    }
}
