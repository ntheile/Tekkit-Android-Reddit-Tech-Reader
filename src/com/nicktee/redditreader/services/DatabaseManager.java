
package com.nicktee.redditreader.services;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.models.Reddit;

/**
 * @author nicktee
 * 
 * Put all code here that deals with the sqlite database
 * 
 */
public class DatabaseManager {
	
	static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }
    
    
    
    //*****************************************************
    // Prefs
    //*****************************************************

    public List<Prefs> getAllPrefs(){
    	
    	List<Prefs> prefsList = null;
    	try{
    		prefsList = getHelper().getPrefsDao().queryForAll();
    	} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
    	
    	Log.v("Pref Database", prefsList.get(0).toString() );
    	
    	return prefsList;
    }
    
    public void updatePref(Prefs pref){
    	
    	try{
    		getHelper().getPrefsDao().update(pref);
    	}catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
    	
    }
    

    
 
    
    
    //*****************************************************
    // Reddits
    //*****************************************************

    public List<Reddit> getAllReddits() {
        List<Reddit> redditLists = null;
        try {
        	redditLists = getHelper().getRedditDao().queryForAll();
        } catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
        return redditLists;
    }
    
    public void addReddit(Reddit r) {
        try {
            getHelper().getRedditDao().create(r);
        } catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
    }

    public void updateReddits(Reddit r) {
        try {
            getHelper().getRedditDao().update(r);
        } catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void deleteReddits() {
        try {
            getHelper().getRedditDao().deleteBuilder().delete();
        } catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
    }
}
