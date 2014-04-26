package org.iitg.mobileprofiler.mobilecore;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AskQuestionActivity extends Activity{

	TextView textView;
	Button button;
	EditText editText;
	Spinner spinner;
	String[] topics = {"Politics","Movies","Cricket","Animation",
					   "Entertainment", "Technology", "TV Show",
					   "News", "Social","Football", "Tennis"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_display);
		
		
		button = (Button)findViewById(R.id.btn_sumit);
		editText = (EditText) findViewById(R.id.et_question);
		spinner = (Spinner) findViewById( R.id.sp_1);
		
		
		ArrayAdapter<String> adapter_fileName= new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_block, topics);
		spinner.setAdapter(adapter_fileName);		
		
	}
	
	public void Submit(View v){
		String question = editText.getText().toString();
		String selectedTopic = topics[spinner.getSelectedItemPosition()];
		if(question.equals("")){
			Toast.makeText(this, "Please enter a String", Toast.LENGTH_SHORT).show();
		}else{
			MainActivity.userNodePeer.sendQuestionToPeers(question, selectedTopic);
		}
	}
	
}
