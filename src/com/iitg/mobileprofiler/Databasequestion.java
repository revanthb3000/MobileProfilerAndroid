package com.iitg.mobileprofiler;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class Databasequestion extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TABLE CODE
		ScrollView scroll = new ScrollView(getBaseContext());
		TableLayout tableLayout = new TableLayout(getApplicationContext());
		scroll.addView(tableLayout);
	    TableRow tableRow;
	    TextView textView;
	    int noRows = 20;
	    int noCols = 2;
	    for (int i = 0; i < noRows; i++) {
	        tableRow = new TableRow(getApplicationContext());
	        for (int j = 0; j < noCols; j++) {
	            textView = new TextView(getApplicationContext());
	            textView.setText("test done");
	            textView.setPadding(20, 20, 20, 20);
	            tableRow.addView(textView);
	        }
	        tableLayout.addView(tableRow);
	    }
	    setContentView(scroll);
		/*setContentView(R.layout.activity_databasequestion);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		*/
		getQuestions();
	}

	private void getQuestions() {
		// TODO Auto-generated method stub
		System.out.println("I WANT THIS TO HAPPEN");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.databasequestion, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_databasequestion, container, false);
			return rootView;
		}
	}

}
