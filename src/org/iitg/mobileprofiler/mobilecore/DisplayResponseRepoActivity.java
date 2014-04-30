package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;
import java.util.Map;

import org.iitg.mobileprofiler.core.ResponseRecommendations;
import org.iitg.mobileprofiler.p2p.tools.UtilityFunctions;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayResponseRepoActivity extends Activity{

	
	ListView main_display;
	String[] questions ={};
	
	Double[] averagesRecommendations = {};
	Double[] participationRecommendations = {};
	Double[] entropyRecommendations = {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout .response);
		
		ArrayList<String> questionsList = new ArrayList<String>();
		ArrayList<Double> averageList = new ArrayList<Double>();
		ArrayList<Double> participationList = new ArrayList<Double>();
		ArrayList<Double> entropyList = new ArrayList<Double>();
		
		//ResponseRecommendations responseRecommendations = new ResponseRecommendations(UtilityFunctions.getHexDigest(MainActivity.phoneName));
		ResponseRecommendations responseRecommendations = new ResponseRecommendations(UtilityFunctions.getHexDigest("Revanth"));
		Map<String, Double> recommendations = responseRecommendations.getAverageRecommendation();
		
		for(String question : recommendations.keySet()){
			questionsList.add(question);
			averageList.add(recommendations.get(question));
		}
		
		recommendations = responseRecommendations.getParticipationHistoryRecommendation();
		
		for(String question : questionsList){
			participationList.add(recommendations.get(question));
		}
		
		recommendations = responseRecommendations.getEntropyRecommendation();
		
		for(String question : questionsList){
			entropyList.add(recommendations.get(question));
		}
		
		questions = new String[questionsList.size()];
	    questions = questionsList.toArray(questions);
	    
	    averagesRecommendations = new Double[averageList.size()];
	    averagesRecommendations = averageList.toArray(averagesRecommendations);
	    
	    participationRecommendations = new Double[participationList.size()];
	    participationRecommendations = participationList.toArray(participationRecommendations);

	    entropyRecommendations = new Double[entropyList.size()];
	    entropyRecommendations = entropyList.toArray(entropyRecommendations);
		
		main_display = (ListView) findViewById(R.id.listView_mainDisplay);
		//ArrayAdapter<String> my_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abc);
		
		My_Adapter my_adapter = new My_Adapter(this,questions);
		main_display.setAdapter(my_adapter);
		
	}

	public class My_Adapter extends ArrayAdapter<String>{

		Context context;
		public My_Adapter(Context c,String[] titles) {
			
			super(c,R.layout.list_block_3,titles);
			this.context =c;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_block_3, parent,false);
			}
						
			TextView data_text = (TextView) v.findViewById(R.id.tv_itemName);
			TextView text_one = (TextView) v.findViewById(R.id.tv_one);
			TextView text_two = (TextView) v.findViewById(R.id.tv_two);
			TextView text_three = (TextView) v.findViewById(R.id.tv_three);
					
			data_text.setText(questions[position]);
			String one_value = String.valueOf(averagesRecommendations[position]);
			text_one.setText(one_value);
			String two_value = String.valueOf(participationRecommendations[position]);
			text_two.setText(two_value);
			String three_value = String.valueOf(entropyRecommendations[position]);
			text_three.setText(three_value);
			
			return v;
		}		
	}


}
