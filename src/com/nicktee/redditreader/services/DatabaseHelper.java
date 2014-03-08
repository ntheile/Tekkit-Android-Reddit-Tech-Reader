package com.nicktee.redditreader.services;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.models.Reddit;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	 // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "RedditDB.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 14;

    // the DAO object we use to access the SimpleData table
    public Dao<Reddit, Integer> redditDao = null;
    public Dao<Prefs, Integer> prefsDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        // Reddits Table
    	try {
    		Log.v("DB", "onCreate");
            TableUtils.createTable(connectionSource, Reddit.class);
            TableUtils.createTable(connectionSource, Prefs.class);
            seed();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
       
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
        	Log.v("DB", "onUpgrade");
        	
            TableUtils.dropTable(connectionSource, Reddit.class, true);
            TableUtils.dropTable(connectionSource, Prefs.class, true);
            onCreate(db, connectionSource);
        	
        	/*List<String> allSql = new ArrayList<String>(); 
            switch(oldVersion) 
            {
              case 1: 
                  //allSql.add("alter table AdData add column `new_col` VARCHAR");
                  //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }*/
        	
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    // seed the prefs by adding default values
    public void seed(){
    	
    	try{
    		Log.v("DB", "Seeding the database");
    		Prefs p = new Prefs();
        	p.setSubReddits("Android");
        	p.setSelected(true);
    		getPrefsDao().create(p);
    		p.setSubReddits("webdev");
    		getPrefsDao().create(p);
    		p.setSubReddits("programmerhumor");
    		getPrefsDao().create(p);
    		p.setSubReddits("singularity");
    		getPrefsDao().create(p);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    

    public Dao<Reddit, Integer> getRedditDao() {
        if (null == redditDao) {
            try {
            	redditDao = getDao(Reddit.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return redditDao;
    }
    
    public Dao<Prefs, Integer> getPrefsDao(){
    	 if (null == prefsDao) {
             try {
             	prefsDao = getDao(Prefs.class);
             }catch (java.sql.SQLException e) {
                 e.printStackTrace();
             }
         }
         return prefsDao;
    }
    
}
