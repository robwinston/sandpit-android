package com.rtwsquared.android.util.activity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by robw on 17/09/2015.
 * A collection of ActivityGroup
 */
public class ActivityGroupCollection  implements Iterable<ActivityGroup> {


    private List<ActivityGroup> activityGroups;

    public ActivityGroupCollection() {
        activityGroups = new ArrayList<>();
    }

    public void addActivityGroup(ActivityGroup activityGroup)
    {
        activityGroups.add(activityGroup);
    }

    public ActivityGroup getActivityGroup(String name) {
        //  These collections should never be very big ...
        for (ActivityGroup ag : activityGroups) {
            if (ag.getName().equals(name))
                return ag;
        }
        return null;
    }

    public boolean containsActivityGroup(String name) {
        //  These collections should never be very big ...
        for (ActivityGroup ag : activityGroups) {
            if (ag.getName().equals(name))
                return true;
        }
        return false;
    }

    public int size() {
        return activityGroups.size();
    }

    @Override
    public String toString() {
        return activityGroups.toString();
    }

    @Override
    public Iterator<ActivityGroup> iterator() {
        return activityGroups.iterator();
    }
}
