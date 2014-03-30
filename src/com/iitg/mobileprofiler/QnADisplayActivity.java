package com.iitg.mobileprofiler;

import org.iitg.mobileprofiler.db.DatabaseConnector;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
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

public class QnADisplayActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scroll = new ScrollView(getBaseContext());
		TableLayout tableLayout = new TableLayout(getApplicationContext());
		scroll.addView(tableLayout);
	    TableRow tableRow;
	    TextView textView;
	    DatabaseConnector databaseConnector = new DatabaseConnector();
	    int numberOfQuestions = databaseConnector.getMaxQuestionId();
	    for (int i = 1; i <= numberOfQuestions; i++) {
	        String question = databaseConnector.getQuestion(i);
	        if(question==null){
	        	continue;
	        }
	        Double answerWeight = databaseConnector.getWeightedAnswer(i);
	        tableRow = new TableRow(getApplicationContext());
	        textView = new TextView(getApplicationContext());
            textView.setText(question);
            textView.setPadding(20, 20, 20, 20);
            tableRow.addView(textView);
            textView = new TextView(getApplicationContext());
            textView.setText(answerWeight.toString());
            textView.setPadding(20, 20, 20, 20);
            tableRow.addView(textView);
	        tableLayout.addView(tableRow);
	    }
	    setContentView(scroll);
	    databaseConnector.closeDBConnection();
		getQuestions();
	}

	private void getQuestions() {
		// TODO Auto-generated method stub
		System.out.println("I WANT THIS TO HAPPEN");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qn_adisplay, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_qn_adisplay,
					container, false);
			return rootView;
		}
	}

}
