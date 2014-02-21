package com.nicktee.redditreader;

import java.util.ArrayList;
import java.util.List;

import models.Reddit;
import services.DatabaseManager;
import services.RedditService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
	
	String nextPageReddit;
	
	RedditArrayAdapter adapter;
	
	View footerView;
	
	int mCurCheckPosition = 0;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);		
		// For all ormlite calls use the DatabaseManager
		DatabaseManager.init(this);
		
		 if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
         }
		 
	}
	
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

	@AfterViews
	void afterViews() {
		setSupportProgressBarIndeterminateVisibility(true);
		if (isOnline() == false){
			getCachedReddits();	
		}
		else{
			getRedditInBackground();	
		}
		setFooter();
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
		this.reddits = redditService.getRedditsList(null);
		setNextPage();
		populateRedditCache(this.reddits);
		showResult(this.reddits, true);
	}

	@UiThread
	void showResult(List<Reddit> redditList, boolean shouldHideProgress) {
		// load adapter
		adapter = new RedditArrayAdapter(this,
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
	
	void setFooter(){
		// footer load more
		footerView =  ((LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_reddit, null, false);
	    listViewToDo.addFooterView(footerView);
	    footerView.findViewById(R.id.btnLoadMore).setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v)
            {
	        	// request more reddits and add to the adapter
	        	footerView.findViewById(R.id.btnLoadMore).setVisibility(View.INVISIBLE);
	    		footerView.findViewById(R.id.progressMoreReddits).setVisibility(View.VISIBLE);
	        	getMoreRedditsInBackground();
            }
         });
	}
	
	void setNextPage(){
		nextPageReddit = this.reddits.get(this.reddits.size() - 1).getAfter();
		Log.v("Kicker", nextPageReddit);
	}
	
	@Background
	void getMoreRedditsInBackground() {
		
		List<Reddit> moreReddits = redditService.getRedditsList(nextPageReddit);
		for (Reddit r: moreReddits){
			this.reddits.add(r);	
		}
		setNextPage();
		updateRedditsAdapter();
	}
	
	@UiThread
	void updateRedditsAdapter(){
		footerView.findViewById(R.id.btnLoadMore).setVisibility(View.VISIBLE);
		footerView.findViewById(R.id.progressMoreReddits).setVisibility(View.INVISIBLE);
		adapter.notifyDataSetChanged();
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
}
