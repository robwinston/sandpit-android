package com.rtwsquared.android.util.trace;

import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by robw on 14/09/2015.
 * Implement Toaster as a Queue (LinkedList for now)
 * Has a private variable which sets maximum queue depth
 * This has the effect of controlling how fast Toasts are cancelled when there's a burst of activity
 * This shallower the depth, the faster they get cancelled
 * This is meant to handle the problem of Toasts piling up "too much"
 */
public class ToasterQueue  implements  Toaster {

    public static int DEFAULT_MAXIMUM_DEPTH = 12;
    public static boolean DEFAULT_CANCEL_OTHERS = false;

    public ToasterQueue() {
        toastQueue = new LinkedList<>();
        cancelOthers = DEFAULT_CANCEL_OTHERS;
        queueDepth = DEFAULT_MAXIMUM_DEPTH;
    }

    @Override
    public synchronized int add(Toast toast) {
        while (toastQueue.size() > queueDepth)
        {
            toastQueue.removeFirst().cancel();
        }

        if (cancelOthers) {
            while(!toastQueue.isEmpty())
            {
                toastQueue.removeFirst().cancel();
            }
        }
        toastQueue.add(toast);

        return ++totalRequests;
    }

    @Override
    public synchronized int getTotalRequests() {
        return totalRequests;
    }


    @Override
    public synchronized void setCancelOthers(boolean cancelOthers) {
        this.cancelOthers = cancelOthers;
    }


    @Override
    public synchronized int replace(Toast toast) {
        if (getCurrentDepth() > 0) {
            Toast replaced = toastQueue.removeLast();
            replaced.cancel();
        }
        add(toast);
        return getCurrentDepth();
    }


    @Override
    public synchronized int getCurrentDepth() {
        return toastQueue.size();
    }

    @Override
    public synchronized int getMaximumDepth() {
        return queueDepth;
    }

    @Override
    public synchronized void setDepth(int depth) {
        queueDepth = depth;
    }

    @Override
    public synchronized String getStats() {
        return java.lang.String.format("Total: %d; Current: %d/%d", getTotalRequests(), getCurrentDepth(), getMaximumDepth());
    }


    private int totalRequests;
    private int queueDepth;
    private boolean cancelOthers;
    private LinkedList<Toast> toastQueue;

}
