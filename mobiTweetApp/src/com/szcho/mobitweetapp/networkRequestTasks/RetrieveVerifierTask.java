package com.szcho.mobitweetapp.networkRequestTasks;

import com.szcho.mobitweetapp.AuthenticateActivity;
import com.szcho.mobitweetapp.TwitterData;
import com.szcho.mobitweetapp.mainActivity;

import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveVerifierTask extends AsyncTask<Void, Void, RequestToken> {
	
	private TwitterData twitterData;
	
	public RetrieveVerifierTask(TwitterData twitterData) {
		this.twitterData = twitterData;
	}

	@Override
	protected RequestToken doInBackground(Void... params) {
		try {
			RequestToken requestToken = twitterData.getTwitter().getOAuthRequestToken("mobitweetapp://callback");
			return requestToken;
		} catch (TwitterException e) {
			Log.e("RetrieveRequestTokenTask.doInBackground", e.getMessage());
			return null;
		}
	}

    protected void onPostExecute(RequestToken requestToken) {
    	if (requestToken != null) {
	    	twitterData.setRequestToken(requestToken);
			String authUrl = requestToken.getAuthenticationURL();
			
			Intent intent = new Intent(twitterData.getActivity(), AuthenticateActivity.class);
		    intent.putExtra("URL", authUrl);
		    twitterData.getActivity().startActivityForResult(intent, mainActivity.ON_ACTIVITY_RESULT_AUTH);
    	} else {
    		Log.e("RetrieveRequestTokenTask.onPostExecute", "Couldn't recieve requestToken");
    	}
    }

}
