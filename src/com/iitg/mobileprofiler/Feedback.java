package com.iitg.mobileprofiler;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class Feedback extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		TextView textView = (TextView)findViewById(R.id.editText1); 
		//Get the question from DATABASE
		textView.setText("OK Please rate this Q ");
		textView.setKeyListener(null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_feedback,
					container, false);
			return rootView;
		}
	}
	
	public void sendFeedback(View view){
		EditText editText = (EditText) findViewById(R.id.EditText);
		int rating;
		try{
			rating = Integer.parseInt(editText.getText().toString());
		}catch (NumberFormatException e){
			rating = 0;
		}
		
		System.out.println(rating);
		if(rating<1 || rating>10){
			System.out.println("IN IF");
			new AlertDialog.Builder(this)
		    .setTitle("HIGH RATING")
		    .setMessage("Enter the values between 1 to 10").show();
		}
		else{
			//Write the code to enter the value in DATABASE
		}
	}

}
