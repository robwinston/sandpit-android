package com.rtwsquared.android.util.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rtwsquared.android.sandpit.R;
import com.rtwsquared.android.util.trace.TraceManager;
import com.rtwsquared.android.util.trace.TraceManagerImpl;
import com.rtwsquared.android.util.trace.TraceType;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity implements TraceManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
    }



    static List<TraceType> defaultTraceTypes = new ArrayList<TraceType>();
    static {
        defaultTraceTypes.add(TraceType.Log);
        defaultTraceTypes.add(TraceType.Toast);    }

    TraceManagerImpl traceManager;
    protected void setupTrace(String parentClass, List<TraceType> traceTypes) {
        traceManager = new TraceManagerImpl(getApplicationContext(), parentClass, traceTypes);
    }

    protected void setupTrace(String parentClass) {
        traceManager = new TraceManagerImpl(getApplicationContext(), parentClass, defaultTraceTypes);
    }

    @Override
    public void traceMe(String message) {
        if (traceManager != null)
            traceManager.traceMe(message);
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
