package org.iitg.mobileprofiler.mobilecore;

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
	String[] data ={"Suppose this is a long question ? aba aljafk ","Mango","Banana","Fruit","Grape","Tomato","Strawberry"};
	
	double[] one = {6,7,9,4,5,2,3};
	double[] two = {1,3,2,4,5,2,3};
	double[] three = {3,3,2,4,5,2,3};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout .response);
		
		
		main_display = (ListView) findViewById(R.id.listView_mainDisplay);
		//ArrayAdapter<String> my_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abc);
		
		My_Adapter my_adapter = new My_Adapter(this,data);
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
					
			data_text.setText(data[position]);
			String one_value = String.valueOf(one[position]);
			text_one.setText(one_value);
			String two_value = String.valueOf(two[position]);
			text_two.setText(two_value);
			String three_value = String.valueOf(three[position]);
			text_three.setText(three_value);
			
			return v;
		}		
	}


}
