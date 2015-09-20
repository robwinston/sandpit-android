package com.rtwsquared.android.util.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Original code created by sp00m on 20/03/2013.
 * Adapted to this use by rob on 15/09/2015
 */
public class ClassFinder {


    //TODO refactor ... cries out for a functional approach!
    public static List<Class<?>>  getClassesOfPackage(String packageName, String packageCodePath)   {
        ArrayList<Class<?>>  classes = new ArrayList();
            Enumeration<String> entries = getClassNames(packageCodePath);
            while (entries != null && entries.hasMoreElements()) {
                String className = entries.nextElement();
                // This relies on the convention that activity class names end with Activity
                // Could look at base type instead ... but this provides an easy way to exclude activities
                // which aren't "launchable"
                if (className.contains(packageName)  && className.endsWith("Activity")) {
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException ignore) {}
                }
            }
        return classes;
    }

    // this does seem to work - so method above uses technique, to behave like the method further below ...
    // http://stackoverflow.com/questions/15446036/find-all-classes-in-a-package-in-android
    public static String[] getClassnamesOfPackage(String packageName, String packageCodePath) {
        ArrayList<String> classes = new ArrayList<String>();
        Enumeration<String> entries = getClassNames(packageCodePath);
        while (entries != null && entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.contains(packageName)) {
                    classes.add(className.substring(className.lastIndexOf(".") + 1, className.length()));
                }
            }
        return classes.toArray(new String[classes.size()]);
    }


    private static Enumeration<String> getClassNames(String packageCodePath)  {
        try {
            DexFile df = new DexFile(packageCodePath);
            return df.entries();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Minimal SDK level doesn't allow this ...
        //Collections.<String>emptyEnumeration();
        return null;
    }


    // TODO Understand why this doesn't work
    /*
    * From http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package
    * this doesn't seem to work in Android, but may be my ignorance
    * retaining it for now to remind me to revisit

    // Usage: List<Class<?>> classes = ClassFinder.find("com.package");

    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    public static List<Class<?>> getClassesOfPackage(String scannedPackage) {
        String scannedPath = scannedPackage.replace(DOT, SLASH);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        // Not working ... tried this, it didn't work either ...
        //URL scannedUrl = Thread.currentThread().getContextClassLoader().getParent().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }


    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
    */

}