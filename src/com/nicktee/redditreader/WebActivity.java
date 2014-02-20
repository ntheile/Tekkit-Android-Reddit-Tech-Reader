package com.nicktee.redditreader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
	void afterView(){
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setWebView();
	}
	
	
	void setWebView(){
		String url ="";
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null) {
		   url  = extras.getString("Url");
		}
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);
		
	}
	
	

}
