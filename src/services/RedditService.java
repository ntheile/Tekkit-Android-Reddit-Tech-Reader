package services;

import java.util.ArrayList;
import java.util.List;

import models.Reddit;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;

import utils.JsonUtils;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EBean
public class RedditService extends IRedditService_ {
	
	@RestService
	IRedditService redditService;

	public List<Reddit> getRedditsList(String after){
	
		List<Reddit> reddits = new ArrayList<Reddit>();
		JsonNode json;
		if (after == null){
			 json = redditService.getRedditsAsJSON();	
		}
		else{
			json = redditService.getRedditsAsJSONPage(after);
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
