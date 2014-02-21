package services;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(converters = { MappingJacksonHttpMessageConverter.class })
public interface IRedditService {
	// /r/singularity , /r/programmerhumor , /r/webdev , /r/programming
	
		@Get("http://www.reddit.com/r/Android/.json")
		JsonNode getRedditsAsJSON();
		
		@Get("http://www.reddit.com/r/Android/.json?count=25&after={after}")
		JsonNode getRedditsAsJSONPage(String after);
		
		
}
