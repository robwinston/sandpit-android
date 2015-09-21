package com.rtwsquared.android.util.trace;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robw on 19/09/2015.
 * Use to manage a collection of Tracers
 * Provides consumer with the ability to trace to multiple destinations
 * Presently this isn't a singleton because the assumption is there is one per activity, not one per JVM
 */
public class TraceManagerImpl implements TraceManager {

    private final List<Tracer> tracers;

    public TraceManagerImpl(Context applicationContext, String parentClass, List<TraceType> traceTypes) {
        tracers = new ArrayList<>();
        populateTracers(traceTypes, applicationContext, parentClass);
        setTraceEnabled(true);
    }

    private boolean traceEnabled;
    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    @Override
    public void traceMe(String message) {
        if (isTraceEnabled()) {
            for (Tracer tracer : tracers) {
                if (tracer != null)
                    tracer.traceMe(message);
            }
        }
    }

    private void populateTracers(List<TraceType> traceTypes, Context applicationContext, String parentClass) {
        for (TraceType traceType : traceTypes) {
            Tracer tracer = traceType.getTracer(applicationContext, parentClass);
            Log.d("MyTrace", "TraceManagerImpl.populateTracers: " + (tracer == null ? "Null tracer!" : tracer.getClass().getCanonicalName()));
            tracers.add(tracer);
        }
    }
}

