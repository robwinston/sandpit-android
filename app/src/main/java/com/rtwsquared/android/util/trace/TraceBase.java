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

    public TraceBase(Context context, String parentClass)
    {
        this.context = context;
        this.parentClass = parentClass;
        setTraceEnabled(true);
    }

    private boolean traceEnabled;
    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    private final String timeFormat = "HH:mm:ss.SSS";
    private final SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.UK);

    private boolean includeTime = true;
    public boolean isTimeEnabled() {
        return includeTime;
    }
    protected void setTimeEnabled(boolean includeTime) {
        this.includeTime = includeTime;
    }


    protected String getTime() {
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    protected String getBaseMessage(String callingMethod) {
        // get the StackTraceElement for the calling method & use it to get its name
        return isTimeEnabled() ?
                String.format("%s: %s.%s()", getTime(), parentClass, callingMethod) :
                String.format("%s.%s()", parentClass, callingMethod);
    }

    // This value backs us through the call chain that gets us here
    // So trace will show the method name we're trying to trace, instead of an intermediate method on its way here
    // Currently the chain is SomeActivity.someMethod ->  BaseActivity.traceMe -> TraceManager.traceMe -> Tracer.traceMe -> getCallingMethod
    // Hence the setting is 4
    private static final int STACK_DEPTH = 4;
    protected String getCallingMethod() {
        return new Throwable().fillInStackTrace().getStackTrace()[STACK_DEPTH].getMethodName();
    }
}
