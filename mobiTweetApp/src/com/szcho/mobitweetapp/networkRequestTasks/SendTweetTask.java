package com.szcho.mobitweetapp.networkRequestTasks;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SendTweetTask extends AsyncTask<String, Void, Integer> {
	
	private Activity activity;
	private Twitter twitter;
	private ProgressDialog dialog;
	
	public SendTweetTask(Activity activity, Twitter twitter) {
		this.activity = activity;
		this.twitter = twitter;
		dialog = ProgressDialog.show(activity, "", "Loading, Please wait...", true);
	}

	@Override
	protected Integer doInBackground(String... text) {
		try {
			twitter4j.Status status = twitter.updateStatus(text[0]);
			Log.i("SendTweetTask.doInBackground", "Updated status: " + status.getText());
		} catch (TwitterException e) {
			Log.i("SendTweetTask.doInBackground", e.getErrorMessage());
		}
		return 1;
	}

    protected void onPostExecute(Integer i) {
		Intent returnIntent = new Intent();
		activity.setResult(Activity.RESULT_OK, returnIntent); 
    	dialog.dismiss();
		activity.finish();
    }

}
