package com.szcho.mobitweetapp;


import com.szcho.mobitweetapp.networkRequestTasks.AuthenticateTask;
import com.szcho.mobitweetapp.networkRequestTasks.RetrieveVerifierTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterData {
	private final String consumerKey = "WBPbfUjoXImIJS24jxjzRA";
	private final String consumerSecret = "C73pf5n6NSpTtg91qvEVzclRNQ9fTZoUXEgWq9N65k";
	private String accessToken = "";
	private String accessTokenSecret = "";
	private Twitter twitter;
	private RequestToken requestToken;

	private Activity activity;
	private boolean connected = false;

	public TwitterData(Activity a) {
		this.activity = a;
    	loadPreferences();
		LogIn();
	}
    
    public void LogIn() {
    	Log.i("TwitterData.LogIn", "Starting connection");
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		if (accessToken != "") {
			Log.i("TwitterData.LogIn", "Using existing access");
			twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
			connected = true;
		} else {
			Log.i("TwitterData.LogIn", "Getting access");
			new RetrieveVerifierTask(this).execute();
		}
    }
    
    public void authenticate(String verifier) {
    	//Log.i("TwitterData.authenticate", "Verifier: " + verifier); //debug
		new AuthenticateTask(this).execute(verifier);
    }
    
    public void setAccessToken(String accessToken) {
    	this.accessToken = accessToken;
    }
    
    public void setAccessTokenSecret(String accessTokenSecret) {
    	this.accessTokenSecret = accessTokenSecret;
    }

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}
    
    public Twitter getTwitter() {
    	return twitter;
    }
    
    public Activity getActivity() {
    	return activity;
    }
    
	public void savePreferences(){
		SharedPreferences sharedPreferences = activity.getSharedPreferences("mobiTweetApp", 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("accessToken", accessToken);
		editor.putString("accessTokenSecret", accessTokenSecret);
		editor.commit();
	}

	private void loadPreferences(){
		SharedPreferences sharedPreferences = activity.getSharedPreferences("mobiTweetApp", 0);
		String newAccessToken = sharedPreferences.getString("accessToken", "");
		String newAccessTokenSecret = sharedPreferences.getString("accessTokenSecret", "");
		setAccessToken(newAccessToken);
		setAccessTokenSecret(newAccessTokenSecret);
	}
	
	public void logOut() {
		accessToken = "";
		accessTokenSecret = "";
		savePreferences();
		CookieSyncManager.createInstance(activity);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeSessionCookie();
		activity.finish();
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
    
}
