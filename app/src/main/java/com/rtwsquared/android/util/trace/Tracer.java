package com.rtwsquared.android.util.trace;

import java.util.List;

/**
 * Created by robw on 19/09/2015.
 * Trace facility interface
 */
public interface Tracer {

    boolean isTraceEnabled();
    void setTraceEnabled(boolean traceEnabled);
    void traceMe(String message);

    // In present implementation, each tracer is of only one type
    // However, this method signature lets things which are managing multiple tracers have a consistent interface
    List<TraceType> getTraceTypes();
}
