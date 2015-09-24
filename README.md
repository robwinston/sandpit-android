# sandpit-android
Work area for ramping up on Android development.  
Note: if considering the use of this code elsewhere, bear in mind that it's been written by someone brand new to Android development.  As such, it undoubtedly does dumb (or at least non-idiomatic) things in places.  

## Prototyping Toolkit

### Overview

This poor-man's "framework" is designed to facilitate experimentation with the Android SDK.  Multiple Activity classes under investigation can be run from a single Launcher Activity "automatically" by adopting a few simple conventions.  This is made possible by the following:

* Support for dynamically constructing "Switchboard" Activites using a nested approach: 
  - Populate a "Main" screen from a list of java packages described in an XML configuration file - this would be spawned by a given Launchable Activity
  - For each package described in the XML,  populate a "Sub-Main" screen for all classes in a given java package whose names end with `Activity`  (as such, creating a "random" class with a name ending in `Activity` is unlikely to do what one wnats).
  - Both of these dynamically populate a "template" View with what they discover

* This facility relies on a certain amount of configuration by convention -
  - Activites are grouped by the java package they are in - each of which is described in the XML configuration.   This simplifies configuration & facilitates adding additional prototypes to a given package without having to modify the configuration - i.e. they will be picked up automatically (provided the classname ends with Activity). (Presently there is no way to ask that Activites in multiple packages be included on a single "switchboard".)
  - Presumably if repackaged appropriately, these activites could be run on their own.  (The "ends with Activity" filter provides a simple mechanism for excluding "child" activities i.e. ones which rely on being spawned by a "parent" activity.)
  - If there is no entry in the XML config for a given package, it will be ignored entirely.

* To augment use of the debugger during investigation, a simple, somewhat configurable & extensible, Tracer facility is also provided.  
  - Current concrete implementations use Toast or Log for tracing, but the BaseTrace abstract class could be extended in other ways - e.g. file or SQL Lite.
  - These are wrapped in a TraceManager with a simple API called by Activities wishing to employ it.

* To localize references to View elements but reduce the cost of retrieving them, a simple View element cache is provided (and "transparently" plugged in by overriding the findViewById method).  This is undoubtedly overkill for View elements referenced only once, but it demonstates a way to provide this.  In some cases, it provides a useful alternative to instance variables - e.g. when repeatedly updating a ProgressBar.  Whether or not this is really better than instance varibles is certainly debatable, but it was a useful way to explore extension mechanisms.

* Although this framework may have utility in its own right, it's largely been used explore several techniques:
  - Providing capabilty to all Activities through both inheritance and some composition.  Of course composition is generally better than inheritance - it's been used here largely for ease of implementation.  It will be refactored at some point.
  - Dynamically constructing Views from supplied data
  - Overriding provided methods to augment supplied capability
  - Delegating cross-cutting concerns (e.g. Tracing) to a common class

## Further Detail

### "Switchboard" classes

* `MainActivity` - presently, this is simply the Launchable stub used to run the `PackageDispatcherActivity`.  A Trace configuration capability is envisioned - when implemented, it could be exposed here.
* `PackageDispatcherActivity` - reads the `activity_group_config.xml` file & creates an entry in the "switchboard template" for each package described.
* `ButtonDispatchActivity` or `TableDispatchActivity` - iteratively used by the `PackageDispatcherActivity`, passing it a different package name for each instance. 
  - Interrogates the package, and presents the activities for that package. See below for further info on this feature.  
  - The Package, Button, and Table dispatchers all extend a base abstract class `DispatchBaseActivity` which provides common capabiliy.  These are augmented by several utility classes.  These could be combined/extended in other ways.

* Simple helper classes are used to organise the relevant info needed for the facility.  There is room for improvment here, but it gets the job done with minimal ceremony.
  - `ActivityGroup` - used to describe a package: Name, Description, PackageName.
  - `ActivityGroupCollection` - what it sounds like, really a placeholder for fancier stuff if needed.

