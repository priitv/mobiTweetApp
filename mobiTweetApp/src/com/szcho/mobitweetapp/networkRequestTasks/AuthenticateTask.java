package com.szcho.mobitweetapp.networkRequestTasks;

import com.szcho.mobitweetapp.TwitterData;
import com.szcho.mobitweetapp.mainActivity;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.os.AsyncTask;
import android.util.Log;

public class AuthenticateTask extends AsyncTask<String, Void, AccessToken> {
	
	private TwitterData twitterData;
	
	public AuthenticateTask(TwitterData twitterData) {
		this.twitterData = twitterData;
	}

	@Override
	protected AccessToken doInBackground(String... verifier) {
		try {
			AccessToken accessTokenTwi = twitterData.getTwitter().
					getOAuthAccessToken(twitterData.getRequestToken(), verifier[0]);
			return accessTokenTwi;
		} catch (TwitterException e) {
			Log.e("AuthenticateTask.doInBackground", e.getMessage());
			return null;
		}
	}

    protected void onPostExecute(AccessToken accessTokenTwi) {
		twitterData.setAccessToken(accessTokenTwi.getToken());
		twitterData.setAccessTokenSecret(accessTokenTwi.getTokenSecret());
		
		if (twitterData.getAccessToken() != null) {
			twitterData.getTwitter().setOAuthAccessToken(accessTokenTwi);
    		twitterData.setConnected(true);
    		twitterData.savePreferences();
    		new RetrieveHomeTimelineTask((mainActivity) twitterData.getActivity()).execute();
		} else {
			Log.d("AuthenticateTask.onPostExecute", "Not verified");
		}
    }

}
