package com.szcho.mobitweetapp.networkRequestTasks;

import java.util.ArrayList;
import java.util.List;

import twitter4j.TwitterException;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.szcho.mobitweetapp.TweetData;
import com.szcho.mobitweetapp.mainActivity;

public class RetrieveHomeTimelineTask extends AsyncTask<Void, Void, List<TweetData>> {
	
	private mainActivity activity;
	ProgressDialog dialog;
	
	public RetrieveHomeTimelineTask(mainActivity activity) {
		this.activity = activity;
		dialog = ProgressDialog.show(activity, "", "Loading, Please wait...", true);
	}

	@Override
	protected List<TweetData> doInBackground(Void... params) {
		List<TweetData> tweets = new ArrayList<TweetData>();
    	try {
			List<twitter4j.Status> statuses = activity.getTwitterData()
					.getTwitter().getHomeTimeline();
			for (twitter4j.Status status : statuses) {
				tweets.add(new TweetData(status));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    	return tweets;
	}

    protected void onPostExecute(List<TweetData> tweets) {
    	activity.refreshTimeline(tweets);
    	dialog.dismiss();
    }

}