* The `PackageDispatchActivity` launcher: 
  - Builds the `ActivityGroupCollection` in `getActivityGroups()` (by processing the aforementioned xml config file)
    - TODO: alternatively read the AndroidManifest to obtain this info
  - Processes the collection in `addDispatcherLayout(LinearLayout parentLayout)` to construct "switchboard" entries for each ActivityGroup. Each of these invokes a `[Button|Table]DispatcherActivity` (with package name passed in the intent).
    - Note: Currently hardcoded to use the `TableDispatchActivity`, but this could be made configurable
  - This method uses another helper class `IntentData` to send String-based key/value pairs to the receiving activity.  Presently, all this used for is to send the package name - but it's a placeholder for doing more - e.g. extended to support the various data types passable through intents.
  - Presently, the reciever for each button is a `[Button|Table]DispatcherActivity`, but it could be anything which understand the convention.

* The  `[Button|Table]DispatcherActivity` launcher:
  - Gets the package name passed to it.
  - Uses the `ClassFinder` utility class to find all of the classes in this package whose classnames end with `Activity`.
  - Note: Credit to the following for providing much of what it took to do this  - 
    - [Find all classes in a package in Android](http://stackoverflow.com/questions/15446036/find-all-classes-in-a-package-in-android)
    - [How to get all classes names in a package?](http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package)
 
* `PackageDispatchActivity` & `[Button|Table]DispatcherActivity` all extend `DispatchBaseActivity` which provides common functionality.
* As suggested, it should be possible to develop other "Dispatch" Activities which use different navigation (e.g. menus), but re-use the core capability.  This would likely surface further refactoring  opportunities.

### Base Activity classes

* `TraceBaseActivity` - A  view-less Activity class which provides tracing for lifecycle methods - other activities can extend it to pick up this, or extend `BaseActivity` to use tracing feature for other things (see other activity files for how this is done).  

  - The `setupTrace(AnActivityClass.class.getSimpleName())` in the parent activty's `onCreate()` is what turns on tracing for that activity. 
  - An activity may extend `TraceBaseActivity`, but control whether or not tracing actually takes place by whether or not they invoke setupTrace

* `BaseActivity`

  - A view-less Activity class with miscellaneous utility methods.  `TraceBaseActivity` extends this.
  - Includes a generic method to set a View element's (e.g. Button) click behaviour to dispatch to another activity.  
  - Includes an inner class which implements the Holder pattern to cache View elements in lieu of stashing them in instance variables. Granted this is overkill for items accessed only once in the onCreate() method (but then storing them in instance variables has no benefit either), but it demonstrates the principle.  Nonetheless, for anyone accessing elements repeatedly it's a performant alternative to instance variables.
    - This feature is implemented by overriding the `findViewById(int id)` method, so it is transparent to the consumer. 


### Tracing
A somewhat "pluggable", albeit naive implementation of a simple tracing capability.

* `TraceManager` - Manage a collection of one or more Tracers to support sending trace entries to multiple destinations.  
  - Has a first-cut implementation of a run-time configuration capability and convenience methods to selectively turn tracing on/off other than at the activity setup level but these haven't been exercised  yet.

* `TracerBase` - Core functionality for tracing - format message with time, parent class, and calling method. 
  - Note: "stack depth" is hard coded & used to skip over intermediate helper methods, so it will provide misleading info if used in a different context. Where this is done is commented in the code and is easily modified.  May in future make this genuinely configurable.
  - Note: time is optional - LogTrace turns it off because it's redundant
* `ToastTracer` - Issue trace messages using Toast
* `LogTracer` - Issue trace messages using Log.d

* Toaster...
The Toaster files are helper classes for the implementation of `ToastTracer` tracing facility.  Uses a cache with a settable depth to limit how big the stack of Taost gets - useful when tracing statements trigger a "storm" of trace entries.  When the depth is reached, Toast entries are LIFO cancelled & discarded.  Done largely as a learning exercise to play around with Toast.



## "Prototype" Activities

The other activities are explorations of various capabilites in the SDK.

### ParentActivity / ChildActivity1
* Simple impelentation of exchanging data between Activities.  It uses EditText/TextView in both to demonstrate the exchange (and uses the TraceActivity as well to show what's coming and going). 
* ChildActivity1 doesn't appear on the launcher panel because its classname doesn't end with "Activity".

### ProgressMockActivity / ProgressURLActivity
* Investigation of ProgressBar & ProgressDialog, running background tasks, posting View actions to UI thread. 