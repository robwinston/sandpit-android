# sandpit-android
Work area for ramping up on Android development.

Has a simple framework for testing activities:
* Utility Views which can look at package inventory & contents and build nested "launcher" Activites for them.
* A simple, somewhat configurable, trace facility which can use Toast and/or Log for tracing.



## Base files

* MainActivity - "top level" launcher, a collection of pointers to DispatchButtonsActivity, one for each java package listed in its configuration (this is currently "baked" into the code - this will be changed at some point). 
* DispatchButtonsActivity - reused by passing it a different package name for each instance. Presents the activities for that package. See below for further info on this feature.   

### Dynamic Launching 
* Support for dynamically constructing "Activity Launcher" Activites using two techniques: 
  - Populate a "Main" Activity from a list of java packages
  - Populate a "Sub-Main" Activity for the Actvities in a given java package  

This facility relies on a certain amount of configuration by convention -
  - Activites are grouped by the java package they are in - in this case, there is a java package for each "capability" under investigation
  - "Launchable" Activity classes are presumed to end with the word Activity - anything else is ignored.  The provides a simple mechanism for excluding "child" activities.

* Simple helper classes are used to organise the relevant info needed for the facility.  There is room for improvment here, but it gets the job done with minimal ceremony.
  - ActivityGroup - used to describe a package: Name, Description, PackageName.
  - ActivityGroupCollection - what it sounds like, really a placeholder for fancier stuff if needed.

* The `MainActivity` launcher: 
  - Manually builds the ActivityGroupCollection in `getActivityGroups()` - opportunity here to be more dynamic, e.g. read the AndroidManifest.
  - Processes the collection in `addDispatcherLayout(LinearLayout parentLayout)` to construct dipatcher buttons & text for each ActivityGroup. Each of these invokes a `ButtonDispatcherActivity` (with package name passed in the intent).
  - This method uses another helper class `IntentData` to send String-based key/value pairs to the receiving activity.  Presently, all this used for is to send the package name - but it's a placeholder for doing more - e.g. extended to support the various data types passable through intents.
  - Presently, the reciever for each button is a `ButtonDispatcherActivity`, but it could be anything which understand the convention.

* The  `ButtonDispatcherActivity` launcher:
  - Gets the package name passed to it.
  - Uses the ClassFinder utility class to find all of the classes in this package whose names end with Activity.
  - Thanks to the following for providing almost all of of what it took to do this  - 
    - [Find all classes in a package in Android](http://stackoverflow.com/questions/15446036/find-all-classes-in-a-package-in-android)
    - [How to get all classes names in a package?](http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package)
  - Uses code similar to that in the MainActivity class (clearly a refactoring opportunity!) to build an entry for each Activity.

* MainActivity & ButtonDispatcherActivity extend DispatchBaseActivity which provides common functionality.
* As suggested, it should be possible to develop other "Dispatch" Activities which use different navigation (e.g. menus), but re-use the core capability.  This would likely surface further refactoring  opportunities.

## Tracing
A somewhat "pluggable", albeit naive implementation of a simple tracing capability.

### TraceManager
Manage a collection of one or more Tracers to support sending trace entries to multiple destinations

### TracerBase
Core functionality for tracing - format mssage with time, parent class, calling method. 

Note: "stack depth" is hard coded, so it will provide misleading info if used in a different context. Where this is done is commented in the code and is easily modified.  May in future make this genuinely configurable.

### ToastTracer
Issue trace messages using Toast

### LogTracer
Issue trace messages using Log.d

### Toaster...
The Toaster files are helper classes for a novice's implementation of a Toast-based tracing facility.  Uses a cache with a settable depth to limit how big the stack of Taost gets - useful when tracing statements trigger a "storm" of trace entries.  When the depth is reached, Toast entries are LIFO cancelled & discarded.  Done largely as a learning exercise to play around with Toast.

### TraceBaseActivity
A  view-less Activity class which provides tracing for lifecycle methods - other activities can extend it to use tracing feature (see other activity files for how this is done).  

* The `setupTrace(AnActivityClass.class.getSimpleName())` in the parent activty's `onCreate()` is what turns on tracing for that activity. 
* An activity may extend TraceActivity, but control whether or not tracing actually takes place by whether or not they invoke setupTrace

There is room for improvement:

* Decouple it from class hierarchy - right now TraceActivity extends Activity, so other base classes are unvailable
* Selectively turn tracing on/off other than at the activity setup level there's some scaffolding for this, but it hasn't been fleshed out yet.

But then, there's likely something far better out there - this is more of a personal exercise to get comfortable with environment & to revive rusty Java skills. 

### BaseActivity

A view-less Activity class with miscellaneous utility methods.  TraceBaseActivity extends this.

* Includes a generic method to set a View element's (e.g. Button) click behaviour to dispatch to another activity.  
* Includes an inner class which implements the Holder pattern to cache View elements in lieu of stashing them in instance variables. Granted this is overkill for items accessed only once in the onCreate() method (but then storing them in instance variables has no benefit either), but it demonstrates the principle.  Nonetheless, for anyone accessing elements repeatedly (e.g. RandomMountains) it's a performant alternative to instance variables.
* This feature is implemented by overriding the `findViewById(int id)` method, so it is transparent to the consumer.  


## Activities

The other activities are explorations of various capabilites in the SDK.
### ParentActivity / ChildActivity1

* Simple impelemtation of exchanging data between Activities.  It uses EditText/TextView in both to demonstrate the exchange (and uses the TraceActivity as well to show what's coming and going). 
* ChildActivity1 doesn't appear on the launcher panel because its classname doesn't end with "Activity".

