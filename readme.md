Tekkit Reddit Tech Reader
=======================
Reader for Reddit including the following subreddits (more to come):  Ported to use ActionBarCompat

* /r/Android

![Screenshot](/assets/reddit_reader.png)

Table of Contents
------------------
* [Frameworks Used](#frameworks-used)
* [Code Organization](#code-organization)
* [Architecture](#architecture)
* [Reddit Model Ormlite and Json](#reddit-model-ormlite-and-json)
* [Code Diet](#code-diet)
* [Simple Rest Service for Reddit](#simple-rest-service-for-reddit)
* [Dependency Injection using @EBean](#dependency-injection-using-ebean)
* [Adapters](#adapters)

Frameworks Used
----------------
* `Android Annotations` - Puts your code on a diet, reduces boilerplate code using annotations, lightweight dependecy injection, easy REST, easy Async Tasks
* `Spring` - Used for REST calls
* `ActionBarCompat` - Makes the action bar work in older versions of Android
* `Picaso` - Image loader and cacher from Square
* `Ormlite` - Sqlite Object Relational Mapper to map reddit objects into sql for caching.


Code Organization
------------------
Package Structure

* `com.nicktee.kick` - this is where activies and adapter code goes, basically any code dealing with the view
* `models` - This is where the raw models go represting out object, in this case a Reddit object
* `services` - This is where all the services go, in this case a RedditService that does the REST calls to get 
Reddit articles. The DatabaseManager and DataBaseHelper  coordinate the calls to the sqlite database.

Architecture
--------------
The app loads cached Reddits from the sqlite database. In the background a new list of Reddits from the web are fetched. Once the new reedits are fetched they are displayed on the 
listview adapter and the the sqlite database is updated with the new Reddits.

Reddit Model Ormlite and Json
-----------------------------
This is the Reddit model. Notice we use Jackson for json serialization using various @Json* annotations and 
ormlite @Database* annatations to allow our model to be put into a sqlite database. There are a DatabaseHelper and DatabaseManager classes
to help coordinate saving items to the sqlite database. This article explains this in depth: http://logic-explained.blogspot.com/2011/12/using-ormlite-in-android-projects.html

`Reddit.java`
```java
package models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "Reddit")
public class Reddit {

	// constructor
	public Reddit() {
		super();
	}

	@DatabaseField(generatedId = true)
	@JsonIgnore
	private int id;

	@DatabaseField
	@JsonProperty("url")
	private String url;

	@DatabaseField
	@JsonProperty("selftext")
	private String selftext;

	@DatabaseField
	@JsonProperty("thumbnail")
	private String thumbnail;

	@DatabaseField
	@JsonProperty("author_flair_text")
	private String author_flair_text;

	@DatabaseField
	@JsonProperty("title")
	private String title;

	// ... getters and setters

}

```

Code Diet
---------
Android annotations is used to keep the boilerplate logic out of your main activities and maintain clean code. Just look at the before and after exmaple here
http://androidannotations.org/ . 

`MainActivity.java`
Notice how clean the code is in the MainActiviy. We use annotations to get our view widgets and inject our RedditService into the view with one line of code.
We also run a backgroud thread to fetch the Reddit articles, then use the adapter in the UIThread to display them.

```java
@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_main)
public class MainActivity extends ActionBarActivity {

	@ViewById
	TextView textView1;

	@ViewById
	ListView listViewToDo;
	
	@ViewById
	ProgressBar progress;
	
	public List<Reddit> reddits = new ArrayList<Reddit>();
	
	
	@AfterViews
	void afterViews() {
		getRedditInBackground();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater();
		return true;
	}

	
	@OptionsItem
	void menu_settingsSelected() {
		// do work when settings is clicked
	}

	@OptionsItem
	void menu_item1Selected() {
		// do work when item1 is clicked
	}

	@OptionsItem
	void menu_redditSelected() {
		// do work when reddit is clicked
	}
	
	@Bean
	RedditService redditService;
	
	@Background
	void getRedditInBackground() {
		this.reddits = redditService.getRedditsList();
		showResult(this.reddits);
	}


	@UiThread
	void showResult(List<Reddit> redditList) {
		// load adapter
		RedditArrayAdapter adapter = new RedditArrayAdapter(this, R.layout.row_reddit, redditList);
		listViewToDo.setAdapter(adapter);
		// hide progress bar
		progress.setVisibility(View.GONE) ;
	}
	
	@ItemClick
	protected void listViewToDoItemClicked(Reddit selectedRedditItem) {
		// open browser with article url
		String url = selectedRedditItem.getUrl();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

}
```


Simple Rest Service for Reddit
------------------------------
Did you know reddit provides json formatting for each page, for example check out
http://www.reddit.com/r/Android/.json. This makes it very simple to create a reader app. Android Annotations
and Spring do most of the dirty work when doing a REST call. All we have to do is create an interface with the
@Rest annotation, then include your implemetations. Below I created a @Get annotation which returns
a JsonNode representing data from Reddit. We implement this interface in the code in the next section. 


`IRedditService.java`
```java
package services;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(converters = { MappingJacksonHttpMessageConverter.class })
public interface IRedditService {
	
	@Get("http://www.reddit.com/r/Android/.json")
	JsonNode getRedditsAsJSON();	

}



```

Dependency Injection using @EBean
----------------------------------
Notice in the services package we declare a `IRedditService.java` interface to do the Get request and return JSON, 
then we simply extend this interface to map the JSON to our Reddit Object model. I am using the android annontations @EBean annotation to 
allow this class to be injected into our main activity without having to "new" one up.

RedditService.java

```java
package services;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.type.TypeReference;
import utils.JsonUtils;
import models.Reddit;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.rest.RestService;

@EBean
public class RedditService extends IRedditService_ {
	
	@RestService
	IRedditService redditService;

	public List<Reddit> getRedditsList(){
	
		List<Reddit> reddits = new ArrayList<Reddit>();
		JsonNode json = redditService.getRedditsAsJSON();
		
		// Map json to Reddit model
		JsonNode dataNode = json.get("data");
		List<JsonNode> childrenNode = dataNode.get("children").findValues("data");
		Reddit r = new Reddit();
		for (JsonNode c : childrenNode) {
			try {
				r = JsonUtils.defaultMapper().readValue(c,
						new TypeReference<Reddit>() {
						});
				reddits.add(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return reddits;
		
	};

}

```


Adapters
---------
It took me a while to understand the adapter but after looking at a few examples it was quite simple.

First create a layout representing one row, then use an adapter to inject that row with the data.

`row_reddit.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="10dp" >

     <ImageView
        android:id="@+id/iconImg"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_marginRight="8dp"
         />
    
    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:textStyle="bold"
        android:layout_toRightOf="@id/iconImg"
        />

    <TextView
        android:id="@+id/desc"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/title"
        android:layout_toRightOf="@id/iconImg"
        android:textSize="12sp"
        />
 
</RelativeLayout>

```

`RedditArrayAdapter.java`
This code is pretty much boilerplate. We pass in a List of Reddits in the constructor, 
most of the code that injects the data into the row view is in the getView() method.

```java
package com.nicktee.kick;

import java.util.List;

import models.Reddit;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RedditArrayAdapter extends ArrayAdapter<Reddit> {

	List<Reddit> reddits;
	Context ctxt;
	int mLayoutResourceId;

	public RedditArrayAdapter(Context c, int layoutResourceId,
			List<Reddit> redditList) {
		super(c, layoutResourceId);
		reddits = redditList;
		ctxt = c;
		mLayoutResourceId = layoutResourceId;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reddits.size();
	}

	@Override
	public Reddit getItem(int position) {
		// TODO Auto-generated method stub
		return reddits.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		View row = view;

		final Reddit currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}
		
		// set the title
		final TextView textView = (TextView) row.findViewById(R.id.title);
		textView.setText(currentItem.getTitle());
		
		// set the sub text
		final TextView textView2 = (TextView) row.findViewById(R.id.desc);
		textView2.setText(currentItem.getAuthor_flair_text());
		
		// set the image
		final ImageView iconImg = (ImageView)row.findViewById(R.id.iconImg);
		//Load the image using the Picasso by Square lib, allows for image caching
		String imgUrl = currentItem.getThumbnail().toString();
		Picasso.with(ctxt).load(imgUrl).into(iconImg);
		
		
		return row;
	}

}



```
