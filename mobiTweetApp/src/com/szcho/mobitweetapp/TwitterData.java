package com.szcho.mobitweetapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Tweet;
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
			    activity.startActivityForResult(intent, 1);
	    	}
		} catch (TwitterException e) {
			e.printStackTrace();
			Log.e("TwitterData.LogIn", e.getMessage());
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
				Log.d("TwitterData.authenticate", "Not verified");
			}

		} catch (TwitterException ex) {
			Log.e("TwitterData.authenticate", "" + ex.getMessage());
		}
    	savePreferences();
    }

    public List<TweetData> getHomeTimeline() {
        List<TweetData> tweets = new ArrayList<TweetData>();
    	try {
			List<Status> statuses = twitter.getHomeTimeline();
			for (Status status : statuses) {
				tweets.add(new TweetData(status));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    	return tweets;
    }
    
    public List<TweetData> searchTweets(String search) {
    	List<TweetData> statuses = new ArrayList<TweetData>();
        try {
    		Query query = new Query(search);
			QueryResult result = twitter.search(query);
			List<Tweet> tweets = result.getTweets();
			for (Tweet tweet : tweets) {
				statuses.add(new TweetData(tweet));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
        return statuses;
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
    
}
