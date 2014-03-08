package com.nicktee.redditreader.services;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@Rest(converters = { MappingJacksonHttpMessageConverter.class })
public interface IRedditService  {
	
		@Get("http://www.reddit.com/r/{subreddits}/.json")
		JsonNode getRedditsAsJSON(String subreddits);
		
		@Get("http://www.reddit.com/r/{subreddits}/.json?count=25&after={after}")
		JsonNode getRedditsAsJSONPage(String after, String subreddits);
		
}



