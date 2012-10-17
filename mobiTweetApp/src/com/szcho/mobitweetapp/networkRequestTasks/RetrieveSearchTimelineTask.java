package com.szcho.mobitweetapp.networkRequestTasks;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.szcho.mobitweetapp.TweetData;
import com.szcho.mobitweetapp.mainActivity;

public class RetrieveSearchTimelineTask extends AsyncTask<String, Void, List<TweetData>> {
	
	private mainActivity activity;
	private ProgressDialog dialog;
	
	public RetrieveSearchTimelineTask(mainActivity activity) {
		this.activity = activity;
		dialog = ProgressDialog.show(activity, "", "Loading, Please wait...", true);
	}

	@Override
	protected List<TweetData> doInBackground(String... search) {
    	List<TweetData> statuses = new ArrayList<TweetData>();
        try {
    		Query query = new Query(search[0]);
    		Log.i("RetrieveSearchTimelineTask.doInBackground"
    				, "Searching: " + search[0]);
			QueryResult result = activity.getTwitterData()
					.getTwitter().search(query);
			List<Tweet> tweets = result.getTweets();
			for (Tweet tweet : tweets) {
				statuses.add(new TweetData(tweet));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    	
    	return statuses;
	}

    protected void onPostExecute(List<TweetData> tweets) {
    	activity.refreshTimeline(tweets);
    	dialog.dismiss();
    }

}
