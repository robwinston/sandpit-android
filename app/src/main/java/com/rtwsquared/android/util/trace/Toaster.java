package com.rtwsquared.android.util.trace;

import android.widget.Toast;

/**
 * Created by robw on 15/09/2015.
 * Public behaviour of a Toaster
 */
public interface Toaster {
    /**
     * @param toast - TOAST to be added to Toaster
     * @return total number of elements added over lifetime (not current depth - same as getTotalRequests())
     */
    int add(Toast toast);

    /**
     * Replaces the last TOAST added with this one - handy when tracing inside a loop or event handler
     * @param toast - TOAST to be added to Toaster
     * @return total number of elements added over lifetime (not current depth - same as getTotalRequests())
     */
    int replace(Toast toast);

    /**
     * @return total - number of elements added over lifetime (not current depth)
     */
    int getTotalRequests();

    /**
     * @param cancelOthers - toggle whether or not adding an element to the stack, throws away what's already there
     */
    void setCancelOthers(boolean cancelOthers);


    /**
     * @return - return current number of entries
     */
    int getCurrentDepth();

    /**
     * @return - return maximum number of entries
     */
    int getMaximumDepth();


    /**
     * @param depth - set max cache depth, older TOAST will be cancelled & removed
     */
    void setDepth(int depth);

    /**
     * @return String - return formatted stats - getTotalRequests(); getCurrentDepth()/getMaximumDepth())
     */
    String getStats();
}
