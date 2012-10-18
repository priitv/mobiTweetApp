package com.szcho.mobitweetapp;

import com.szcho.mobitweetapp.networkRequestTasks.SendTweetTask;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity to post tweets.. Nothing fancy
 * @author PriitV
 *
 */
public class TweetActivity extends Activity {
	
	private TwitterData twitter;
	private Button send;
	private EditText text;
	private TextView label;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.send_tweet);
        final Activity activity = this;
    	twitter = new TwitterData(this);
    	send = (Button) findViewById(R.id.button_send_tweet);
    	text = (EditText) findViewById(R.id.edittext_send_tweet);
    	label = (TextView) findViewById(R.id.label_send_tweet);
    	
    	text.addTextChangedListener(mTextEditorWatcher);
    	
    	send.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			    new SendTweetTask(activity, twitter.getTwitter())
			    	.execute(text.getText().toString());
			}
		});
	}
	
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        	label.setText("Send Tweet(" + (140-text.length()) + "):");
        }

		public void afterTextChanged(Editable arg0) {
			
		}
};
	
}
