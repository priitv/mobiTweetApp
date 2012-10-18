package com.szcho.mobitweetapp;

import java.util.ArrayList;
import java.util.List;

import com.szcho.mobitweetapp.networkRequestTasks.RetrieveHomeTimelineTask;
import com.szcho.mobitweetapp.networkRequestTasks.RetrieveSearchTimelineTask;

import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class mainActivity extends Activity implements OnClickListener, OnScrollListener {

	public static final int ON_ACTIVITY_RESULT_AUTH = 1, ON_ACTIVITY_RESULT_TWEET = 2;
	public static final int PAGE_HOME = 0, PAGE_SEARCH = 1;
    private Context context;
	private TwitterData twitter;
	private List<TweetData> tweets;
	private int pageNumber = 1, page = PAGE_HOME;
	private boolean readyForListUpdate = false;

	private TextView tweetButton, homeButton, searchButton;
	private EditText searchInput;
	
	private ListView list;
	private TimelineAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timeline);
        Log.i("mainActivity.onCreate"
        		, "########## starting main activity ###########");
    	tweets = new ArrayList<TweetData>();
    	twitter = new TwitterData(this);
    	list=(ListView)findViewById(R.id.list);
        tweetButton = (TextView) findViewById(R.id.tweet);
        homeButton = (TextView) findViewById(R.id.home);
        searchButton = (TextView) findViewById(R.id.search);
        searchInput = (EditText) findViewById(R.id.edittext_search);
        list.setOnScrollListener(this);
        tweetButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        if (twitter.isConnected()) 
        	new RetrieveHomeTimelineTask(this).execute(pageNumber);
    } 
    
    public void onClick(View view) {
    	switch (view.getId()) {
        case R.id.tweet:
        	searchInput.setText("");
        	Intent intent = new Intent(context, TweetActivity.class);
            startActivityForResult(intent, ON_ACTIVITY_RESULT_TWEET);
        break;
        case R.id.home:
        	searchInput.setText("");
        	page = PAGE_HOME;
        	pageNumber = 1;
			new RetrieveHomeTimelineTask(this).execute(pageNumber);
        break;
        case R.id.search:
        	pageNumber = 1;
        	page = PAGE_SEARCH;
        	String searchText = searchInput.getText().toString();
        	if (!searchText.matches("")) {
				new RetrieveSearchTimelineTask(this)
					.execute(searchText);
        	} else {
        		Toast.makeText(this, "Search field was empty!", Toast.LENGTH_SHORT).show();
        	}
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case ON_ACTIVITY_RESULT_AUTH:
    		//after logging in and getting verifier
			if(resultCode == RESULT_OK){
				String result=data.getStringExtra("result");
				twitter.authenticate(result);
			} else if(resultCode == RESULT_CANCELED){
				finish();
			}
    		break;
    	case ON_ACTIVITY_RESULT_TWEET:
    		//request fresh home timeline after tweeting
			if(resultCode == RESULT_OK){
				Log.i("mainActivity.onActivityResult", "tweeted");
				try {
					Thread.sleep(500);
					//ugly but otherwise it may get old data from server,
					//can't understand why. Maybe server-side problem(couldn't 
					//display new data fast enough).
				} catch (InterruptedException e) {
					Log.i("mainActivity.onActivityResult", "sleep interrupted");
				}
	        	page = PAGE_HOME;
	        	pageNumber = 1;
				new RetrieveHomeTimelineTask(this).execute(1);
			} else {
				Log.i("mainActivity.onActivityResult", "Tweet wasn't sent.");
			}
			break;
		}
    }
    
    /**
     * Set Timeline and data in it
     */
    private void displayTimeLine(List<TweetData> statuses) {
    	
    	adapter=new TimelineAdapter(this, statuses);
        list.setAdapter(adapter);
	}

    /**
     * Change data in Timeline
     * @param statuses 
     */
    public void refreshTimeline(List<TweetData> statuses) {
    	if (adapter != null) {
			adapter.setStatuses(statuses);
			adapter.notifyDataSetChanged();
        	if (pageNumber == 1) list.setSelection(0);
    	} else {
    		displayTimeLine(statuses);
    	}
    	readyForListUpdate = true;
    }
	
	public TwitterData getTwitterData() {
		return twitter;
	}

	public List<TweetData> getTweets() {
		return tweets;
	}

	public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		switch(lw.getId()) {
        case R.id.list:
            final int lastItem = firstVisibleItem + visibleItemCount;
            if(lastItem == totalItemCount && page == PAGE_HOME && pageNumber < 5 
            		&& tweets.size() == pageNumber*20) {
            	if (readyForListUpdate) {
            		pageNumber++;
	            	new RetrieveHomeTimelineTask(this).execute(pageNumber);
	            	readyForListUpdate = false;
            	}
            }
		}
		
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
    
}
