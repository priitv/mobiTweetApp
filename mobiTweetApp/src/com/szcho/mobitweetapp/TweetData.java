package com.szcho.mobitweetapp;

import twitter4j.Status;
import twitter4j.Tweet;

public class TweetData {
	private String userName, text, createdAt, profileImageURL;
	private long tweetId;
	
	/**
	 * Contains needed data for TimelineAdapter
	 * @param userName
	 * @param text - status text
	 * @param createdAt
	 * @param profileImageURL
	 */
	public TweetData(String userName, String text, String createdAt, String profileImageURL, int tweetId) {
		this.userName = userName;
		this.text = text;
		this.createdAt = createdAt;
		this.profileImageURL = profileImageURL;
		this.tweetId = tweetId;
	}
	
	public TweetData(Status status) {
		this.userName = status.getUser().getName();
		this.text = status.getText();
		this.createdAt = status.getCreatedAt().toLocaleString();
		this.profileImageURL = status.getUser().getProfileImageURL().toString();
		this.tweetId = status.getId();
	}
	
	public TweetData(Tweet tweet) {
		this.userName = tweet.getFromUserName();
		this.text = tweet.getText();
		this.createdAt = tweet.getCreatedAt().toLocaleString();
		this.profileImageURL = tweet.getProfileImageUrl();
		this.tweetId = tweet.getId();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(int tweetId) {
		this.tweetId = tweetId;
	}
	
}
