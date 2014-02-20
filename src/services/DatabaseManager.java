package services;

import java.sql.SQLException;
import java.util.List;

import models.Reddit;
import android.content.Context;

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

    public List<Reddit> getAllReddits() {
        List<Reddit> wishLists = null;
        try {
            wishLists = getHelper().getRedditDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishLists;
    }
    
    public void addReddit(Reddit r) {
        try {
            getHelper().getRedditDao().create(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateReddits(Reddit r) {
        try {
            getHelper().getRedditDao().update(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteReddits() {
        try {
            getHelper().getRedditDao().deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
