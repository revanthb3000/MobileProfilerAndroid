package com.iitg.mobileprofiler;

import org.iitg.mobileprofiler.mobilecore.MobileProfierMainActivity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class QueryActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_query,
					container, false);
			return rootView;
		}
	}
	
	public void sendQuestion(View view) {
		EditText editText = (EditText) findViewById(R.id.editText1);
		String message = editText.getText().toString();
		System.out.println("Hello"+editText.getText().toString()+"BYE");
		if(message.equals(null)){
			System.out.println("IN IF");
			new AlertDialog.Builder(this)
		    .setTitle("Enter Question")
		    .setMessage("Enter the Question").show();
		}
		else if(!message.equals("Enter your Question")){
			MobileProfierMainActivity.userNodePeer.sendQuestionToPeers(message);
		}
		editText.setHint("Enter your Question");

	}

}
