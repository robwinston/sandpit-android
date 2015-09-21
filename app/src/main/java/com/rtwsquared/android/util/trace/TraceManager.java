package com.rtwsquared.android.util.trace;

/**
 * Created by robw on 19/09/2015.
 * Adds method to selectively trace to one Tracer
 */
public interface TraceManager extends Tracer {
    void traceMe(TraceType traceType, String message);
}
