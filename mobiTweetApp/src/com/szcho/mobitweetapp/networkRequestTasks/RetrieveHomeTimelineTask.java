package com.szcho.mobitweetapp.networkRequestTasks;

import java.util.List;

import twitter4j.Paging;
import twitter4j.TwitterException;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.szcho.mobitweetapp.TweetData;
import com.szcho.mobitweetapp.mainActivity;

public class RetrieveHomeTimelineTask extends AsyncTask<Integer, Void, List<TweetData>> {
	
	private mainActivity activity;
	ProgressDialog dialog;
	
	public RetrieveHomeTimelineTask(mainActivity activity) {
		this.activity = activity;
		dialog = ProgressDialog.show(activity, "", "Loading, Please wait...", true);
	}

	@Override
	protected List<TweetData> doInBackground(Integer... i) {
		int page = 1;
		if (i == null || i[0] == 1) activity.getTweets().clear(); 
		else page = i[0];
    	try {
			List<twitter4j.Status> statuses = (activity.getTweets().isEmpty())?activity.getTwitterData()
					.getTwitter().getHomeTimeline(new Paging(page))
					:activity.getTwitterData()
					.getTwitter().getHomeTimeline(new Paging(page).maxId(activity.getTweets().get(0).getTweetId()));
			for (twitter4j.Status status : statuses) {
				activity.getTweets().add(new TweetData(status));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    	Log.i("RetrieveHomeTimelineTask.doInBackground", "Page "+page+" arrived");
    	return activity.getTweets();
	}

    protected void onPostExecute(List<TweetData> tweets) {
    	activity.refreshTimeline(tweets);
    	dialog.dismiss();
    }

}
