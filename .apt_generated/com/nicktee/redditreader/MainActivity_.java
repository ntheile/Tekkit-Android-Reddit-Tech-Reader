//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.nicktee.redditreader;

import java.sql.SQLException;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.googlecode.androidannotations.api.SdkVersionHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.nicktee.redditreader.models.Prefs;
import com.nicktee.redditreader.models.Reddit;
import com.nicktee.redditreader.services.DatabaseHelper;
import com.nicktee.redditreader.services.MyPrefs_;
import com.nicktee.redditreader.services.RedditService_;

public final class MainActivity_
    extends MainActivity
{

    private ConnectionSource connectionSource_;
    private Handler handler_ = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void init_(Bundle savedInstanceState) {
        myPrefs = new MyPrefs_(this);
        injectExtras_();
        connectionSource_ = OpenHelperManager.getHelper(this, DatabaseHelper.class).getConnectionSource();
        try {
            prefsTable = DaoManager.createDao(connectionSource_, Prefs.class);
        } catch (SQLException e) {
            Log.e("MainActivity_", "Could not create DAO", e);
        }
        redditService = RedditService_.getInstance_(this);
    }

    private void afterSetContentView_() {
        listViewToDo = ((ListView) findViewById(com.nicktee.redditreader.R.id.listViewToDo));
        subredditTitle = ((TextView) findViewById(com.nicktee.redditreader.R.id.subredditTitle));
        {
            AdapterView<?> view = ((AdapterView<?> ) findViewById(com.nicktee.redditreader.R.id.listViewToDo));
            if (view!= null) {
                view.setOnItemClickListener(new OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listViewToDoItemClicked(((Reddit) parent.getAdapter().getItem(position)));
                    }

                }
                );
            }
        }
        ((RedditService_) redditService).afterSetContentView_();
        afterViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static MainActivity_.IntentBuilder_ intent(Context context) {
        return new MainActivity_.IntentBuilder_(context);
    }

    @SuppressWarnings("unchecked")
    private<T >T cast_(Object object) {
        return ((T) object);
    }

    private void injectExtras_() {
        Intent intent_ = getIntent();
        Bundle extras_ = intent_.getExtras();
        if (extras_!= null) {
            if (extras_.containsKey("url")) {
                try {
                    url = cast_(extras_.get("url"));
                } catch (ClassCastException e) {
                    Log.e("MainActivity_", "Could not cast extra to expected type, the field is left to its default value", e);
                }
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(com.nicktee.redditreader.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showResult(final List<Reddit> redditList, final boolean shouldHideProgress) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.showResult(redditList, shouldHideProgress);
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void updateRedditsAdapter() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.updateRedditsAdapter();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void getRedditInBackground() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.getRedditInBackground();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void getMoreRedditsInBackground() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.getMoreRedditsInBackground();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    @Override
    public void getCachedReddits() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    MainActivity_.super.getCachedReddits();
                } catch (RuntimeException e) {
                    Log.e("MainActivity_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, MainActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public MainActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

        public MainActivity_.IntentBuilder_ url(String url) {
            intent_.putExtra("url", url);
            return this;
        }

    }

}
