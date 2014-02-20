package com.nicktee.redditreader;

import java.util.ArrayList;
import java.util.List;

import models.Reddit;
import services.DatabaseManager;
import services.RedditService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@ViewById
	ListView listViewToDo;

	@Extra
	String url;

	public List<Reddit> reddits = new ArrayList<Reddit>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// For all ormlite calls use the DatabaseManager
		DatabaseManager.init(this);
		
	}

	@AfterViews
	void afterViews() {
		setSupportProgressBarIndeterminateVisibility(true);
		getCachedReddits();
		getRedditInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Bean
	RedditService redditService;

	@Background
	void getRedditInBackground() {
		this.reddits = redditService.getRedditsList();
		populateRedditCache(this.reddits);
		showResult(this.reddits, true);
	}

	@UiThread
	void showResult(List<Reddit> redditList, boolean shouldHideProgress) {
		// load adapter
		RedditArrayAdapter adapter = new RedditArrayAdapter(this,
				R.layout.row_reddit, redditList);
		listViewToDo.setAdapter(adapter);
		// hide progress bar
		if (shouldHideProgress) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@ItemClick
	protected void listViewToDoItemClicked(Reddit selectedRedditItem) {
		// open browser with article url
		String url = selectedRedditItem.getUrl();
		Intent i = new Intent(getApplicationContext(), WebActivity_.class);
		i.putExtra("Url", url);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

	@Background
	void getCachedReddits() {
		this.reddits = DatabaseManager.getInstance().getAllReddits();
		showResult(this.reddits, false);
	}

	void populateRedditCache(List<Reddit> rs){
		// delete all the old reddits
		DatabaseManager.getInstance().deleteReddits();
		// add all the new reddits
		for (Reddit red : rs){
			DatabaseManager.getInstance().addReddit(red);
		}
	}
	
	

	
}
