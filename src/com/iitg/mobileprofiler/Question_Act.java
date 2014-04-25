package com.iitg.mobileprofiler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Question_Act extends Activity{

	TextView t_q;
	Button ok;
	EditText et_q;
	Spinner sp_topic;
	String[] topic = {"abc","alf","topic2","topic3"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_display);
		
		
		ok = (Button)findViewById(R.id.btn_sumit);
		et_q = (EditText) findViewById(R.id.et_question);
		sp_topic = (Spinner) findViewById( R.id.sp_1);
		
		
		ArrayAdapter<String> adapter_fileName= new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_block, topic);
		sp_topic.setAdapter(adapter_fileName);
		
		
	}
	
	public void Submit(View v){
		String qus = et_q.getText().toString();
		String sellected_topic = topic[sp_topic.getSelectedItemPosition()];
		if(qus.equals("")){
			Toast.makeText(this, "Please enter a String", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "Question: "+qus+"\n"+"topic:"+sellected_topic, Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
