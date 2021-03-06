package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;

import org.iitg.mobileprofiler.p2p.tools.PendingQuestion;

import com.iitg.mobileprofiler.R;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GiveFeedbackActivity extends FragmentActivity implements Communicator, OnItemClickListener{

	ListView mainDisplayListView;

	String[] questions = {};;
	
	Boolean[] privacyOptions = {};;
	
	Integer[] ratingValues = {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		setContentView(R.layout.listing_data);
		
		mainDisplayListView = (ListView) findViewById(R.id.listView_mainDisplay);
		
		loadArrays();
	    
		PendingQuestionsAdapter pendingQuestionsAdapter = new PendingQuestionsAdapter(this,questions);
		mainDisplayListView.setAdapter(pendingQuestionsAdapter);
		mainDisplayListView.setOnItemClickListener(this);
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public void PopOut(int list_position){

		Log.v("POP_OUP_1", questions[list_position] +" priv:"+String.valueOf(privacyOptions[list_position])+" Ratting:"+String.valueOf(ratingValues[list_position]));
		FragmentManager manager = getSupportFragmentManager();
		DialogPopOut myDialog = new DialogPopOut();
		Bundle args = new Bundle();

		args.putInt("list_position", list_position);
		args.putString("data", questions[list_position]);
		args.putBoolean("mode", privacyOptions[list_position]);
		args.putInt("num", ratingValues[list_position]);
		myDialog.setArguments(args);
		myDialog.show(manager, "value");
		
	}
	
	public void loadArrays(){
		ArrayList<String> questionsList = new ArrayList<String>();
		ArrayList<Boolean> privacyList = new ArrayList<Boolean>();
		ArrayList<Integer> ratingsList = new ArrayList<Integer>();
		
		for(PendingQuestion pendingQuestion : FirstPage.userNodePeer.getPendingQuestions()){
			questionsList.add(pendingQuestion.getQuestion());
			privacyList.add(true);
			ratingsList.add(pendingQuestion.getAnswer());
		}
		
		questions = new String[questionsList.size()];
	    questions = questionsList.toArray(questions);
	    
	    ratingValues = new Integer[ratingsList.size()];
	    ratingValues = ratingsList.toArray(ratingValues);
	    
	    privacyOptions = new Boolean[privacyList.size()];
	    privacyOptions = privacyList.toArray(privacyOptions);
	}
	
	public class PendingQuestionsAdapter extends ArrayAdapter<String>{

		Context context;
		public PendingQuestionsAdapter(Context c,String[] titles) {
			
			super(c,R.layout.list_block_new,titles);
			this.context =c;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View v = convertView;
			if(v==null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_block_new, parent,false);
			}
						
			TextView data_text = (TextView) v.findViewById(R.id.tv_itemName);
			ImageView mode_icon = (ImageView) v.findViewById(R.id.iv_mode);
			TextView text_ratting = (TextView) v.findViewById(R.id.tv_ratting);
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopOut(position);  //This will make the dialog appear
				}
			});
			
			data_text.setText(questions[position]);
			if(privacyOptions[position] ==true){
				mode_icon.setImageResource(R.drawable.lock);
			}
			String rating = String.valueOf(ratingValues[position]);
			text_ratting.setText(rating);
			
			return v;
		}		
	}

	@Override
	public void makeChange(int position, boolean isPrivate, int rating) {
		
		View selectedView = mainDisplayListView.getChildAt(position);
		
		ImageView s_mode_icon = (ImageView) selectedView.findViewById(R.id.iv_mode);
		TextView s_text_ratting = (TextView) selectedView.findViewById(R.id.tv_ratting);

		if(isPrivate==true){
			s_mode_icon.setImageResource(R.drawable.lock);
			
		}else{
			s_mode_icon.setImageResource(R.drawable.my_unlock);
		}

		s_text_ratting.setText(String.valueOf(rating));
		
		//Data Modification
		privacyOptions[position] = isPrivate;
		ratingValues[position] = rating;
		
		PendingQuestion pendingQuestion = FirstPage.userNodePeer.getPendingQuestions().get(position);
		FirstPage.userNodePeer.getPendingQuestions().remove(position);
		
		pendingQuestion.setAnswer(rating);
		pendingQuestion.sendReply(!isPrivate);
		
		loadArrays();
		
		PendingQuestionsAdapter pendingQuestionsAdapter = new PendingQuestionsAdapter(this,questions);
		mainDisplayListView.setAdapter(pendingQuestionsAdapter);
		mainDisplayListView.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		Log.i("TEST", "ON ITEM CLICK CALLED");
		//PopOut(data[position],privacy[position],ratting_value[position]);  //This will make the dialog appear
	}	
	
}
