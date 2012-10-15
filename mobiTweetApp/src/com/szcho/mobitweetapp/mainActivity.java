package com.szcho.mobitweetapp;

import java.util.ArrayList;
import java.util.List;

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
        	searchInput.setText("");
        	Intent intent = new Intent(context, TweetActivity.class);
            startActivity(intent);
        break;
        case R.id.home:
        	searchInput.setText("");
    		statuses = twitter.getHomeTimeline();
    		refreshTimeline();
        break;
        case R.id.search:
        	statuses = twitter.searchTweets(searchInput.getText().toString());
    		refreshTimeline();
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
    
    //Left next method just in case for future uses, but not using it anymore.
    //From now on getting verifier from AuthenticateActivity result.
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("LogInActivity.onNewIntent", "call");
		Uri uri = intent.getData();
		twitter.authenticate(uri.getQueryParameter("oauth_verifier"));
		displayTimeLine();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){
				String result=data.getStringExtra("result");
				twitter.authenticate(result);
				displayTimeLine();
			}
		}
    }
    
    @Override
	protected void onResume() {
    	super.onResume();
    	Log.i("mainActivity", "resume");
    	if (twitter.isConnected()) {
    		statuses = twitter.getHomeTimeline();
    		if (adapter != null) {
    			refreshTimeline();
    		} else {
    			displayTimeLine();
    		}
    	}
	}
    
    /**
     * Set Timeline and data in it
     */
    private void displayTimeLine() {
    	list=(ListView)findViewById(R.id.list);
    	
    	adapter=new TimelineAdapter(this, statuses);
        list.setAdapter(adapter);
	}

    /**
     * Change data in Timeline
     */
    private void refreshTimeline() {
    	if (adapter != null) {
			adapter.setStatuses(statuses);
			adapter.notifyDataSetChanged();
    	}
    }
    
}
