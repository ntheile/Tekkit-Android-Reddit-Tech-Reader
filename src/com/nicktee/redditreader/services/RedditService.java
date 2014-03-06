package com.nicktee.redditreader.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.rest.RestService;
import com.nicktee.redditreader.models.Reddit;
import com.nicktee.redditreader.utils.JsonUtils;

@EBean
public class RedditService extends IRedditService_ {
	
	@RestService
	IRedditService redditService;

	public List<Reddit> getRedditsList(String after){
	
		List<Reddit> reddits = new ArrayList<Reddit>();
		JsonNode json;
		if (after == null || after == ""){
			 json = redditService.getRedditsAsJSON();	
		}
		else{
			json = redditService.getRedditsAsJSONPage(after);
		}
		
		/*try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
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
