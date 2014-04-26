package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;

import org.iitg.mobileprofiler.db.DatabaseConnector;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GetFeedbackActivity extends Activity{

	
	ListView mainListView;
	
	String[] questions = {};
	
	String[] classNames ={};
	
	Double[] weights = {};
	
	Integer[] numberOfRatings = {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listing_data);
		
		ArrayList<String> questionsList = new ArrayList<String>();
		ArrayList<String> classNamesList = new ArrayList<String>();
		ArrayList<Double> weightsList = new ArrayList<Double>();
		ArrayList<Integer> numberOfRatingsList = new ArrayList<Integer>();
		
	    DatabaseConnector databaseConnector = new DatabaseConnector();
	    int numberOfQuestions = databaseConnector.getMaxQuestionId();
	    for (int i = 1; i <= numberOfQuestions; i++) {
	        String question = databaseConnector.getQuestion(i);
	        if(question==null){
	        	continue;
	        }
	        questionsList.add(question);
	        classNamesList.add(databaseConnector.getQuestionClassName(i));
	        weightsList.add(databaseConnector.getWeightedAnswer(i));
	        numberOfRatingsList.add(databaseConnector.getNumberOfReplies(i));
	    }
	    databaseConnector.closeDBConnection();
	    
	    questions = new String[questionsList.size()];
	    questions = questionsList.toArray(questions);
	    
	    classNames = new String[classNamesList.size()];
	    classNames = questionsList.toArray(classNames);
	    
	    weights = new Double[weightsList.size()];
	    weights = weightsList.toArray(weights);
	    
	    numberOfRatings = new Integer[numberOfRatingsList.size()];
		numberOfRatings = numberOfRatingsList.toArray(numberOfRatings);
	    
		mainListView = (ListView) findViewById(R.id.listView_mainDisplay);
		//ArrayAdapter<String> my_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abc);
		
		QuestionsAdaptar questionsAdaptar = new QuestionsAdaptar(this,questions);
		mainListView.setAdapter(questionsAdaptar);
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public class QuestionsAdaptar extends ArrayAdapter<String>{

		Context context;
		public QuestionsAdaptar(Context c,String[] titles) {
			
			super(c,R.layout.list_block_2,titles);
			this.context =c;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_block_2, parent,false);
			}
						
			TextView questionTextView = (TextView) v.findViewById(R.id.tv_itemName);
			TextView classNameTextView = (TextView) v.findViewById(R.id.tv_type);
			TextView ratingTextView = (TextView) v.findViewById(R.id.tv_ratting);
			TextView numberofResponsesTextView = (TextView) v.findViewById(R.id.tv_no_ofResponse);
					
			questionTextView.setText(questions[position]);
			classNameTextView.setText(classNames[position]);
			ratingTextView.setText(String.valueOf(weights[position]));
			numberofResponsesTextView.setText(String.valueOf(numberOfRatings[position]));
			
			return v;
		}		
	}
}
