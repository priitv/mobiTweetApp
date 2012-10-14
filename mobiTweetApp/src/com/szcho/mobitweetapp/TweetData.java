package com.szcho.mobitweetapp;

public class TweetData {
	private String userName, text, createdAt, profileImageURL;
	
	public TweetData(String userName, String text, String createdAt, String profileImageURL) {
		this.userName = userName;
		this.text = text;
		this.createdAt = createdAt;
		this.profileImageURL = profileImageURL;
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
	
}
