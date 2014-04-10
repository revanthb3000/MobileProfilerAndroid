package com.iitg.mobileprofiler;

import java.util.ArrayList;

import org.iitg.mobileprofiler.db.DatabaseConnector;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Questions extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.activity_questions);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String value = extras.getString("msg");
		    System.out.println(value);
		}
		//Set of quesitons
		DatabaseConnector databaseConnector = new DatabaseConnector();
		ArrayList<String> questions = databaseConnector.getQuestionsList();
		databaseConnector.closeDBConnection();
		String[] textArray = new String[questions.size()];
		textArray = questions.toArray(textArray);
		int length=textArray.length;
		LinearLayout layout = new LinearLayout(this);
		setContentView(layout);
		layout.setOrientation(LinearLayout.VERTICAL);        
		for(int i=0;i<length;i++)
		{
		    TextView tv=new TextView(getApplicationContext());
		    tv.setText(textArray[i]);
		    final String msg = textArray[i];
		    tv.setClickable(true);
		    tv.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	
	            	Intent intent = new Intent(Questions.this, Responses.class);
	            	// You can either send the responses or any identifier to get the responses in responses class.
	            	// Use the same code to print the responses in the other activity
	        		intent.putExtra("msg", msg);
	        		startActivity(intent);
	               System.out.println(msg);
	             }
            });
		    layout.addView(tv);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_questions,
					container, false);
			return rootView;
		}
	}

}
