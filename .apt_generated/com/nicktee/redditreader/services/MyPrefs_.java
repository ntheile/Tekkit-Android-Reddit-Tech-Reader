//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.nicktee.redditreader.services;

import android.content.Context;
import android.content.SharedPreferences;
import com.googlecode.androidannotations.api.sharedpreferences.EditorHelper;
import com.googlecode.androidannotations.api.sharedpreferences.SharedPreferencesHelper;
import com.googlecode.androidannotations.api.sharedpreferences.StringPrefEditorField;
import com.googlecode.androidannotations.api.sharedpreferences.StringPrefField;

public final class MyPrefs_
    extends SharedPreferencesHelper
{


    public MyPrefs_(Context context) {
        super(context.getSharedPreferences((getLocalClassName(context)+"_MyPrefs"), 0));
    }

    private static String getLocalClassName(Context context) {
        String packageName = context.getPackageName();
        String className = context.getClass().getName();
        int packageLen = packageName.length();
        if (((!className.startsWith(packageName))||(className.length()<= packageLen))||(className.charAt(packageLen)!= '.')) {
            return className;
        }
        return className.substring((packageLen + 1));
    }

    public MyPrefs_.MyPrefsEditor_ edit() {
        return new MyPrefs_.MyPrefsEditor_(getSharedPreferences());
    }

    public StringPrefField subreddit() {
        return stringField("subreddit", "Android");
    }

    public StringPrefField subreddit2() {
        return stringField("subreddit2", "webdev");
    }

    public final static class MyPrefsEditor_
        extends EditorHelper<MyPrefs_.MyPrefsEditor_>
    {


        MyPrefsEditor_(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        public StringPrefEditorField<MyPrefs_.MyPrefsEditor_> subreddit() {
            return stringField("subreddit");
        }

        public StringPrefEditorField<MyPrefs_.MyPrefsEditor_> subreddit2() {
            return stringField("subreddit2");
        }

    }

}