package com.szcho.mobitweetapp;

import twitter4j.TwitterException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to post tweets.. Nothing fancy
 * @author PriitV
 *
 */
public class TweetActivity extends Activity {
	
	private TwitterData twitter;
	private Button send;
	private EditText text;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_tweet);
    	twitter = new TwitterData(this);
    	send = (Button) findViewById(R.id.button_send_tweet);
    	text = (EditText) findViewById(R.id.edittext_send_tweet);
    	
    	send.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			    try {
					twitter.getTwitter().updateStatus(text.getText().toString());
					finish();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
