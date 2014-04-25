package com.iitg.mobileprofiler;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class MyList2 extends Activity{

	
	ListView main_display;
	String[] data ={"Suppose this is a long question ? aba aljafk ","Mango","Banana","Fruit","Grape","Tomato","Strawberry"};
	String[] group ={"Group 1","Group 2","Banana","Fruit","Grape","g2","g1"};
	int[] ratting_value = {6,7,9,4,5,2,3};
	int[] no_of_rat = {1,3,2,4,5,2,3};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listing_data);
		
		main_display = (ListView) findViewById(R.id.listView_mainDisplay);
		//ArrayAdapter<String> my_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abc);
		
		My_Adapter my_adapter = new My_Adapter(this,data);
		main_display.setAdapter(my_adapter);
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	//...........................................................................

	
	public class My_Adapter extends ArrayAdapter<String>{

		Context context;
		public My_Adapter(Context c,String[] titles) {
			
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
						
			TextView data_text = (TextView) v.findViewById(R.id.tv_itemName);
			TextView text_group = (TextView) v.findViewById(R.id.tv_type);
			TextView text_ratting = (TextView) v.findViewById(R.id.tv_ratting);
			TextView text_response = (TextView) v.findViewById(R.id.tv_no_ofResponse);
					
			data_text.setText(data[position]);
			text_group.setText(group[position]);
			String ratting = String.valueOf(ratting_value[position]);
			text_ratting.setText(ratting);
			String no_of_r = String.valueOf(no_of_rat[position]);
			
			return v;
		}		
	}
}
