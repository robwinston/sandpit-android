package com.rtwsquared.android.util.activity;


/**
 * Created by robw on 17/09/2015.
 * For passing data to be put in an intent ...
 * Really should just be a tuple for whatever (because Java7 itself doesn't have them?)
 * In any event it should be as general as what an intent will accept
 * ... but doing MVP just now
 */
public class IntentData<K, D> {

    private final K key;
    private final D data;

    public IntentData(K key, D data)
    {
        this.key = key;
        this.data = data;
    }

    public K getKey() {
        return key;
    }

    public D getData() {
        return data;
    }
}
