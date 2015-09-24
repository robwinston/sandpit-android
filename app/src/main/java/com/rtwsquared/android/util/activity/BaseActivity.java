package com.rtwsquared.android.util.activity;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rtwsquared.android.util.trace.TraceManager;
import com.rtwsquared.android.util.trace.TraceManagerImpl;
import com.rtwsquared.android.util.trace.TraceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BaseActivity extends Activity implements TraceManager {

    private BaseActivity.Holder holder = new BaseActivity.Holder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View findViewById(int id) {
        return holder.findView(id);
    }


    // Convenience methods - simple wrappers for some API calls to make calling code somewhat less verbose
    // (and to provide rudimentary error handling)

    private static List<Integer> visibilites =
            Arrays.asList(View.VISIBLE, View.INVISIBLE, View.GONE);

    /**
     * Set the visibility of a given View element id to the desired state
     * Issues trace entry but does nothing if passed a bogus id or visibility value
     * @param id - resource id
     * @param visibility - one of (View.VISIBLE, View.INVISIBLE, View.GONE)
     */
    public void setVisibility(int id, int visibility)
    {

        View aView = findViewById(id);
        if (aView == null)
            traceMe("Failed to find view for id: " + id);
        else if (visibilites.contains(visibility))
            aView.setVisibility(visibility);
        else
            traceMe("Invalid Visibility setting: " + visibility);
    }

    /**
     * Set the text of a given TextView element id to the supplied value
     * Issues trace entry but does nothing if passed a bogus id
     * @param id - resource id
     * @param text - desired text
     */
    public void setTextViewText(int id, String text) {
        TextView aTextView = (TextView) findViewById(id);
        if (aTextView == null)
            traceMe("Failed to find view for id: " + id);
        else
        {
            aTextView.setText(text);
            setVisibility(id, View.VISIBLE);
        }
    }

    public void setEnabled(int id, boolean state)
    {
        findViewById(id).setEnabled(state);
    }

    // Misc
    protected Class classForName(String className)
    {
        Class theClass =  null;

        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            traceMe(String.format("Failed to find Class for name: %s - %s", className, e.getMessage()));
            return theClass;
        }
        return theClass;
    }

    // View element caching section
    protected void traceCacheStats()
    {
        traceMe(getCacheStats());
    }

    protected String getCacheStats()
    {
        return holder.stats();
    }

    class Holder {

        HashMap<Integer, View> viewCache = new HashMap<>();
        int hits = 0;

        boolean contains(int viewId)
        {
            return viewCache.containsKey(viewId);
        }

        String stats()
        {
            return String.format("View Cache: %d hits, %d entries", hits, viewCache.size());
        }

        View findView(int viewId)
        {
            View viewElement;

            if (contains(viewId)) {
                viewElement = viewCache.get(viewId);
                hits++;
            }
            else
            {
                viewElement = BaseActivity.super.findViewById(viewId);
                if (viewElement == null)
                    traceMe(String.format("View %d not found", viewId));
                else {
                    viewCache.put(viewId, viewElement);
                }
            }
            return viewElement;
        }
    }


    // Tracing section
    static final List<TraceType> DEFAULT_TRACE_TYPES = new ArrayList<TraceType>();
    static {
        DEFAULT_TRACE_TYPES.add(TraceType.LOG);
        //DEFAULT_TRACE_TYPES.add(TraceType.TOAST);
    }

    TraceManagerImpl traceManager;
    protected void setupTrace(String parentClass, List<TraceType> traceTypes) {
        traceManager = new TraceManagerImpl(getApplicationContext(), parentClass, traceTypes);
    }

    protected void setupTrace(String parentClass) {
        traceManager = new TraceManagerImpl(getApplicationContext(), parentClass, DEFAULT_TRACE_TYPES);
    }

    @Override
    public void traceMe(String message) {
        if (traceManager != null)
            traceManager.traceMe(message);
    }

    @Override
    public List<TraceType> getTraceTypes() {
        return (traceManager == null) ? null : traceManager.getTraceTypes();
    }


    @Override
    public void traceMe(TraceType traceType, String message) {
        if (traceManager != null)
            traceManager.traceMe(traceType, message);
    }

    @Override
    public boolean isTraceEnabled() {
        return traceManager != null && traceManager.isTraceEnabled();
    }

    @Override
    public void setTraceEnabled(boolean traceEnabled) {
        if (traceManager != null)
            traceManager.setTraceEnabled(traceEnabled);
    }
}
