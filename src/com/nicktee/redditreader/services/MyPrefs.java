package com.nicktee.redditreader.services;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface MyPrefs {
	
	@DefaultString("Android")
	String subreddit();
	
	@DefaultString("webdev")
	String subreddit2();

}
