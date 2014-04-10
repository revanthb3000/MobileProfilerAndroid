package com.iitg.mobileprofiler;

import java.util.ArrayList;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.db.ResponseDao;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Responses extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		setContentView(R.layout.activity_responses);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		*/
		
		Bundle extras = getIntent().getExtras();
		String question = "";
		if (extras != null) {
		    question = extras.getString("msg");
		}
		
		ArrayList<String> responses = new ArrayList<String>();
		DatabaseConnector databaseConnector = new DatabaseConnector();
		ArrayList<ResponseDao> responseDaos = databaseConnector.getAnswersOfQuestion(question);
		databaseConnector.closeDBConnection();
		for(ResponseDao responseDao : responseDaos){
			responses.add(responseDao.getUserId() + "-" + responseDao.getAnswer());
		}
		String[] textArray = new String[responses.size()];
		textArray = responses.toArray(textArray);
		int length=textArray.length;
		LinearLayout layout = new LinearLayout(this);
		setContentView(layout);
		layout.setOrientation(LinearLayout.VERTICAL);
		for(int i=0;i<length;i++)
		{
		    TextView tv=new TextView(getApplicationContext());
		    tv.setText(textArray[i]);
		    layout.addView(tv);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.responses, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_responses,
					container, false);
			return rootView;
		}
	}

}
