package com.szcho.mobitweetapp;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import android.view.View;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class mainActivity extends Activity implements OnClickListener {

    private Context context;
	private TwitterData twitter;
	
	private TextView tweetButton, homeButton, searchButton;
	private EditText searchInput;
	
	private ListView list;
	private TimelineAdapter adapter;
	private List<TweetData> statuses = new ArrayList<TweetData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timeline);
        Log.i("info", "########## asi hakkab ###########");
    	twitter = new TwitterData(this);
        tweetButton = (TextView) findViewById(R.id.tweet);
        homeButton = (TextView) findViewById(R.id.home);
        searchButton = (TextView) findViewById(R.id.search);
        searchInput = (EditText) findViewById(R.id.edittext_search);
        tweetButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    } 
    
    public void onClick(View view) {
    	switch (view.getId()) {
        case R.id.tweet:
        	Intent intent = new Intent(context, TweetActivity.class);
            startActivity(intent);
        break;
        case R.id.home:
        	searchInput.setText("");
    		getHomeTimeline();
    		refreshTimeline();
        break;
        case R.id.search:
        	searchTweets(searchInput.getText().toString());
        break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.log_out:
            twitter.logOut();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("LogInActivity.onNewIntent", "kutse tagasi");
		Uri uri = intent.getData();
		twitter.authenticate(uri.getQueryParameter("oauth_verifier"));
		displayTimeLine();
	}
    
    @Override
	protected void onResume() {
    	super.onResume();
    	Log.i("mainActivity", "resume");
    	if (twitter.isConnected()) {
    		getHomeTimeline();
    		if (adapter != null) {
    			refreshTimeline();
    		} else {
    			displayTimeLine();
    		}
    	}
	}
    
    private void getHomeTimeline() {
    	try {
			statuses.clear();
			List<Status> statuses = twitter.getTwitter().getHomeTimeline();
			for (Status status : statuses) {
				this.statuses.add(new TweetData(status.getUser().getName(),
						status.getText(), 
						status.getCreatedAt().toLocaleString(), 
						null));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    }
    
    private void displayTimeLine() {
    	list=(ListView)findViewById(R.id.list);
    	
    	adapter=new TimelineAdapter(this, statuses);
        list.setAdapter(adapter);
	}
    
    private void refreshTimeline() {
    	if (adapter != null) {
			adapter.setStatuses(statuses);
			adapter.notifyDataSetChanged();
    	}
    }
    
    private void searchTweets(String search) {
        try {
    		Query query = new Query(search);
			QueryResult result = twitter.getTwitter().search(query);
			List<Tweet> tweets = result.getTweets();
			statuses.clear();
			for (Tweet tweet : tweets) {
				statuses.add(new TweetData(tweet.getFromUserName(),
						tweet.getText(), 
						tweet.getCreatedAt().toLocaleString(), 
						null));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
        refreshTimeline();
    }
    
}
