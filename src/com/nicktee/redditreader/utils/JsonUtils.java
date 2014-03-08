package com.nicktee.redditreader.utils;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.nicktee.redditreader.RedditArrayAdapter;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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
		
		public static boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
		    final boolean[] primitives = new boolean[booleanList.size()];
		    int index = 0;
		    for (Boolean object : booleanList) {
		        primitives[index++] = object;
		    }
		    return primitives;
		}
		
		
}
