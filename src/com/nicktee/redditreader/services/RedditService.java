package com.nicktee.redditreader.services;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.j256.ormlite.dao.Dao;
import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.models.Reddit;
import com.nicktee.redditreader.utils.JsonUtils;

@EBean
public class RedditService extends IRedditService_ {
	
	@RestService
	IRedditService redditService;
	
	@Pref
	MyPrefs_ myPrefs;
	
	@OrmLiteDao(helper=DatabaseHelper.class, model=Prefs.class)
	Dao<Prefs, Integer> db;
	
	public List<Reddit> getRedditsList(String after){
		
	
		String subreddits = "Android";
		
		try{
			List<Prefs>prefsList = db.query(db.queryBuilder().where().eq("selected", true).prepare());
			subreddits = "";
			for(Prefs p : prefsList){
				subreddits = subreddits + p.getSubReddits() + "+";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		Log.v("DBt", subreddits);
		
	
		
		List<Reddit> reddits = new ArrayList<Reddit>();
		JsonNode json;
		if (after == null || after == ""){
			 json = redditService.getRedditsAsJSON(subreddits);	
		}
		else{
			json = redditService.getRedditsAsJSONPage(after, subreddits);
		}
		
		
		// Map json to Reddit model
		JsonNode dataNode = json.get("data");
		List<JsonNode> childrenNode = dataNode.get("children").findValues("data");
		Reddit r = new Reddit();
		for (JsonNode c : childrenNode) {
			try {
				r = JsonUtils.defaultMapper().readValue(c,
						new TypeReference<Reddit>() {
						});
				r.setAfter(json.findValue("after").asText());
				reddits.add(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return reddits;
		
	}
	
	
}
