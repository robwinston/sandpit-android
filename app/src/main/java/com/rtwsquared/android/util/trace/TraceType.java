package com.rtwsquared.android.util.trace;

import android.content.Context;

import java.lang.reflect.Constructor;

/**
 * Created by robw on 19/09/2015.
 * Used to specify a particular tracing implementation
 */
public enum TraceType {
    Toast("com.rtwsquared.android.util.trace.ToastTracer"),
    Log("com.rtwsquared.android.util.trace.LogTracer");

    TraceType(String classname) {
        try {
            itsClass = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            android.util.Log.d(TAG, String.format("TraceType() : %s %s", e.getMessage(), e.toString()));
        }
    }

    private Class itsClass;
    private final String TAG = "TraceType init";


    Tracer getTracer(Context applicationContext, String parentClass)
    {
        Tracer aTracer;
        try {
            android.util.Log.d(TAG, String.format("TraceType.getTracer : %s ", itsClass.toString()));
            Class classes[] = new Class[] {Context.class, String.class };
            Constructor itsConstructor = itsClass.getConstructor(classes);
            android.util.Log.d(TAG, String.format("TraceType.getTracer : %s ", itsConstructor.toString()));
            aTracer = (Tracer) itsConstructor.newInstance(applicationContext, parentClass);

        } catch (Exception e) {
            android.util.Log.d(TAG, String.format("TraceType.getTracer : %s %s", e.getMessage(), e.toString()));
            return null;
        }
        return aTracer;
    }
}
