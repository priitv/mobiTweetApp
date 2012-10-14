package com.szcho.mobitweetapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import twitter4j.Twitter;
import twitter4j.TwitterException;
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
    	try {
    		Log.i("TwitterData.LogIn", "Starting connection");
	    	twitter = new TwitterFactory().getInstance();
	    	twitter.setOAuthConsumer(consumerKey, consumerSecret);
	    	if (accessToken != "") {
	    		Log.i("TwitterData.LogIn", "Using existing access");
	    		twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
	    		connected = true;
	    	} else {
	    		Log.i("TwitterData.LogIn", "Getting access");
				requestToken = twitter.getOAuthRequestToken("mobitweetapp://callback");
				String authUrl = requestToken.getAuthenticationURL();
				
				Intent intent = new Intent(activity, AuthenticateActivity.class);
			    intent.putExtra("URL", authUrl);
			    activity.startActivity(intent);
	    	}
		} catch (TwitterException e) {
			e.printStackTrace();
			Log.e("LogInActivity.twitterLogIn", e.getMessage());
		}
    }
    
    public void authenticate(String verifier) {
    	try {
			AccessToken accessTokenTwi = twitter.getOAuthAccessToken(requestToken, verifier);
			accessToken = accessTokenTwi.getToken();
			accessTokenSecret = accessTokenTwi.getTokenSecret();
			
			if (accessToken != null) {
				twitter.setOAuthAccessToken(accessTokenTwi);
	    		connected = true;
			} else {
				Log.d("authenticate", "Not verified");
			}

		} catch (TwitterException ex) {
			Log.e("authenticate", "" + ex.getMessage());
		}
    	savePreferences();
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
    
    public Twitter getTwitter() {
    	return twitter;
    }
    
	private void savePreferences(){
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
		Log.i("loadPreferences", "" + newAccessToken);
		setAccessToken(newAccessToken);
		setAccessTokenSecret(newAccessTokenSecret);
	}
	
	public void logOut() {
		accessToken = "";
		accessTokenSecret = "";
		savePreferences();
		activity.finish();
	}

	public boolean isConnected() {
		return connected;
	}
    
}
