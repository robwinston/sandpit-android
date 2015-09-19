package com.rtwsquared.android.util.activity;

/**
 * Created by robw on 17/09/2015.
 * Used to describe a collection of Activities in a given java package
 */
public class ActivityGroup   {

    public ActivityGroup(String name, String description, String packageName)
    {
        this.name = name;
        this.description = description;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Description: %s,  Package name: %s", name, description, packageName);
    }

    private String name;
    private String description;
    private String packageName;

}
