package com.nicktee.redditreader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.OrmLiteDao;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.j256.ormlite.dao.Dao;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.models.Reddit;
import com.nicktee.redditreader.services.DatabaseHelper;
import com.nicktee.redditreader.services.DatabaseManager;
import com.nicktee.redditreader.services.MyPrefs_;
import com.nicktee.redditreader.services.RedditService;
import com.nicktee.redditreader.utils.JsonUtils;
import com.squareup.picasso.Picasso;

@EActivity
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@ViewById
	ListView listViewToDo;

	@Extra
	String url;

	@ViewById
	TextView subredditTitle;

	@Pref
	MyPrefs_ myPrefs;

	DatabaseManager db;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Prefs.class)
	Dao<Prefs, Integer> prefsTable;

	public List<Reddit> reddits = new ArrayList<Reddit>();

	String nextPageReddit;

	RedditArrayAdapter adapter;

	View footerView;

	int mCurCheckPosition = 0;

	int mListHeight = 3500;

	List<Prefs> prefs;

	String title = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// Set up Fading Action Bar
		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.header)
				.contentLayout(R.layout.activity_main).parallax(true);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		setImageOnFadingActionBar();

		// For all ormlite calls use the DatabaseManager
		DatabaseManager.init(this);
		db = DatabaseManager.getInstance();
		setTitle();

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
		if (isOnline() == false) {
			getCachedReddits();
		} else {
			getRedditInBackground();
		}
		setFooter();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showPrefsDialog();
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
		adapter = new RedditArrayAdapter(this, R.layout.row_reddit, redditList);
		listViewToDo.setAdapter(adapter);
		// hide progress bar
		if (shouldHideProgress) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
		listViewToDo.getLayoutParams().height = mListHeight;
		
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
		this.reddits = db.getAllReddits();
		showResult(this.reddits, false);
	}

	void populateRedditCache(List<Reddit> rs) {
		// delete all the old reddits
		db.deleteReddits();
		// add all the new reddits
		for (Reddit red : rs) {
			DatabaseManager.getInstance().addReddit(red);
		}
	}

	void setFooter() {
		// footer load more
		footerView = ((LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer_reddit, null, false);
		listViewToDo.addFooterView(footerView);
		footerView.findViewById(R.id.btnLoadMore).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						// request more reddits and add to the adapter
						footerView.findViewById(R.id.btnLoadMore)
								.setVisibility(View.INVISIBLE);
						footerView.findViewById(R.id.progressMoreReddits)
								.setVisibility(View.VISIBLE);
						getMoreRedditsInBackground();
					}
				});
	}

	void setNextPage() {
		nextPageReddit = this.reddits.get(this.reddits.size() - 1).getAfter();
		mListHeight = mListHeight + 3500;
		listViewToDo.getLayoutParams().height = mListHeight;
		Log.v("Kicker", nextPageReddit);
	}

	void showPrefsDialog() {
		DialogFragment prefsActivity = new PrefsActivity();
		prefsActivity.show(getSupportFragmentManager(), "prefs");
	
	}

	@Background
	void getMoreRedditsInBackground() {

		List<Reddit> moreReddits = redditService.getRedditsList(nextPageReddit);
		for (Reddit r : moreReddits) {
			this.reddits.add(r);
		}
		setNextPage();
		updateRedditsAdapter();
	}

	@UiThread
	void updateRedditsAdapter() {
		footerView.findViewById(R.id.btnLoadMore).setVisibility(View.VISIBLE);
		footerView.findViewById(R.id.progressMoreReddits).setVisibility(
				View.INVISIBLE);
		adapter.notifyDataSetChanged();
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	void setImageOnFadingActionBar() {
		ArrayList<String> images = new ArrayList<String>();
		images.add("http://www.androidanalyse.com/wp-content/uploads/2014/02/I-Love-Android-Wallpaper.jpeg");
		images.add("http://www.sleekdesignstudio.com/html5poster/wallpapers/HTML5_Wallpaper_2560x1440.png");
		images.add("http://fc01.deviantart.net/fs71/i/2012/307/e/6/html5_wallpaper_by_ivanmladenovi-d5jubwb.jpg");
		images.add("http://blog.inner-active.com/wp-content/uploads/2013/08/AndroidWallpaper.jpg");
		images.add("http://android-foundry.com/wp-content/uploads/071812_Android_IceCreamMonth_wallpaper.jpg");
		images.add("http://netdna.webdesignerdepot.com/uploads/2012/11/code.jpg");
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(images.size());
		ImageView imgView = (ImageView) findViewById(R.id.image_header);
		try{
			
			Picasso.with(this.getBaseContext()).load(images.get(index)).into(imgView);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		imgView.getLayoutParams().height = 450;
	}

	void setTitle() {
		try {
			prefs = prefsTable.query(prefsTable.queryBuilder().where().eq("selected", true).prepare());
			for (Prefs p : prefs) {
				title = title + p.getSubReddits() + "+";
			}
			subredditTitle.setText(title);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
