package com.iitg.mobileprofiler;


import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.widget.ListView.FixedViewInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyList extends FragmentActivity implements Communicator, OnItemClickListener{

	ListView main_display;
	String[] data ={"Suppose this is a long question ? aba aljafk lafa falfjalf lajf ","Mango","Banana","Fruit","Grape","Tomato","Strawberry"};
	Boolean[] privacy = {true,true,false,true,true,false,false};
	int[] ratting_value = {6,7,9,4,5,2,3};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		boolean t =isTablet(this);
//		
//		if(t==true){
//			Log.i("ABC", "is called");
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}else{
//			Log.v("ABC", "is called");
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
		
		
		
		setContentView(R.layout.listing_data);
		
		main_display = (ListView) findViewById(R.id.listView_mainDisplay);
		//ArrayAdapter<String> my_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, abc);
		
		My_Adapter my_adapter = new My_Adapter(this,data);
		main_display.setAdapter(my_adapter);
		main_display.setOnItemClickListener(this);
		
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	//...........................................................................
	public void PopOut(int list_position){
		//android.app.FragmentManager mannager = getf
		Log.v("POP_OUP_1", data[list_position] +" priv:"+String.valueOf(privacy[list_position])+" Ratting:"+String.valueOf(ratting_value[list_position]));
		FragmentManager manager = getSupportFragmentManager();
		Df_PopOut myDialog = new Df_PopOut();
		Bundle args = new Bundle();
		//Log.v("POP_OUP_2", name+" priv:"+String.valueOf(b)+" Ratting:"+String.valueOf(r));
		args.putInt("list_position", list_position);
		args.putString("data", data[list_position]);
		args.putBoolean("mode", privacy[list_position]);
		args.putInt("num", ratting_value[list_position]);
		myDialog.setArguments(args);
		//Log.v("POP_OUP", name+" priv:"+String.valueOf(b)+" Ratting:"+String.valueOf(r));
		myDialog.show(manager, "value");
		
	}
	
	public class My_Adapter extends ArrayAdapter<String>{

		Context context;
		public My_Adapter(Context c,String[] titles) {
			
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
			
			
			data_text.setText(data[position]);
			if(privacy[position] ==true){
				mode_icon.setImageResource(R.drawable.lock);
			}
			String ratting = String.valueOf(ratting_value[position]);
			text_ratting.setText(ratting);
			
			return v;
		}		
	}

	@Override
	public void makeChange(int position, boolean mode, int ratting) {
		
		Log.v("PPZ", "First");
		View sellected_View = main_display.getChildAt(position);//(View) main_display.getItemAtPosition(position);
		//main_display.getitem
		//TextView s_data_text = (TextView) sellected_View.findViewById(R.id.tv_itemName);
		
		ImageView s_mode_icon = (ImageView) sellected_View.findViewById(R.id.iv_mode);
		Log.v("PPZ", "First");
		TextView s_text_ratting = (TextView) sellected_View.findViewById(R.id.tv_ratting);
		Log.v("PPZ", "First");
		if(mode==true){
			s_mode_icon.setImageResource(R.drawable.lock);
			
		}else{
			s_mode_icon.setImageResource(R.drawable.my_unlock);
		}
		Log.v("PPZ", "First");
		s_text_ratting.setText(String.valueOf(ratting));
		
		//Data Modification
		privacy[position] = mode;
		ratting_value[position] = ratting;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		Log.i("TEST", "ON ITEM CLICK CALLED");
		//PopOut(data[position],privacy[position],ratting_value[position]);  //This will make the dialog appear
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		String new_check_box_code="";
//		for(int i=0;i<no_of_Question;i++){
//			if(topics[i].isChecked()){
//				new_check_box_code = new_check_box_code.concat("1");
//			}else{
//				new_check_box_code = new_check_box_code.concat("0");
//			}
//		}
//		WriteSP(new_check_box_code);//This will write the new code... hehe
//	}
	
	
	
}
