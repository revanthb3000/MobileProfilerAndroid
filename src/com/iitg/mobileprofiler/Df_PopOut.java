package com.iitg.mobileprofiler;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Df_PopOut extends DialogFragment implements OnClickListener{
	
	
	TextView vtv_heading,vtv_value,t_ratting;
	Button b_mode;
	Button a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,a10;
	Button[] a ={a0,a1,a2,a3,a4,a5,a6,a7,a8,a9}; 
	Button cancel,set;
	
	Communicator fc;
	int list_position;
	String data;
	boolean b;
	int r;
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		fc = (Communicator) activity;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.df_pop, null);
		
		
		//initTypeface();
		list_position = getArguments().getInt("list_position",1);
		data=getArguments().getString("data");
		b =getArguments().getBoolean("mode",false);
		r = getArguments().getInt("num",1);
		
		setVariable(view);
		setCancelable(false);
		return view;
	}
	
	private void initTypeface() {
		//
		//Typeface myFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/forque.ttf");
		//vtv_heading.setTypeface(myFont);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);		 
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
	
	
	private void setVariable(View view) {
		// 
		
		vtv_heading=(TextView) view.findViewById(R.id.tv_title);
		vtv_value = (TextView) view.findViewById(R.id.tv_fvalue);
		t_ratting = (TextView) view.findViewById(R.id.tv_frag_rate);
		
		cancel = (Button) view.findViewById(R.id.btn_cancel);
		set = (Button) view.findViewById(R.id.btn_set);
			
		
		a1 = (Button) view.findViewById(R.id.btn_1);
		a2 = (Button) view.findViewById(R.id.btn_2);
		a3 = (Button) view.findViewById(R.id.btn_3);
		a4 = (Button) view.findViewById(R.id.btn_4);
		a5 = (Button) view.findViewById(R.id.btn_5);
		a6 = (Button) view.findViewById(R.id.btn_6);
		a7 = (Button) view.findViewById(R.id.btn_7);
		a8 = (Button) view.findViewById(R.id.btn_8);
		a9 = (Button) view.findViewById(R.id.btn_9);
		a10 = (Button) view.findViewById(R.id.btn_10);
		b_mode = (Button) view.findViewById(R.id.btn_mode);
		
		cancel.setOnClickListener(this);
		set.setOnClickListener(this);
		b_mode.setOnClickListener(this);

		
		a1.setOnClickListener(this);
		a2.setOnClickListener(this);
		a3.setOnClickListener(this);
		a4.setOnClickListener(this);
		a5.setOnClickListener(this);
		a6.setOnClickListener(this);
		a7.setOnClickListener(this);
		a8.setOnClickListener(this);
		a9.setOnClickListener(this);
		a10.setOnClickListener(this);
		
		
		vtv_value.setText(data);
		Log.v("DF", "String value:"+data);
		t_ratting.setText(String.valueOf(r));
		Log.i("DF", "ratting value:"+String.valueOf(r));
		
		if(b==true){
		b_mode.setText("Private");
		b_mode.setBackgroundResource(R.drawable.btn_frag_red);
		}
	}
	
	


	@Override
	public void onClick(View v) {
		// 
		
		switch(v.getId()){
		
		case R.id.btn_cancel:
			dismiss();
			break;
			
		case R.id.btn_set:
			
			int new_ratting = Integer.parseInt(t_ratting.getText().toString());
			fc.makeChange(list_position,b,new_ratting);
			dismiss();
			break;
			
			//------------------------------------------------------------------------
			
		case R.id.btn_1:
			t_ratting.setText(String.valueOf(1));
			break;
			
		case R.id.btn_2:
			t_ratting.setText(String.valueOf(2));
			break;
			
		case R.id.btn_3:
			t_ratting.setText(String.valueOf(3));
			break;
			
		case R.id.btn_4:
			t_ratting.setText(String.valueOf(4));
			break;
			
		case R.id.btn_5:
			t_ratting.setText(String.valueOf(5));
			break;
			
		case R.id.btn_6:
			t_ratting.setText(String.valueOf(6));
			break;
			
		case R.id.btn_7:
			t_ratting.setText(String.valueOf(7));
			break;
			
		case R.id.btn_8:
			t_ratting.setText(String.valueOf(8));
			break;
			
		case R.id.btn_9:
			t_ratting.setText(String.valueOf(9));
			break;
			
		case R.id.btn_10:
			t_ratting.setText(String.valueOf(10));
			break;
			
		
		case R.id.btn_mode:
			if(b==false){// means it is in public before
				b_mode.setBackgroundResource(R.drawable.btn_frag_red);
				b_mode.setText("Private");
				b=true;
			}else{// means it is in private before
				b_mode.setBackgroundResource(R.drawable.btn_frag_green);
				b_mode.setText("Public");
				b=false;
			}
			
			break;
		
		}
		
	}
}
