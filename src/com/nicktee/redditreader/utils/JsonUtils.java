package com.nicktee.redditreader.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

public class JsonUtils {
	// this helper method can be used to make JSON parsing a one-line operation
		public static <T> T parseJson(JsonNode node, Class<T> class_ ) {
		    try {
		        return JsonUtils.defaultMapper().treeToValue(node, class_);
		    } catch (IOException e) {
		        Log.e("JsonUtilException: ", "Failed to parse JSON entity " + class_.getSimpleName(), e);
		        throw new RuntimeException(e);
		    }
		}

		// re-use a single ObjectMapper so we're not creating multiple object mappers
		private static ObjectMapper sObjectMapper = new ObjectMapper();
		public static ObjectMapper defaultMapper() {
		    return sObjectMapper;
		}
}
