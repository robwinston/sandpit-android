package com.rtwsquared.android.util.activity;

/**
 * Created by robw on 17/09/2015.
 * For passing data to be put in an intent ...
 * Really should just be a tuple for whatever (because Java7 itself doesn't have them?)
 * In any event it should be as general as what an intent will accept
 * ... but doing MVP just now
 */
public class IntentData {

    private final String key;
    private String data;

    public IntentData(String key, String data)
    {
        this.key = key;
        this.data = data;
    }

    public String getKey() { return key;}
    public String getData() {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
