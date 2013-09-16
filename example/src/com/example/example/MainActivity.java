package com.example.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {

	private final String URL = "http://www.google.es";
	
	private TextView status;
	private TextView result;
	
	private AsyncHttpClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.client = new AsyncHttpClient();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_play) this.queryUrl(this.URL);
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// getting content from URL:
		this.queryUrl(this.URL);
	}
	
	private void queryUrl(String url) {
		this.client.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onStart() {
				super.onStart();
				setStatus("Querying URL...");
				status.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
				setResult("");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			public void onSuccess(int code, String content) {
				super.onSuccess(code, content);
				setStatus("SUCCESS!");
				status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
				setResult(content);
				result.animate();
			}
			
			@Override
			public void onFailure(Throwable t, String message) {
				super.onFailure(t, message);
				setStatus("FAILURE!");
				status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
				setResult(message); 
			}
		});
	}
	
	private void setStatus(String status) {
		if (this.status == null) this.status = (TextView)this.findViewById(R.id.status);
		this.status.setText(status);
	}
	
	private void setResult(String result) {
		if (this.result == null) this.result = (TextView)this.findViewById(R.id.result);
		this.result.setText(result);
	}

}
