package com.rtwsquared.android.util.trace;

/**
 * Created by robw on 19/09/2015.
 * Adds trace on/off behaviour
 */
public interface TraceManager extends Tracer {
    boolean isTraceEnabled();
    void setTraceEnabled(boolean traceEnabled);
}
