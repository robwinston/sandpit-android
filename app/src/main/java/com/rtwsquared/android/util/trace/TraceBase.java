package com.rtwsquared.android.util.trace;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by robw on 16/09/2015.
 * Trace functionality common to various techniques
 */
abstract class TraceBase {
    protected final Context context;
    protected final String parentClass;
    // consider providing a way to set stack depth for flexibility?
    private int stackDepth;


    public TraceBase(Context context, String parentClass)
    {
        this.context = context;
        this.parentClass = parentClass;
        this.stackDepth = 4;
    }

    private final String timeFormat = "HH:mm:ss.SSS";
    private final SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.UK);

    protected String getTime() {
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    protected String getBaseMessage(String callingMethod) {
        // get the StackTraceElement for the calling method & use it to get its name
        return String.format("%s: %s.%s()", getTime(), parentClass, callingMethod);
    }

    protected String getCallingMethod() {
        return new Throwable().fillInStackTrace().getStackTrace()[stackDepth].getMethodName();
    }
}
