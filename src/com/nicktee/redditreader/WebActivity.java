package com.nicktee.redditreader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_web)
public class WebActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	@ViewById
	WebView webView;

	@ViewById
	ProgressBar webProgress;

	@AfterViews
	void afterView() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setWebView();
	}

	void setWebView() {
		String url = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			url = extras.getString("Url");
		}
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// maintain scroll position
			setResult(RESULT_CANCELED);
			finish();
			// NavUtils.navigateUpTo(this, new Intent(this,
			// ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
