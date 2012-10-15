package com.szcho.mobitweetapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
/**
 * Activity with webview to get verifier from twitter
 * @author PriitV
 *
 */
public class AuthenticateActivity extends Activity {
 
	private WebView webView;
	private String result;
 
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
 
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(getIntent().getStringExtra("URL"));
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Uri uri = Uri.parse(url);
		        if (!uri.getHost().equals("callback")) {
		            return false;
		        } else {
		        	result = uri.getQueryParameter("oauth_verifier");
					Intent returnIntent = new Intent();
					returnIntent.putExtra("result",result);
					setResult(RESULT_OK,returnIntent);     
					finish();
		        }
				return true;
			}
		});
 
	}
 
}