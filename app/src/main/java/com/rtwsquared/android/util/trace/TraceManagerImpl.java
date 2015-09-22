package com.rtwsquared.android.util.trace;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robw on 19/09/2015.
 * Use to manage a collection of Tracers
 * Provides consumer with the ability to trace to multiple destinations
 * Presently this isn't a singleton because the assumption is there is one per activity, not one per JVM
 */
public class TraceManagerImpl implements TraceManager {

    private final List<Tracer> tracers;
    private final HashMap<TraceType, List<Tracer>> tracersByType;
    private final List<TraceType> traceTypes;

    public TraceManagerImpl(Context applicationContext, String parentClass, List<TraceType> traceTypes) {
        this.traceTypes = traceTypes;
        tracers = populateTracers(traceTypes, applicationContext, parentClass);
        tracersByType = populateTracersByType(tracers);

        setTraceEnabled(true);
    }

    @Override
    public boolean isTraceEnabled() {
        for (Tracer tracer : tracers) {
            if (tracer.isTraceEnabled())
                return true;
        }
        return false;
    }

    @Override
    public void setTraceEnabled(boolean traceEnabled) {
        for (Tracer tracer : tracers) tracer.setTraceEnabled(traceEnabled);
    }

    @Override
    public void traceMe(String message) {
        for (Tracer tracer : tracers) {
            if (tracer != null && tracer.isTraceEnabled())
                tracer.traceMe(message);
        }
    }

    @Override
    public List<TraceType> getTraceTypes() {
        return traceTypes;
    }

    @Override
    public void traceMe(TraceType traceType, String message) {
        if (isTraceEnabled()) {
            for (Tracer tracer : tracersByType.get(traceType)) {
                if (tracer != null)
                    tracer.traceMe(message);
            }
        }
    }


    private List<Tracer> populateTracers(List<TraceType> traceTypes, Context applicationContext, String parentClass) {
        List<Tracer> theTracers = new ArrayList<>();
        for (TraceType traceType : traceTypes) {
            Tracer tracer = traceType.getTracer(applicationContext, parentClass);
            Log.d("MyTrace", "TraceManagerImpl.populateTracers: " + (tracer == null ? "Null tracer!" : tracer.getClass().getCanonicalName()));
            theTracers.add(tracer);
        }
        return theTracers;
    }


    private HashMap<TraceType, List<Tracer>> populateTracersByType(List<Tracer> tracers) {

        HashMap<TraceType, List<Tracer>> theTracersByType = new HashMap<>();
        for (TraceType tt : Arrays.asList(TraceType.values())) {
            theTracersByType.put(tt, new ArrayList<Tracer>());
        }

        for (Tracer tracer : tracers) {
            if (tracer != null) {
                for (TraceType aTraceType : tracer.getTraceTypes()) {
                    theTracersByType.get(aTraceType).add(tracer);
                }
            }
        }
        return theTracersByType;
    }
}

